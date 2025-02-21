package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.request.KeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.KeyboardResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import com.monora.personalbothub.bot_impl.mapper.KeyboardMapper;
import com.monora.personalbothub.bot_impl.service.KeyboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/attachment/keyboards")
@RequiredArgsConstructor
public class KeyboardController {

    private final KeyboardService keyboardService;
    private final KeyboardMapper keyboardMapper;

    @PostMapping("/add")
    public ResponseEntity<KeyboardResponseDTO> createInlineKeyboard(@RequestBody KeyboardRequestDTO requestDTO) {
        KeyboardEntity createdEntity = keyboardService.create(requestDTO);
        KeyboardResponseDTO responseDTO = keyboardMapper.toResponse(createdEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KeyboardResponseDTO> update(@PathVariable Long id, @RequestBody KeyboardRequestDTO requestDTO) {
        keyboardService.update(id, requestDTO);
        KeyboardResponseDTO responseDTO = keyboardService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        keyboardService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Keyboard with id " + id + " was deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeyboardResponseDTO> findById(@PathVariable Long id) {
        KeyboardResponseDTO responseDTO = keyboardService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<KeyboardResponseDTO>> findAll() {
        List<KeyboardResponseDTO> responseDTOs = keyboardService.findAll();
        return ResponseEntity.ok(responseDTOs);
    }
}
