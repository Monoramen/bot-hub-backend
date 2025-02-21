package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.KeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.KeyboardResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;

import java.util.List;

public interface KeyboardService {

    KeyboardEntity create(KeyboardRequestDTO keyboardRequestDTO);

    KeyboardEntity update(Long id, KeyboardRequestDTO keyboardRequestDTO);

    void delete(Long id);

    KeyboardResponseDTO findById(Long id);

    List<KeyboardResponseDTO> findAll();

    KeyboardResult getKeyboardByCommandId(Long commandId);

    KeyboardEntity getById(Long id);
}