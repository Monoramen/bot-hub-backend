package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.KeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.KeyboardResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import com.monora.personalbothub.bot_impl.service.KeyboardService;

import java.util.List;

public class KeyboardServiceImpl implements KeyboardService {

    @Override
    public KeyboardEntity create(KeyboardRequestDTO keyboardRequestDTO) {
        return null;
    }

    @Override
    public KeyboardEntity update(KeyboardRequestDTO keyboardRequestDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public KeyboardResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public List<KeyboardResponseDTO> findAll() {
        return List.of();
    }
}
