package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.request.InlineKeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.request.InlineKeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineKeyboardResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineKeyboardResponseDTO;
import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

public interface InlineKeyboardService {

    void create(InlineKeyboardRequestDTO inlineKeyboardRequestDTO);

    void update(InlineKeyboardRequestDTO inlineKeyboardRequestDTO);

    void delete(Long id);

    InlineKeyboardResponseDTO findById(Long id);

    List<InlineKeyboardResponseDTO> findAll();

    InlineKeyboardMarkup getInlineKeyboardByCommandId(Long commandId);

}