package com.monora.personalbothub.bot_impl.util.handler.impl;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_impl.service.InlineKeyboardService;
import com.monora.personalbothub.bot_impl.util.handler.CommandAttachmentHandler;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class InlineKeyboardAttachmentHandler implements CommandAttachmentHandler {

    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public boolean supports(CommandEntity command) {
        return command.getAttachments() != null;
    }

    @Override
    public void decorate(SendMessage sendMessage, CommandEntity command) {
        log.info("Декорирование сообщения inline-клавиатурой для команды {}", command.getCommand());
        var inlineKeyboard = inlineKeyboardService.getInlineKeyboardByCommandId(command.getId());
        sendMessage.replyMarkup(inlineKeyboard);
    }
}