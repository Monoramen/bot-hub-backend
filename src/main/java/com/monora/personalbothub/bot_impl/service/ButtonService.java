package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.ButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.ButtonResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.ButtonEntity;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import com.pengrad.telegrambot.model.request.KeyboardButton;

import java.util.List;

public interface ButtonService {

    ButtonEntity create(ButtonRequestDTO buttonRequestDTO, KeyboardEntity keyboard);

    ButtonEntity update(ButtonRequestDTO buttonRequestDTO);

    void delete(Long id);

    ButtonResponseDTO findById(Long id);

    List<ButtonResponseDTO> findAll();

    KeyboardButton[][] getButtonRowByKeyboardId(Long keyboardId);
}