package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;

import java.util.List;

public interface InlineKeyboardService {

    void addInlineKeyboard(InlineKeyboardEntity inlineKeyboard);

    InlineKeyboardEntity getInlineKeyboardByName(String name);

    void deleteInlineKeyboardByName(String name);

    List<InlineKeyboardEntity> getAllInlineKeyboards();

}