package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.request.ButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.ButtonResponseDTO;
import com.monora.personalbothub.bot_impl.mapper.ButtonMapper;
import com.monora.personalbothub.bot_impl.service.ButtonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/attachments/keyboard/buttons")
@RequiredArgsConstructor
public class ButtonController {

    private final ButtonService buttonService;
    private final ButtonMapper buttonMapper;

    @PostMapping("/add")
    public ResponseEntity<ButtonResponseDTO> create(@RequestBody ButtonRequestDTO requestDTO) {
//        buttonService.create(requestDTO);
//        ButtonEntity entity = buttonMapper.toEntity(requestDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(buttonMapper.toResponse(entity));
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ButtonResponseDTO> update(@PathVariable Long id, @RequestBody ButtonRequestDTO requestDTO) {
        buttonService.update(requestDTO);
        ButtonResponseDTO responseDTO = buttonService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        buttonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ButtonResponseDTO> findById(@PathVariable Long id) {
        ButtonResponseDTO responseDTO = buttonService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping()
    public ResponseEntity<List<ButtonResponseDTO>> findAll() {
        List<ButtonResponseDTO> responseDTOs = buttonService.findAll();
        return ResponseEntity.ok(responseDTOs);
    }
}
