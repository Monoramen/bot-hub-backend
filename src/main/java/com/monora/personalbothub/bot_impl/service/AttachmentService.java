package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.AttachmentRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.AttachmentResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;

import java.util.List;


public interface AttachmentService {

    AttachmentEntity create(AttachmentRequestDTO dto);

    AttachmentResponseDTO getById(Long id);

    AttachmentResponseDTO getAttachmentByCommandId(Long commandId);

    AttachmentResponseDTO update(Long id, AttachmentRequestDTO dto);

    List<AttachmentResponseDTO> findAll();

    void delete(Long id);
}