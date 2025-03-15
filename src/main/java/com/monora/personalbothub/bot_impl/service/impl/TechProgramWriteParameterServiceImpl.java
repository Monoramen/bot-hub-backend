package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.response.FiringProgramResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.FiringStepResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_impl.service.TechProgramWriteParameterService;
import com.monora.personalbothub.bot_impl.util.modbus.ModbusClientWriter;
import com.monora.personalbothub.bot_impl.util.modbus.enums.TechProgramParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TechProgramWriteParameterServiceImpl implements TechProgramWriteParameterService {

    private final ModbusClientWriter modbusClientWriter;

    /**
     * Записывает полную программу обжига в Modbus-устройство.
     *
     * @param program DTO с данными программы.
     * @param unitId  ID устройства.
     * @return true в случае успеха, false если произошла ошибка.
     */

    @Override
    public void writeFiringProgram(FiringProgramResponseDTO program, int deviceProgramId, int unitId) {
        if (deviceProgramId < 1 || deviceProgramId > 3) {
            throw new ApiException(ApiErrorType.BAD_REQUEST,
                    "Неподдерживаемый номер программы устройства: " + deviceProgramId + ". Допустимы только 1, 2, 3.");
        }

        log.info("Начало записи программы '{}' (ID из БД: {}) в слот P{} устройства с unitId {}",
                program.name(), program.id(), deviceProgramId, unitId);
        log.info("Начало записи программы '{}' (ID из БД: {}) в слот P{} устройства с unitId {}",
                program.name(), program.id(), deviceProgramId, unitId);

        for (FiringStepResponseDTO step : program.steps()) {
            // Теперь метод writeStep будет выбрасывать исключение в случае ошибки
            writeStep(deviceProgramId, step, unitId);
        }

        log.info("Программа успешно записана в слот P{}.", deviceProgramId);
    }

    @Override
    public void startStopTechProgram(boolean start, int unitId) {
        if (start) {
            log.info("Запускаю в работу.");
        } else {
            log.info("Отключено");
        }
        boolean success = modbusClientWriter.startStopTechProgram(start, unitId);
        if (!success) {
            throw new ApiException(ApiErrorType.INTERNAL_SERVER_ERROR, "Не удалось выполнить операцию 'Пуск/Останов'.");
        }
    }

    @Override
    public void selectTechProgram(int programNumber, int unitId) {
        boolean success = modbusClientWriter.selectTechProgram(programNumber, unitId);
        if (!success) {
            throw new ApiException(ApiErrorType.INTERNAL_SERVER_ERROR, "Не удалось выбрать программу #" + programNumber);
        }
    }

    private void writeStep(int deviceProgramId, FiringStepResponseDTO step, int unitId) {
        int stepNumber = step.stepNumber();
        if (stepNumber < 1 || stepNumber > 5) {
            log.warn("Пропуск шага с некорректным номером: {}", stepNumber);
            return; // Просто пропускаем, если номер некорректный, не бросая исключение
        }

        // 1. Запись целевой температуры (SP)
        writeParameterByName(deviceProgramId, stepNumber, "SP", step.targetTemperatureC(), unitId);
        // 2. Запись времени роста (t.rS)
        writeParameterByName(deviceProgramId, stepNumber, "T_RS", step.rampTimeMinutes(), unitId);
        // 3. Запись времени выдержки (t.Stb)
        writeParameterByName(deviceProgramId, stepNumber, "T_STB", step.holdTimeMinutes(), unitId);
    }

    private void writeParameterByName(int programId, int stepNumber, String paramSuffix, Number value, int unitId) {
        try {
            String enumName = String.format("P%d_S%d_%s", programId, stepNumber, paramSuffix.replace(".", "_"));
            TechProgramParameter parameter = TechProgramParameter.valueOf(enumName);
            writeParameterInternal(parameter, value, unitId);
        } catch (IllegalArgumentException e) {
            throw new ApiException(
                    ApiErrorType.BAD_REQUEST,
                    "Не удалось найти параметр для P" + programId + ".S" + stepNumber + "_" + paramSuffix + ": " + e.getMessage()
            );
        }
    }

    private void writeParameterInternal(TechProgramParameter param, Number value, int unitId) {
        int address = param.getHashCode();
        String dataType = param.getDataType();

        log.debug("Запись '{}' по адресу 0x{}, значение: {}, тип: {}", param.getName(), String.format("%04X", address), value, dataType);

        boolean success;
        switch (dataType) {
            case "int8":
                success = modbusClientWriter.writeInt8(address, value.intValue(), unitId);
                break;
            case "int16":
                success = modbusClientWriter.writeInt16(address, value.intValue(), unitId);
                break;
            case "STORED_DOT":
                success = modbusClientWriter.writeStoredDot(address, value.doubleValue(), unitId);
                break;
            default:
                throw new ApiException(ApiErrorType.BAD_REQUEST, "Неподдерживаемый тип данных для записи: " + dataType);
        }
        if (!success) {
            throw new ApiException(ApiErrorType.INTERNAL_SERVER_ERROR, "Ошибка записи параметра '" + param.getName() + "' по адресу " + String.format("0x%04X", address) + ".");
        }
    }

}