package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.AttachmentRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.AttachmentResponseDTO;
import com.monora.personalbothub.bot_db.repository.AttachmentRepository;
import com.monora.personalbothub.bot_db.repository.CommandRepository;
import com.monora.personalbothub.bot_impl.mapper.AttachmentMapper;
import com.monora.personalbothub.bot_impl.service.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional

public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final CommandRepository commandRepository;
    private final AttachmentMapper attachmentMapper;

    @Override
    public AttachmentResponseDTO createAttachment(AttachmentRequestDTO dto) {
        return null;
    }

    @Override
    public AttachmentResponseDTO getAttachment(Long id) {
        return null;
    }

    @Override
    public List<AttachmentResponseDTO> getAttachmentsByCommandId(Long commandId) {
        return List.of();
    }

    @Override
    public AttachmentResponseDTO updateAttachment(Long id, AttachmentRequestDTO dto) {
        return null;
    }

    @Override
    public void deleteAttachment(Long id) {

    }

}