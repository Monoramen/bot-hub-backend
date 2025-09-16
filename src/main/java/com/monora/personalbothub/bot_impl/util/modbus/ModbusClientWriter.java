package com.monora.personalbothub.bot_impl.util.modbus;

import com.digitalpetri.modbus.client.ModbusRtuClient;
import com.digitalpetri.modbus.pdu.*;
import com.monora.personalbothub.bot_impl.util.modbus.enums.RuntimeParameter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ModbusClientWriter {

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 30;

    private final ModbusConnectionManager connectionManager;
    private final ParameterRegistry parameterRegistry;

    public ModbusClientWriter(ModbusConnectionManager connectionManager, ParameterRegistry parameterRegistry) {
        this.connectionManager = connectionManager;
        this.parameterRegistry = parameterRegistry;
    }

    // ✅ Вспомогательный метод: ленивое получение клиента с попыткой подключения
    private ModbusRtuClient getConnectedClient(int address) {
        if (!connectionManager.isConnected()) {
            log.debug("Modbus не подключён — попытка подключения перед записью по адресу 0x{}...", String.format("%04X", address));
            connectionManager.connect();
            if (!connectionManager.isConnected()) {
                log.warn("Не удалось подключиться к Modbus перед записью по адресу 0x{}", String.format("%04X", address));
                return null;
            }
        }

        ModbusRtuClient client = (ModbusRtuClient) connectionManager.getClient();
        if (client == null) {
            log.error("Клиент Modbus недоступен для записи по адресу 0x{}", String.format("%04X", address));
        }
        return client;
    }

    public boolean writeMultipleRegisters(int startAddress, int[] values, int unitId) {
        ModbusRtuClient client = getConnectedClient(startAddress); // ✅ ленивое подключение
        if (client == null) return false;

        ByteBuf buffer = Unpooled.buffer(values.length * 2);
        for (int v : values) {
            buffer.writeShort(v);
        }
        byte[] byteValues = new byte[buffer.readableBytes()];
        buffer.readBytes(byteValues);

        WriteMultipleRegistersRequest request =
                new WriteMultipleRegistersRequest(startAddress, values.length, byteValues);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                WriteMultipleRegistersResponse response = client.writeMultipleRegisters(unitId, request);
                if (response != null) {
                    log.info("Успешная групповая запись {} регистров с 0x{} для unitId {}",
                            values.length, String.format("%04X", startAddress), unitId);
                    return true;
                }
            } catch (Exception e) {
                log.warn("Попытка {}: Ошибка записи группы регистров с 0x{}: {}",
                        attempt, String.format("%04X", startAddress), e.getMessage());
            }
            sleepBeforeRetry();
        }

        log.error("Не удалось выполнить групповую запись регистров с 0x{} после {} попыток.",
                String.format("%04X", startAddress), MAX_RETRIES);
        return false;
    }

    public boolean writeInt16(int address, int value, int unitId) {
        return writeMultipleRegisters(address, new int[]{value}, unitId);
    }

    public boolean writeInt8(int address, int value, int unitId) {
        return writeMultipleRegisters(address, new int[]{value & 0xFF}, unitId);
    }

    public boolean writeStoredDot(int address, double value, int unitId) {
        log.info("Подготовка к записи STORED_DOT по адресу 0x{} со значением {}", String.format("%04X", address), value);

        int dotPosition = findDotPosition(value);
        int scaledValue = (int) (value * Math.pow(10, dotPosition));
        int[] values = new int[]{scaledValue, dotPosition};

        boolean success = writeMultipleRegisters(address, values, unitId);

        if (success) {
            log.info("STORED_DOT успешно записано: {} (масштабированное: {}, точка: {})", value, scaledValue, dotPosition);
        } else {
            log.error("Ошибка записи STORED_DOT по адресу 0x{}", String.format("%04X", address));
        }

        return success;
    }

    public boolean writeSingleCoil(int address, boolean value, int unitId) {
        ModbusRtuClient client = getConnectedClient(address); // ✅ ленивое подключение
        if (client == null) {
            return false;
        }

        WriteSingleCoilRequest request = new WriteSingleCoilRequest(address, value);
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                WriteSingleCoilResponse response = client.writeSingleCoil(unitId, request);
                if (response != null) {
                    log.info("Успешная запись в катушку 0x{} для unitId {}", String.format("%04X", address), unitId);
                    return true;
                } else {
                    log.warn("Попытка {}: Запись катушки 0x{} не подтверждена, повтор...", attempt, String.format("%04X", address));
                }
            } catch (Exception e) {
                log.warn("Попытка {}: Ошибка записи катушки по адресу 0x{}: {}", attempt, String.format("%04X", address), e.getMessage());
            }
            sleepBeforeRetry();
        }
        log.error("Не удалось записать катушку 0x{} после {} попыток.", String.format("%04X", address), MAX_RETRIES);
        return false;
    }

    public boolean startStopTechProgram(boolean start, int unitId) {
        log.info("Запуск/останов программы: {}, unitId: {}", start, unitId);
        return writeSingleCoil(RuntimeParameter.R_S.getAddress(), start, unitId);
    }

    public boolean selectTechProgram(int programNumber, int unitId) {
        if (programNumber < 1 || programNumber > 3) {
            log.error("Номер программы должен быть от 1 до 3. Получено: {}", programNumber);
            return false;
        }

        log.info("Выбор программы технолога #{}, Unit ID: {}", programNumber, unitId);
        return writeSingleRegister(RuntimeParameter.R_PRG.getAddress(), programNumber, unitId);
    }

    @SneakyThrows
    public boolean writeSingleRegister(int address, int value, int unitId) {
        ModbusRtuClient client = getConnectedClient(address); // ✅ ленивое подключение
        if (client == null) {
            log.error("Клиент Modbus не доступен, пропуск записи регистра по адресу 0x{}", String.format("%04X", address));
            return false;
        }

        WriteSingleRegisterRequest request = new WriteSingleRegisterRequest(address, value);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            WriteSingleRegisterResponse response = client.writeSingleRegister(unitId, request);
            if (response != null) {
                log.info("Успешно записан регистр 0x{} со значением {} на unitId {}", String.format("%04X", address), value, unitId);
                return true;
            }
            sleepBeforeRetry();
        }

        log.error("Не удалось записать регистр 0x{} после {} попыток.", String.format("%04X", address), MAX_RETRIES);
        return false;
    }

    private int findDotPosition(double value) {
        String text = String.format("%.6f", Math.abs(value)).replaceAll("0*$", "").replaceAll("\\.$", "");
        int dotIndex = text.indexOf('.');
        return dotIndex == -1 ? 0 : text.length() - dotIndex - 1;
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