package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.request.AttachmentRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.AttachmentResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;
import com.monora.personalbothub.bot_impl.mapper.AttachmentMapper;
import com.monora.personalbothub.bot_impl.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final AttachmentMapper attachmentMapper;

    @PostMapping("/add")
    public ResponseEntity<AttachmentResponseDTO> createAttachment(@RequestBody AttachmentRequestDTO requestDTO) {
        AttachmentEntity attachmentEntity = attachmentService.create(requestDTO);
        AttachmentResponseDTO responseDTO = attachmentMapper.toResponseDTO(attachmentEntity);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AttachmentResponseDTO>> getAllCommands() {
        List<AttachmentResponseDTO> attachments = attachmentService.findAll();
        return ResponseEntity.ok(attachments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttachmentResponseDTO> updateAttachment(@PathVariable Long id, @RequestBody AttachmentRequestDTO requestDTO) {
        AttachmentResponseDTO responseDTO = attachmentService.update(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttachmentResponseDTO> getAttachmentById(@PathVariable Long id) {
        AttachmentResponseDTO responseDTO = attachmentService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/command/{commandId}")
    public ResponseEntity<AttachmentResponseDTO> getAttachmentByCommandId(@PathVariable Long commandId) {
        AttachmentResponseDTO responseDTO = attachmentService.getAttachmentByCommandId(commandId);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        attachmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}