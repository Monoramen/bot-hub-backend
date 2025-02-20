package com.monora.personalbothub.bot_impl.mapper;

import com.monora.personalbothub.bot_api.dto.request.AttachmentRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.AttachmentResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;
import com.monora.personalbothub.bot_db.entity.attachment.InlineKeyboardAttachmentEntity;
import com.monora.personalbothub.bot_db.entity.attachment.KeyboardAttachmentEntity;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {KeyboardMapper.class, InlineKeyboardMapper.class})
public interface AttachmentMapper {

    @Mapping(target = "command.id", source = "commandId")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "keyboard.id", source = "keyboardId")
    KeyboardAttachmentEntity toKeyboardEntity(AttachmentRequestDTO dto);

    @Mapping(target = "command.id", source = "commandId")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "inlineKeyboard.id", source = "inlineKeyboardId")
    InlineKeyboardAttachmentEntity toInlineKeyboardEntity(AttachmentRequestDTO dto);

    // Убедитесь, что свойства соответствуют AttachmentEntity
    AttachmentResponseDTO toResponseDTO(AttachmentEntity entity);

//    default KeyboardEntity map(Long keyboardId) {
//        if (keyboardId == null) {
//            return null;
//        }
//        return KeyboardEntity.builder().id(keyboardId).build();
//    }
//
//    default InlineKeyboardEntity mapInline(Long inlineKeyboardId) {
//        if (inlineKeyboardId == null) {
//            return null;
//        }
//        return InlineKeyboardEntity.builder().id(inlineKeyboardId).build();
//    }
}
