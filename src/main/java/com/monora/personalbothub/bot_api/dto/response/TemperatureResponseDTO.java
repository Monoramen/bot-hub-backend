package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TemperatureResponseDTO(
        @JsonFormat(locale = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp, // дата и время
        float temperature        // значение температуры
) {
}

