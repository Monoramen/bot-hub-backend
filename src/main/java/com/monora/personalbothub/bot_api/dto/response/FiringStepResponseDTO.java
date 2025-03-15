package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FiringStepResponseDTO(
        Integer stepNumber,
        Double targetTemperatureC,
        Integer rampTimeMinutes,
        Integer holdTimeMinutes
) {}
