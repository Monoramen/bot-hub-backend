package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommandMapper {

    CommandEntity toEntity(CommandDto commandDto);

    CommandDto toDto(CommandEntity commandEntity);

    List<CommandDto> toDtoList(List<CommandEntity> commandEntities);
}
