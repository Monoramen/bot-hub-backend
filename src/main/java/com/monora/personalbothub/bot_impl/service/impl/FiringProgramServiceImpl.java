package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.FiringProgramRequestDTO;
import com.monora.personalbothub.bot_api.dto.request.FiringStepRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.FiringProgramResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.FiringStepResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.modbus.FiringProgramEntity;
import com.monora.personalbothub.bot_db.repository.FiringProgramRepository;
import com.monora.personalbothub.bot_impl.mapper.FiringProgramMapper;
import com.monora.personalbothub.bot_impl.service.FiringProgramService;
import com.monora.personalbothub.bot_impl.service.TechProgramReadParameterService;
import com.monora.personalbothub.bot_impl.util.modbus.enums.TechProgramParameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class FiringProgramServiceImpl implements FiringProgramService {

    private final FiringProgramRepository firingProgramRepository;
    private final FiringProgramMapper firingProgramMapper;
    private final TechProgramReadParameterService techProgramReadParameterService;

    @Override
    public FiringProgramResponseDTO create(FiringProgramRequestDTO requestDTO) {
        validateSteps(requestDTO.steps());

        FiringProgramEntity entity = firingProgramMapper.toEntity(requestDTO);
        FiringProgramEntity saved = firingProgramRepository.save(entity);
        return firingProgramMapper.toResponseDTO(saved);
    }

    @Override
    public FiringProgramResponseDTO update(Integer id, FiringProgramRequestDTO requestDTO) {
        validateSteps(requestDTO.steps());

        FiringProgramEntity existing = firingProgramRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Firing program not found"));

        existing.setName(requestDTO.name());
        existing.setSteps(firingProgramMapper.mapSteps(requestDTO.steps()));
        FiringProgramEntity updated = firingProgramRepository.save(existing);
        return firingProgramMapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Integer id) {
        FiringProgramEntity entity = firingProgramRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Firing program not found"));
        firingProgramRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public FiringProgramResponseDTO findById(Integer id) {
        FiringProgramEntity entity = firingProgramRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Firing program not found"));
        return firingProgramMapper.toResponseDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FiringProgramResponseDTO> findAll() {
        List<FiringProgramEntity> all = firingProgramRepository.findAll();
        if (all.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "No firing programs found");
        }
        return firingProgramMapper.toResponseDTOList(all);
    }

    private void validateSteps(List<FiringStepRequestDTO> steps) {
        if (steps.size() > 5) {
            throw new ApiException(ApiErrorType.BAD_REQUEST, "Too many steps. Max is 5.");
        }

        for (FiringStepRequestDTO step : steps) {
            if (step.targetTemperatureC() > 1200) {
                throw new ApiException(ApiErrorType.BAD_REQUEST, "Temperature must not exceed 1200°C");
            }

            if (step.rampTimeMinutes() > 999 || step.holdTimeMinutes() > 999) {
                throw new ApiException(ApiErrorType.BAD_REQUEST, "Time values must not exceed 999 minutes");
            }
        }
    }

    /**
     * Чтение всех параметров технологической программы с устройства и выгрузка в DTO,
     * используя FiringProgramResponseDTO и FiringStepResponseDTO.
     *
     * @param unitId Modbus Unit ID устройства
     * @return Optional, содержащий FiringProgramResponseDTO с параметрами для всех программ, если успешно.
     */

    @Override
    public List<FiringProgramResponseDTO> getAllTechProgramParametersAsFiringProgramDTO(int unitId) {
        // Вызываем асинхронный метод
        CompletableFuture<Map<String, Object>> future = techProgramReadParameterService.loadTechProgramParameters(unitId);

        try {
            // Ждём завершения и получаем результат
            Map<String, Object> parameters = future.get(); // .get() блокирует поток до получения результата

            Optional<?> timeScaleRaw = (Optional<?>) parameters.get(TechProgramParameter.T_SCL.getName());
            if (timeScaleRaw == null || timeScaleRaw.isEmpty() || !(timeScaleRaw.get() instanceof Integer)) {
                log.warn("Не удалось прочитать параметр масштаба времени.");
                return null;
            }

            List<FiringProgramResponseDTO> programResponses = new ArrayList<>();

            for (int programNumber = 1; programNumber <= 3; programNumber++) {
                FiringProgramResponseDTO program = loadSingleProgram(parameters, programNumber);
                if (program != null) {
                    programResponses.add(program);
                }
            }
            return programResponses.isEmpty() ? null : programResponses;

        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при получении результата из CompletableFuture: {}", e.getMessage());
            // Обработка ошибок, если асинхронная задача не выполнилась
            return null;
        } catch (Exception e) {
            log.error("Ошибка при преобразовании параметров в FiringProgramResponseDTO: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Загружает одну программу по номеру.
     */
    @Override
    public FiringProgramResponseDTO getOneTechProgramParametersAsFiringProgramDTO(int programNumber, int unitId) {

        if (programNumber < 1 || programNumber > 3) {
            throw new ApiException(ApiErrorType.BAD_REQUEST, "Номер программы может быть только 1, 2, 3.");
        }

        CompletableFuture<Map<String, Object>> future = techProgramReadParameterService.loadProgramParameters(programNumber, unitId);

        try {
            Map<String, Object> parameters = future.get();

            Optional<?> timeScaleRaw = (Optional<?>) parameters.get(TechProgramParameter.T_SCL.getName());
            if (timeScaleRaw == null || timeScaleRaw.isEmpty() || !(timeScaleRaw.get() instanceof Integer)) {
                log.warn("Не удалось прочитать параметр масштаба времени.");
                return null;
            }

            FiringProgramResponseDTO programResponse = loadSingleProgram(parameters, programNumber);

            return programResponse;

        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при получении результата из CompletableFuture: {}", e.getMessage());
            // Обработка ошибок, если асинхронная задача не выполнилась
            return null;
        } catch (Exception e) {
            log.error("Ошибка при преобразовании параметров в FiringProgramResponseDTO: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Вспомогательный метод: загружает одну программу из уже загруженного Map параметров.
     */
    private FiringProgramResponseDTO loadSingleProgram(Map<String, Object> parameters, int programNumber) {
        List<FiringStepResponseDTO> steps = new ArrayList<>();

        for (int stepNumber = 1; stepNumber <= 5; stepNumber++) {
            String prefix = "P" + programNumber + ".S" + stepNumber + ".";

            Optional<Double> targetTemperatureCOptional = techProgramReadParameterService
                    .readStoredDotParameterFromMap(parameters, prefix + "SP");
            Optional<Integer> rampTimeMinutesOptional = techProgramReadParameterService
                    .readInt16ParameterFromMap(parameters, prefix + "t.rS");
            Optional<Integer> holdTimeMinutesOptional = techProgramReadParameterService
                    .readInt16ParameterFromMap(parameters, prefix + "t.Stb");

            if (targetTemperatureCOptional.isPresent() && rampTimeMinutesOptional.isPresent() && holdTimeMinutesOptional.isPresent()) {
                steps.add(new FiringStepResponseDTO(
                        stepNumber,
                        targetTemperatureCOptional.get(),
                        rampTimeMinutesOptional.get(),
                        holdTimeMinutesOptional.get()
                ));
            } else {
                log.warn("Не удалось прочитать все параметры для шага {} программы {}.", stepNumber, programNumber);
            }
        }

        if (steps.isEmpty()) {
            log.debug("Программа {} не содержит валидных шагов.", programNumber);
            return null;
        }

        return new FiringProgramResponseDTO(
                programNumber,
                "Программа " + programNumber,
                steps
        );
    }


}

