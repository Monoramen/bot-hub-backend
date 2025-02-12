package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.KeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.KeyboardResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;

import java.util.List;

public interface KeyboardService {

    KeyboardEntity create(KeyboardRequestDTO keyboardRequestDTO);

    KeyboardEntity update(KeyboardRequestDTO keyboardRequestDTO);

    void delete(Long id);

    KeyboardResponseDTO findById(Long id);

    List<KeyboardResponseDTO> findAll();

}