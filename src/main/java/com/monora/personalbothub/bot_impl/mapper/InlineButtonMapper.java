package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.request.InlineButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineButtonResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineButtonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InlineButtonMapper {

    // Маппинг запроса в сущность
    @Mapping(target = "inlineKeyboard", ignore = true) // Игнорируем это поле
    InlineButtonEntity toEntity(InlineButtonRequestDTO inlineButtonRequestDTO);

    // Маппинг сущности в ответ
    @Mapping(target = "inlineKeyboardId", ignore = true) // Игнорируем это поле
    InlineButtonResponseDTO toResponse(InlineButtonEntity inlineButtonEntity);

    // Маппинг списка сущностей в список ответов
    List<InlineButtonResponseDTO> toResponseList(List<InlineButtonEntity> inlineButtonEntities);
}