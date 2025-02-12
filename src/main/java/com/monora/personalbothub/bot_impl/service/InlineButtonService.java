package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.InlineButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineButtonResponseDTO;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineButtonEntity;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;

import java.util.List;

public interface InlineButtonService {

    InlineButtonEntity create(InlineButtonRequestDTO inlineButtonRequestDTO);

    InlineButtonEntity update(InlineButtonRequestDTO inlineButtonRequestDTO);

    void delete(Long id);

    InlineButtonResponseDTO findById(Long id);

    List<InlineButtonResponseDTO> findAll();

    InlineKeyboardButton[][] getInlineButtonRowByKeyboardId(Long inlineKeyboardId);



}