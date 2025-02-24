package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.request.AttachmentRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.AttachmentResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineKeyboardAttachmentResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.KeyboardAttachmentResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;
import com.monora.personalbothub.bot_db.entity.attachment.InlineKeyboardAttachmentEntity;
import com.monora.personalbothub.bot_db.entity.attachment.KeyboardAttachmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring",
        uses = {KeyboardMapper.class, InlineKeyboardMapper.class})
public interface AttachmentMapper {

    @SubclassMapping(source = InlineKeyboardAttachmentEntity.class, target = InlineKeyboardAttachmentResponseDTO.class)
    @SubclassMapping(source = KeyboardAttachmentEntity.class, target = KeyboardAttachmentResponseDTO.class)
    AttachmentResponseDTO toResponseDTO(AttachmentEntity entity);

    @Mapping(target = "commandId", source = "command.id")
    @Mapping(target = "inlineKeyboard", source = "inlineKeyboard")
    InlineKeyboardAttachmentResponseDTO toInlineDto(InlineKeyboardAttachmentEntity entity);

    @Mapping(target = "commandId", source = "command.id")
    @Mapping(target = "keyboard", source = "keyboard")
    KeyboardAttachmentResponseDTO toKeyboardDto(KeyboardAttachmentEntity entity);
}
