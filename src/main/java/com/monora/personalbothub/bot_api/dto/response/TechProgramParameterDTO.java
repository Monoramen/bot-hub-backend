package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TechProgramParameterDTO(
        String name,
        String description,
        String dataType,
        String range,
        Object value
) {
}