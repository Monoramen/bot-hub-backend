package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;
import com.monora.personalbothub.bot_impl.service.InlineKeyboardService;

import java.util.List;

public class InlineKeyboardServiceImpl implements InlineKeyboardService {
    @Override
    public void addInlineKeyboard(InlineKeyboardEntity inlineKeyboard) {

    }

    @Override
    public InlineKeyboardEntity getInlineKeyboardByName(String name) {
        return null;
    }

    @Override
    public void deleteInlineKeyboardByName(String name) {

    }

    @Override
    public List<InlineKeyboardEntity> getAllInlineKeyboards() {
        return List.of();
    }
}
