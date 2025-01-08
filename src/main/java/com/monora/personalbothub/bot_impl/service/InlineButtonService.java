package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_db.entity.InlineButtonEntity;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;

import java.util.List;

public interface InlineButtonService {

    void addInlineButton(InlineButtonEntity inlineButton);

    InlineKeyboardButton[] getInlineButtonRowByKeyboardId(int inlineKeyboardId);




}
