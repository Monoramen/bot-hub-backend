package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.InlineButtonDto;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;

public interface InlineButtonService {

    void addInlineButton(InlineButtonDto inlineButtonDto);

    void updateInlineButton(InlineButtonDto inlineButtonDto);

    InlineKeyboardButton[] getInlineButtonRowByKeyboardId(Long inlineKeyboardId);


}