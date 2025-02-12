package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.request.InlineKeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineKeyboardResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InlineButtonMapper.class})
public interface InlineKeyboardMapper {

    // Маппинг запроса в сущность
    InlineKeyboardEntity toEntity(InlineKeyboardRequestDTO inlineKeyboardRequestDTO);

    // Маппинг сущности в ответ
    InlineKeyboardResponseDTO toResponse(InlineKeyboardEntity inlineKeyboardEntity);

    // Маппинг списка сущностей в список ответов
    List<InlineKeyboardResponseDTO> toResponseList(List<InlineKeyboardEntity> inlineKeyboardEntities);
}