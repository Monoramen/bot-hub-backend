package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

public interface InlineKeyboardService {

    InlineKeyboardMarkup getInlineKeyboardByCommandId(Long commandId);

}