package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.request.KeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.KeyboardResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ButtonMapper.class})
public interface KeyboardMapper {

    // Маппинг запроса в сущность
    @Mapping(target = "id", ignore = true)
    KeyboardEntity toEntity(KeyboardRequestDTO keyboardRequestDTO);

    // Маппинг сущности в ответ
    KeyboardResponseDTO toResponse(KeyboardEntity keyboardEntity);

    // Маппинг списка сущностей в список ответов
    List<KeyboardResponseDTO> toResponseList(List<KeyboardEntity> keyboardEntities);
}
