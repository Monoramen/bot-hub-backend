package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.ButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.ButtonResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.ButtonEntity;
import com.monora.personalbothub.bot_impl.service.ButtonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@AllArgsConstructor
public class ButtonServiceImpl implements ButtonService {

    @Override
    public ButtonEntity create(ButtonRequestDTO buttonRequestDTO) {
        return null;
    }

    @Override
    public ButtonEntity update(ButtonRequestDTO buttonRequestDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ButtonResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public List<ButtonResponseDTO> findAll() {
        return List.of();
    }
}
