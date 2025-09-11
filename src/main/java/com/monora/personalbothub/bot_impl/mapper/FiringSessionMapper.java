package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.response.FiringSessionResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.FiringSessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TemperatureMapper.class})
public interface FiringSessionMapper {

    FiringSessionMapper INSTANCE = Mappers.getMapper(FiringSessionMapper.class);

    @Mapping(target = "status", source = "status")
    FiringSessionResponseDTO toDto(FiringSessionEntity entity);

     default FiringSessionResponseDTO toListItemDto(FiringSessionEntity entity) {
         return toDto(entity);
     }

}