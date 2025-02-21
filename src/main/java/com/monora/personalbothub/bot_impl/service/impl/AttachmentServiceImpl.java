package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.AttachmentRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.AttachmentResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;
import com.monora.personalbothub.bot_db.entity.attachment.InlineKeyboardAttachmentEntity;
import com.monora.personalbothub.bot_db.entity.attachment.KeyboardAttachmentEntity;
import com.monora.personalbothub.bot_db.repository.AttachmentRepository;
import com.monora.personalbothub.bot_db.repository.CommandRepository;
import com.monora.personalbothub.bot_impl.mapper.AttachmentMapper;
import com.monora.personalbothub.bot_impl.service.AttachmentService;
import com.monora.personalbothub.bot_impl.service.InlineKeyboardService;
import com.monora.personalbothub.bot_impl.service.KeyboardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final CommandRepository commandRepository;
    private final AttachmentMapper attachmentMapper;
    private final KeyboardService keyboardService;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public AttachmentEntity create(AttachmentRequestDTO requestDTO) {
        CommandEntity command = commandRepository.findById(requestDTO.commandId())
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Command not found"));

        AttachmentEntity attachment;
        switch (requestDTO.type()) {
            case KEYBOARD -> {
                KeyboardAttachmentEntity keyboardAttachment = new KeyboardAttachmentEntity();
                keyboardAttachment.setType("KEYBOARD");
                keyboardAttachment.setCommand(command);
                keyboardAttachment.setKeyboard(keyboardService.getById(requestDTO.keyboardId()));
                attachment = keyboardAttachment;
            }
            case INLINE_KEYBOARD -> {
                InlineKeyboardAttachmentEntity inlineKeyboardAttachment = new InlineKeyboardAttachmentEntity();
                inlineKeyboardAttachment.setType("INLINE_KEYBOARD");
                inlineKeyboardAttachment.setCommand(command);
                inlineKeyboardAttachment.setInlineKeyboard(inlineKeyboardService.getById(requestDTO.inlineKeyboardId()));
                attachment = inlineKeyboardAttachment;
            }
            default -> throw new ApiException(ApiErrorType.BAD_REQUEST, "Неподдерживаемый тип прикрепления");
        }

        return attachmentRepository.save(attachment);
    }

    @Override
    public AttachmentResponseDTO update(AttachmentRequestDTO dto) {
        AttachmentEntity existingAttachment = attachmentRepository.findById(dto.id()).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Attachment not found")
        );
        existingAttachment.setType(existingAttachment.getType());
        existingAttachment.setCommand(commandRepository.findById(dto.id()).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Attachment not found")
        ));


        return null;
    }

    @Override
    public AttachmentResponseDTO getById(Long id) {
        AttachmentEntity attachmentEntity = attachmentRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Attachment not found"));

        return attachmentMapper.toResponseDTO(attachmentEntity);
    }

    @Override
    public AttachmentResponseDTO getAttachmentByCommandId(Long commandId) {
        AttachmentEntity attachmentEntity = attachmentRepository.findByCommandId(commandId).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Attachment not found"));

        return attachmentMapper.toResponseDTO(attachmentEntity);
    }


    @Override
    public void delete(Long id) {
        AttachmentEntity attachmentEntity = attachmentRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Attachment not found"));
        attachmentRepository.delete(attachmentEntity);
    }

}