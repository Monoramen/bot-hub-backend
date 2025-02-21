package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.request.InlineKeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineKeyboardResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import com.monora.personalbothub.bot_impl.mapper.InlineKeyboardMapper;
import com.monora.personalbothub.bot_impl.service.InlineKeyboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/attachments/inline-keyboards")
@RequiredArgsConstructor
public class InlineKeyboardController {

    private final InlineKeyboardService inlineKeyboardService;
    private final InlineKeyboardMapper inlineKeyboardMapper;

    @PostMapping("/add")
    public ResponseEntity<InlineKeyboardResponseDTO> createInlineKeyboard(@RequestBody InlineKeyboardRequestDTO requestDTO) {
        InlineKeyboardEntity createdEntity = inlineKeyboardService.create(requestDTO);
        InlineKeyboardResponseDTO responseDTO = inlineKeyboardMapper.toResponse(createdEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InlineKeyboardResponseDTO> update(@PathVariable Long id, @RequestBody InlineKeyboardRequestDTO requestDTO) {
        inlineKeyboardService.update(id, requestDTO);
        InlineKeyboardResponseDTO responseDTO = inlineKeyboardService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inlineKeyboardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InlineKeyboardResponseDTO> findById(@PathVariable Long id) {
        InlineKeyboardResponseDTO responseDTO = inlineKeyboardService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<InlineKeyboardResponseDTO>> findAll() {
        List<InlineKeyboardResponseDTO> responseDTOs = inlineKeyboardService.findAll();
        return ResponseEntity.ok(responseDTOs);
    }
}
