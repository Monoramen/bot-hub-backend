package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_api.dto.InlineButtonDto;
import com.monora.personalbothub.bot_api.dto.request.InlineButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineButtonResponseDTO;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.entity.InlineButtonEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InlineButtonMapper {

    // Маппинг запроса в сущность
    InlineButtonEntity toEntity(InlineButtonRequestDTO inlineButtonRequestDTO);

    // Маппинг сущности в ответ
    InlineButtonResponseDTO toResponse(InlineButtonEntity inlineButtonEntity);

    // Маппинг списка сущностей в список ответов
    List<InlineButtonResponseDTO> toResponseList(List<InlineButtonEntity> inlineButtonEntities);
}