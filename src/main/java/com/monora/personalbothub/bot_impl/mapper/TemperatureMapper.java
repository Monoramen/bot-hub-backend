package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.response.TemperatureResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TemperatureMapper {

    TemperatureEntity toEntity(TemperatureResponseDTO temperatureResponseDTO);

    @Mapping(source = "session.id", target = "sessionId")
    TemperatureResponseDTO toResponseDTO(TemperatureEntity temperatureEntity);

    List<TemperatureResponseDTO> toResponseDTOList(List<TemperatureEntity> entities);

}
