package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_impl.service.TechProgramReadParameterService;
import com.monora.personalbothub.bot_impl.util.modbus.ModbusClientReader;
import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;
import com.monora.personalbothub.bot_impl.util.modbus.enums.RuntimeParameter;
import com.monora.personalbothub.bot_impl.util.modbus.enums.TechProgramParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TechProgramReadParameterServiceImpl implements TechProgramReadParameterService {

    private final ModbusClientReader modbusClientReader;

    /**
     * Загрузка параметров технологической программы
     *
     * @param unitId Modbus Unit ID устройства
     * @return Map, где ключ - имя параметра, значение - считанное значение
     */

    @Async("modbusExecutor")
    @Override
    public CompletableFuture<Map<String, Object>> loadTechProgramParameters(int unitId) {
        Map<String, Object> results = new HashMap<>();
        for (TechProgramParameter param : TechProgramParameter.values()) {
            Optional<Object> value = readParameterInternal(unitId, param);
            value.ifPresent(v -> results.put(param.getName(), v));
        }
        log.info("Загружено {} параметров технологической программы", results.size());
        if (results.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "Не удалось считать статус устройства.");
        }
        return CompletableFuture.completedFuture(results);
    }

    @Async("modbusExecutor")
    @Override
    public CompletableFuture<Map<String, Object>> loadProgramParameters(int programNumber, int unitId) {
        Map<String, Map<String, Object>> categorizedParameters = new HashMap<>();

        // Создаем префикс для фильтрации (например, "P1.")
        String prefix = "P" + programNumber + ".";

        // Фильтруем TechProgramParameter с помощью стрима
        Map<String, Object> results = Arrays.stream(TechProgramParameter.values())
                .filter(param -> param.getName().startsWith(prefix) || param.getName().equals("t.SCL"))
                .collect(Collectors.toMap(
                        TechProgramParameter::getName,
                        param -> readParameterInternal(unitId, param)
                                .orElse(null)
                ));

        log.info("Загружено {} параметров для программы №{}", results.size(), programNumber);
        return CompletableFuture.completedFuture(results);
    }

    @Async("modbusExecutor")
    @Override
    public CompletableFuture<FiringStatus> readStatusDevice(int unitId) {
        Optional<byte[]> result = modbusClientReader.readRegisters(RuntimeParameter.R_ST.getAddress(), 1, unitId);

        if (result.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "Не удалось считать статус устройства.");
        }

        byte[] registers = result.get();
        int status = ByteBuffer.wrap(registers).getShort();

        FiringStatus firingStatus = FiringStatus.fromCode(status)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Неизвестный статус: " + status));

        return CompletableFuture.completedFuture(firingStatus);
    }


    @Async("modbusExecutor")
    @Override
    public CompletableFuture<Optional<Integer>> readProgramNumberDevice(int unitId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return modbusClientReader.readCurrentProgram(unitId);
            } catch (Exception e) {
                log.error("Ошибка при чтении номера программы: {}", e.getMessage());
                return Optional.empty();
            }
        });
    }


    @Async("modbusExecutor")
    @Override
    public CompletableFuture<Optional<Integer>> readCurrentPowerDevice(int unitId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return modbusClientReader.readCurrentPower(unitId);
            } catch (Exception e) {
                log.error("Ошибка при чтении номера программы: {}", e.getMessage());
                return Optional.empty();
            }
        });
    }

    @Override
    public Optional<Double> readStoredDotParameterFromMap(Map<String, Object> parameters, String parameterName) {
        Object rawValueObj = parameters.get(parameterName);
        Object dotObj = parameters.get(parameterName.replace("SP", "dot"));
        if (rawValueObj instanceof Optional && dotObj instanceof Optional) {
            Object rawValue = ((Optional<?>) rawValueObj).orElse(null);
            Object dot = ((Optional<?>) dotObj).orElse(null);
            if (rawValue instanceof Number && dot instanceof Number) {
                return Optional.of(((Number) rawValue).doubleValue() / Math.pow(10, ((Number) dot).intValue()));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> readInt16ParameterFromMap(Map<String, Object> parameters, String parameterName) {
        Object valueObj = parameters.get(parameterName);
        if (valueObj instanceof Optional) {
            Object value = ((Optional<?>) valueObj).orElse(null);
            if (value instanceof Number) {
                return Optional.of(((Number) value).intValue());
            }
        }
        return Optional.empty();
    }

    private Optional<Object> readParameterInternal(int unitId, TechProgramParameter param) {
        int address = param.getHashCode();
        String dataType = param.getDataType();

        try {
            switch (dataType) {
                case "int8":
                    return Optional.ofNullable(readInt8(address, unitId));
                case "int16":
                    return Optional.ofNullable(readInt16(address, unitId));
                case "STORED_DOT":
                    return Optional.ofNullable(readStoredDot(address, unitId));
                default:
                    log.warn("Неподдерживаемый тип данных для чтения: {}", dataType);
                    return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Ошибка чтения параметра '{}' по адресу 0x{}: {}", param.getName(), String.format("%04X", address), e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<Integer> readInt8(int address, int unitId) {
        Optional<byte[]> result = modbusClientReader.readRegisters(address, 1, unitId);
        return result.map(registers -> (int) (registers[1] & 0xFF));
    }

    private Optional<Integer> readInt16(int address, int unitId) {
        Optional<byte[]> result = modbusClientReader.readRegisters(address, 1, unitId);
        return result.map(registers -> (int) ByteBuffer.wrap(registers).getShort());
    }

    private Optional<Double> readStoredDot(int address, int unitId) {
        Optional<byte[]> result = modbusClientReader.readRegisters(address, 1, unitId);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        int rawValue = ByteBuffer.wrap(result.get()).getShort();
        Optional<Integer> dotOptional = readInt16(address + 1, unitId);
        return dotOptional.map(dot -> (double) rawValue / Math.pow(10, dot));
    }

}