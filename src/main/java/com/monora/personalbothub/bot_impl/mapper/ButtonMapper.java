package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.request.ButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.ButtonResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.ButtonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ButtonMapper {

    @Mapping(target = "keyboard", ignore = true)  // Игнорируем это поле
    ButtonEntity toEntity(ButtonRequestDTO buttonRequestDTO);

    @Mapping(target = "keyboardId", ignore = true)  // Игнорируем это поле
    ButtonResponseDTO toResponse(ButtonEntity buttonEntity);

    // Маппинг списка сущностей в список ответов
    List<ButtonResponseDTO> toResponseList(List<ButtonEntity> buttonEntities);
}