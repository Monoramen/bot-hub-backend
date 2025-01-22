package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.request.CommandRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.CommandResponseDTO;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InlineKeyboardMapper.class})
public interface CommandMapper {

    // Маппинг запроса в сущность
    CommandEntity toEntity(CommandRequestDTO commandRequestDTO);

    // Маппинг сущности в ответ
    CommandResponseDTO toResponse(CommandEntity commandEntity);

    // Маппинг списка сущностей в список ответов
    List<CommandResponseDTO> toResponseList(List<CommandEntity> commandEntities);
}
