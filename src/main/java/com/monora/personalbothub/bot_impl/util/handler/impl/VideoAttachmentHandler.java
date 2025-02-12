package com.monora.personalbothub.bot_impl.util.handler.impl;

import com.monora.personalbothub.bot_impl.util.handler.AttachmentHandler;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VideoAttachmentHandler implements AttachmentHandler {

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        return message != null && message.video() != null;
    }

    @Override
    public void handle(Update update) {
        log.info("Обработка видео вложения, updateId: {}", update.updateId());
    }
} 