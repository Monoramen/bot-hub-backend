package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TimeLeftResponseDTO(
        @JsonFormat(locale = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime leftTime
) {
}