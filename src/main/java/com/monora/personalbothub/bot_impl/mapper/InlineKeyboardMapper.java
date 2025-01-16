package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_api.dto.InlineKeyboardDto;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InlineKeyboardMapper {

    InlineKeyboardEntity toEntity(InlineKeyboardDto inlineKeyboardDto);

    InlineKeyboardDto toDto(InlineKeyboardEntity inlineKeyboardEntity);

    List<InlineKeyboardDto> toDtoList(List<InlineKeyboardEntity> inlineKeyboardEntities);
}
