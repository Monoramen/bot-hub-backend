package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.response.FiringSessionResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.SessionDataResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.TemperatureResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.FiringSessionEntity;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TemperatureMapper.class, FiringProgramMapper.class})
public interface FiringSessionMapper {

    FiringSessionMapper INSTANCE = Mappers.getMapper(FiringSessionMapper.class);

    @Mapping(target = "status", source = "status")
    FiringSessionResponseDTO toDto(FiringSessionEntity entity);

    default FiringSessionResponseDTO toListItemDto(FiringSessionEntity entity) {
        return toDto(entity);
    }

    // ✅ Пусть MapStruct сгенерирует ВЕСЬ маппинг, включая program и temperatureReadings
    @Mapping(target = "maxRecordedTemperature", ignore = true) // ← мы пересчитаем его вручную позже
    @Mapping(target = "latestTemperature", ignore = true)
    // ← тоже обработаем отдельно
    SessionDataResponseDTO toSessionDataDto(FiringSessionEntity session, List<TemperatureEntity> allTemperatures);



    default Double calculateMaxTemperature(List<TemperatureEntity> temperatures) {
        return temperatures.stream()
                .filter(this::isValidTemperature)
                .mapToDouble(TemperatureEntity::getTemperature)
                .max()
                .orElse(0.0);
    }

    default TemperatureResponseDTO findLatestValidTemperature(List<TemperatureEntity> temperatures) {
        return temperatures.stream()
                .filter(this::isValidTemperature)
                .max((t1, t2) -> t1.getTimestamp().compareTo(t2.getTimestamp()))
                .map(this::toTemperatureResponseDTO)
                .orElse(null);
    }

    default TemperatureResponseDTO toTemperatureResponseDTO(TemperatureEntity temp) {
        if (temp == null) return null;
        return new TemperatureResponseDTO(
                temp.getTimestamp(),
                temp.getTemperature(),
                temp.getSession() != null ? temp.getSession().getId() : null
        );
    }

    default boolean isValidTemperature(TemperatureEntity temp) {
        if (temp == null) return false;
        float t = temp.getTemperature();
        return !Float.isNaN(t) && !Float.isInfinite(t) && Math.abs(t) <= 10000 && t >= -50 && t <= 1400;
    }
}