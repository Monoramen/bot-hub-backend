package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.request.InlineButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineButtonResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineButtonEntity;
import com.monora.personalbothub.bot_impl.mapper.InlineButtonMapper;
import com.monora.personalbothub.bot_impl.service.InlineButtonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/attachments/inline-keyboard/inline-buttons")
@RequiredArgsConstructor
public class InlineButtonController {

    private final InlineButtonService inlineButtonService;
    private final InlineButtonMapper inlineButtonMapper;

    @PostMapping("/add")
    public ResponseEntity<InlineButtonResponseDTO> create(@RequestBody InlineButtonRequestDTO requestDTO) {
//        inlineButtonService.create(requestDTO);
//        InlineButtonEntity entity = inlineButtonMapper.toEntity(requestDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(inlineButtonMapper.toResponse(entity));
    return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<InlineButtonResponseDTO> update(@PathVariable Long id, @RequestBody InlineButtonRequestDTO requestDTO) {
        inlineButtonService.update(requestDTO);
        InlineButtonResponseDTO responseDTO = inlineButtonService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inlineButtonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InlineButtonResponseDTO> findById(@PathVariable Long id) {
        InlineButtonResponseDTO responseDTO = inlineButtonService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping()
    public ResponseEntity<List<InlineButtonResponseDTO>> findAll() {
        List<InlineButtonResponseDTO> responseDTOs = inlineButtonService.findAll();
        return ResponseEntity.ok(responseDTOs);
    }
}
