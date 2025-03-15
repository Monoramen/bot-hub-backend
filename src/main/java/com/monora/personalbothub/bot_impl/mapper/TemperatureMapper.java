package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.response.TemperatureResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TemperatureMapper {

    TemperatureEntity toEntity(TemperatureResponseDTO temperatureResponseDTO);


    TemperatureResponseDTO toResponseDTO(TemperatureEntity temperatureEntity);

    List<TemperatureResponseDTO> toResponseDTOList(List<TemperatureEntity> entities);

}
