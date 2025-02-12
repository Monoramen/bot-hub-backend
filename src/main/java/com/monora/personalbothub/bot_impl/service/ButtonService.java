package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.ButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.ButtonResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.ButtonEntity;

import java.util.List;

public interface ButtonService {

    ButtonEntity create(ButtonRequestDTO buttonRequestDTO);

    ButtonEntity update(ButtonRequestDTO buttonRequestDTO);

    void delete(Long id);

    ButtonResponseDTO findById(Long id);

    List<ButtonResponseDTO> findAll();

}