package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.AttachmentRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.AttachmentResponseDTO;

import java.util.List;


public interface AttachmentService {


    AttachmentResponseDTO createAttachment(AttachmentRequestDTO dto);

    AttachmentResponseDTO getAttachment(Long id);

    List<AttachmentResponseDTO> getAttachmentsByCommandId(Long commandId);

    public AttachmentResponseDTO updateAttachment(Long id, AttachmentRequestDTO dto);

    void deleteAttachment(Long id);
}