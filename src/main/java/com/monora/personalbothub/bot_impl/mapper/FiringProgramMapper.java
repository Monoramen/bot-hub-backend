package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.request.FiringProgramRequestDTO;
import com.monora.personalbothub.bot_api.dto.request.FiringStepRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.FiringProgramResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.FiringStepResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.FiringProgramEntity;
import com.monora.personalbothub.bot_db.entity.modbus.FiringProgramHistoryEntity;
import com.monora.personalbothub.bot_db.entity.modbus.FiringStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FiringProgramMapper {

    // --------------------- TO ENTITY ---------------------

    @Mapping(target = "id", ignore = true)
    FiringProgramEntity toEntity(FiringProgramRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    FiringProgramHistoryEntity toEntityHistory(FiringProgramResponseDTO dto);

    // --------------------- TO DTO -----------------------

    FiringProgramResponseDTO toResponseDTO(FiringProgramEntity entity);
    FiringProgramResponseDTO toResponseHistoryDTO(FiringProgramHistoryEntity entity);

    List<FiringProgramResponseDTO> toResponseDTOList(List<FiringProgramEntity> entities);
    List<FiringProgramResponseDTO> toResponseHistoryDTOList(List<FiringProgramHistoryEntity> entities);

    // --------------------- DEFAULT METHODS ---------------

    default List<FiringStep> mapSteps(List<FiringStepRequestDTO> steps) {
        return steps.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    default List<FiringStepResponseDTO> mapStepEntities(List<FiringStep> steps) {
        return steps.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    default FiringStep toEntity(FiringStepRequestDTO dto) {
        return new FiringStep(
                dto.stepNumber(),
                dto.targetTemperatureC(),
                dto.rampTimeMinutes(),
                dto.holdTimeMinutes()
        );
    }

    default FiringStepResponseDTO toDto(FiringStep step) {
        return new FiringStepResponseDTO(
                step.getStepNumber(),
                step.getTargetTemperatureC(),
                step.getRampTimeMinutes(),
                step.getHoldTimeMinutes()
        );
    }
}
