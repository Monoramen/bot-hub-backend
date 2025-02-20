package com.monora.personalbothub.bot_impl.util.handler.impl;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_impl.service.InlineKeyboardService;
import com.monora.personalbothub.bot_impl.service.KeyboardResult;
import com.monora.personalbothub.bot_impl.service.KeyboardService;
import com.monora.personalbothub.bot_impl.util.handler.CommandAttachmentHandler;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KeyboardAttachmentHandler implements CommandAttachmentHandler {

    private final KeyboardService keyboardService;

    @Override
    public boolean supports(CommandEntity command) {
        return command.getAttachments() != null;
    }

    @Override
    public void decorate(SendMessage sendMessage, CommandEntity command) {

        if (command.getAttachments() == null || command.getAttachments().isEmpty()) {
            log.info("Декорирование сообщения клавиатурой для команды {} отсутствует", command.getCommand());
            return;
        } else {
            log.info("Декорирование сообщения inline-клавиатурой для команды {}", command.getCommand());
            KeyboardResult keyboard = keyboardService.getKeyboardByCommandId(command.getId());
            if (keyboard != null) {
                sendMessage.replyMarkup(keyboard.keyboard());
            }
        }
    }
}