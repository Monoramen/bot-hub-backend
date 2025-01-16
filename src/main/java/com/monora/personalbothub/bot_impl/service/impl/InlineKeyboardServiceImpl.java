package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;
import com.monora.personalbothub.bot_db.repository.InlineKeyboardRepository;
import com.monora.personalbothub.bot_impl.service.InlineButtonService;
import com.monora.personalbothub.bot_impl.service.InlineKeyboardService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class InlineKeyboardServiceImpl implements InlineKeyboardService {

    private final InlineButtonService inlineButtonService;
    private final InlineKeyboardRepository inlineKeyboardRepository;

    @Override
    public InlineKeyboardMarkup getInlineKeyboardByCommandId(Long commandId) {
        Optional<InlineKeyboardEntity> optionalKeyboard = Optional.ofNullable(inlineKeyboardRepository.findByCommandId(commandId));

        if (optionalKeyboard.isPresent()) {
            InlineKeyboardEntity keyboard = optionalKeyboard.get();
            InlineKeyboardButton[] buttons = inlineButtonService.getInlineButtonRowByKeyboardId(keyboard.getId());

            log.info("keyboard: {}", keyboard.getId());
            log.info("Buttons: {}", buttons);

            return new InlineKeyboardMarkup(buttons);
        }
        return null;
    }

    @Override
    public List<InlineKeyboardEntity> findAll() {
        return inlineKeyboardRepository.findAll();
    }

}
