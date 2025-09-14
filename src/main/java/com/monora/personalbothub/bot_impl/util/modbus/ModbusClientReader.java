package com.monora.personalbothub.bot_impl.util.modbus;

import com.digitalpetri.modbus.client.ModbusRtuClient;
import com.digitalpetri.modbus.pdu.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.pdu.ReadHoldingRegistersResponse;
import com.monora.personalbothub.bot_impl.util.modbus.enums.ConfigParameter;
import com.monora.personalbothub.bot_impl.util.modbus.enums.RuntimeParameter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Optional;

@Component
@Slf4j
public class ModbusClientReader {
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 50;

    private final ModbusConnectionManager connectionManager;
    private final ParameterRegistry parameterRegistry;

    public ModbusClientReader(ModbusConnectionManager connectionManager, ParameterRegistry parameterRegistry) {
        this.connectionManager = connectionManager;
        this.parameterRegistry = parameterRegistry;
    }

    @PostConstruct
    public void init() {
        try {
            log.info("Инициализация ModbusClient, подключение к устройству...");
            connectionManager.connect();
            log.info("ModbusClient успешно инициализирован");
        } catch (Exception e) {
            log.error("Ошибка инициализации ModbusClient: {}", e.getMessage(), e);
        }
    }

    /**
     * Чтение регистров по адресу с повторными попытками
     */
    public Optional<byte[]> readRegisters(int address, int quantity, int unitId) {
        ModbusRtuClient client = connectionManager.getClient();
        if (client == null) {
            log.error("Клиент Modbus не доступен, пропуск чтения регистров по адресу 0x{}", String.format("%04X", address));
            return Optional.empty();
        }

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(address, quantity);
                log.debug("Попытка {}: Запрос на чтение: unitId={}, адрес=0x{}, кол-во={}", attempt, unitId, String.format("%04X", address), quantity);

                ReadHoldingRegistersResponse response = client.readHoldingRegisters(unitId, request);
                byte[] registers = response.registers();
                applyReadDelay();
                return Optional.of(registers);
            } catch (Exception e) {
                log.warn("Попытка {}: Ошибка чтения регистров по адресу 0x{}: {}", attempt, String.format("%04X", address), e.getMessage());
                if (attempt < MAX_RETRIES) {
                    sleepBeforeRetry();
                } else {
                    log.error("Не удалось прочитать регистр 0x{} после {} попыток: {}", String.format("%04X", address), MAX_RETRIES, e.getMessage());
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }


    /**
     * Чтение одного параметра в зависимости от типа данных
     */
    private Optional<Object> readParameter(int address, String dataType, int unitId) {
        try {
            switch (dataType) {
                case "int8":
                    return readInt8(address, unitId);
                case "int16":
                    return readInt16(address, unitId);
                case "STORED_DOT":
                    return readStoredDot(address, unitId);
                case "Float32":
                    return readFloat32(address, unitId);
                case "ASCII":
                    return readAscii(address, unitId);
                default:
                    log.warn("Неизвестный тип данных: {}", dataType);
                    return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Ошибка чтения параметра по адресу 0x{}: {}", String.format("%04X", address), e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Чтение int8 (1 регистр, младший байт)
     */
    private Optional<Object> readInt8(int address, int unitId) {
        Optional<byte[]> result = readRegisters(address, 1, unitId);
        return result.map(registers -> (int) (registers[1] & 0xFF));
    }

    /**
     * Чтение int16 (1 регистр)
     */
    private Optional<Object> readInt16(int address, int unitId) {
        Optional<byte[]> result = readRegisters(address, 1, unitId);
        return result.map(registers -> ByteBuffer.wrap(registers).getShort());
    }

    /**
     * Чтение STORED_DOT (зависит от положения десятичной точки)
     */
    private Optional<Object> readStoredDot(int address, int unitId) {
        Optional<byte[]> result = readRegisters(address, 1, unitId);
        if (result.isEmpty()) return Optional.empty();

        int rawValue = ByteBuffer.wrap(result.get()).getShort();
        Optional<Integer> dot = readDotParameter(address + 1, unitId);
        double value = dot.map(d -> rawValue / Math.pow(10, d)).orElse((double) rawValue);
        return Optional.of(value);
    }

    /**
     * Чтение положения десятичной точки (int8)
     */
    private Optional<Integer> readDotParameter(int address, int unitId) {
        Optional<byte[]> result = readRegisters(address, 1, unitId);
        return result.map(registers -> (int) (registers[1] & 0xFF));
    }

    /**
     * Чтение Float32 (2 регистра)
     */
    private Optional<Object> readFloat32(int address, int unitId) {
        Optional<byte[]> result = readRegisters(address, 2, unitId);
        return result.map(registers -> {
            byte[] swapped = new byte[4];
            swapped[0] = registers[2];
            swapped[1] = registers[3];
            swapped[2] = registers[0];
            swapped[3] = registers[1];
            return FloatUtils.byteArrayToFloat(swapped);
        });
    }

    /**
     * Чтение ASCII (предполагается, что строка занимает несколько регистров)
     */
    private Optional<Object> readAscii(int address, int unitId) {
        Optional<byte[]> result = readRegisters(address, 2, unitId);
        return result.map(registers -> new String(registers).trim());
    }

    /**
     * Совместимость: метод для чтения температуры
     */
    public float readTemperature(int unitId) {
        RuntimeParameter tempParam = parameterRegistry.getRuntimeParameter("rEAd");
        if (tempParam == null) {
            log.error("Параметр rEAd не найден в реестре");
            return 0;
        }
        int quantity = 2;

        try {
            Optional<byte[]> result = readRegisters(RuntimeParameter.READ.getAddress(), quantity, unitId);
            if (result.isPresent()) {
                byte[] registers = result.get();
                byte[] swapped = new byte[4];
                swapped[0] = registers[2];
                swapped[1] = registers[3];
                swapped[2] = registers[0];
                swapped[3] = registers[1];

                float value = FloatUtils.byteArrayToFloat(swapped);
                log.info("Температура: {} °C", value);
                applyReadDelay();
                return value;
            } else {
                log.warn("Не удалось прочитать значение для unitId {}", unitId);
            }
        } catch (Exception e) {
            log.error("Ошибка чтения параметра rEAd: {}", e.getMessage());
        }
        return 0;
    }


    public Optional<Integer> readCurrentPower(int unitId) {
        RuntimeParameter powerParam = RuntimeParameter.R_OUT; // ✅ правильно — это мощность
        log.debug("Чтение R_OUT: адрес=0x{}, unitId={}",
                String.format("%04X", powerParam.getAddress()), unitId);

        try {
            Optional<byte[]> result = readRegisters(powerParam.getAddress(), 2, unitId);

            if (result.isPresent()) {
                byte[] registers = result.get();

                log.debug("Полученные байты: [0x{}, 0x{}]",
                        String.format("%02X", registers[0]),
                        String.format("%02X", registers[1]));

                // ✅ Правильное преобразование int16 (Big-Endian)
                int rawValue = ((registers[0] & 0xFF) << 8) | (registers[1] & 0xFF);

                // ✅ Преобразование в проценты: 0.1 ед. = 1%
                int powerPercent = (int) Math.round(rawValue / 10.0);

                log.info("Текущая мощность: {}%", powerPercent);
                applyReadDelay();
                return Optional.of(powerPercent);
            } else {
                log.warn("readRegisters вернул empty для R_OUT");
            }
        } catch (Exception e) {
            log.error("Ошибка чтения R_OUT: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }


    public Optional<Integer> readCurrentProgram(int unitId) {
        // Используем прямое обращение к enum вместо реестра
        RuntimeParameter programParam = RuntimeParameter.R_PRG;

        log.debug("Чтение R_PRG: адрес=0x{}, unitId={}",
                String.format("%04X", programParam.getAddress()), unitId);

        try {
            Optional<byte[]> result = readRegisters(programParam.getAddress(), 1, unitId);

            if (result.isPresent()) {
                byte[] registers = result.get();

                // Детальное логирование
                log.debug("Полученные байты: [0x{}, 0x{}]",
                        String.format("%02X", registers[0]),
                        String.format("%02X", registers[1]));

                // Преобразование байтов в число
                int programNumber = ((registers[0] & 0xFF) << 8) | (registers[1] & 0xFF);

                log.info("Текущая программа: #{}", programNumber);
                applyReadDelay();
                return Optional.of(programNumber);
            } else {
                log.warn("readRegisters вернул empty для R_PRG");
            }
        } catch (Exception e) {
            log.error("Ошибка чтения R_PRG: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }
    private void applyReadDelay() {
        if (RETRY_DELAY_MS > 0) {
            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Прерывание во время задержки чтения: {}", e.getMessage());
            }
        }
    }

    private void sleepBeforeRetry() {
        try {
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.error("Прервано ожидание перед повторной попыткой: {}", ie.getMessage());
        }
    }
}