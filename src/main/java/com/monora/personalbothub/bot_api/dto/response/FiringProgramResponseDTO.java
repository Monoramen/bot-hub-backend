package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FiringProgramResponseDTO(
        int id,
        String name,
        List<FiringStepResponseDTO> steps
) {
}