// src/main/java/com/monora/personalbothub/bot_api/dto/response/SessionDataResponseDTO.java

package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SessionDataResponseDTO(
        Long id,
        FiringProgramResponseDTO program,
        LocalDateTime startTime,
        LocalDateTime endTime,
        FiringStatus status,
        Integer actualDurationMinutes,
        Double maxRecordedTemperature,
        List<TemperatureResponseDTO> temperatureReadings,
        TemperatureResponseDTO latestTemperature
) {
}