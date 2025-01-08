package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_api.dto.InlineButtonDto;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.entity.InlineButtonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InlineButtonMapper {

    InlineButtonEntity toEntity(InlineButtonDto inlineButtonDto);

    InlineButtonDto toDto(InlineButtonEntity inlineButtonEntity);
}
