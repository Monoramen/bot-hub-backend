package com.monora.personalbothub.bot_api.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FiringStepRequestDTO(

        @NotNull
        @Min(1)
        @Max(5)
        Integer stepNumber,

        @NotNull
        @DecimalMax(value = "1200.0", inclusive = true, message = "Temperature must be ≤ 1200°C")
        Double targetTemperatureC,

        @NotNull
        @Min(0)
        @Max(999)
        Integer rampTimeMinutes,

        @NotNull
        @Min(0)
        @Max(999)
        Integer holdTimeMinutes

) {}
