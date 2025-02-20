package com.monora.personalbothub.bot_impl.util.handler.impl;

import com.monora.personalbothub.bot_impl.util.handler.AttachmentHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class VideoHandler implements AttachmentHandler {

    private final TelegramBot telegramBot;

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        return message != null && message.video() != null;
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        if (message == null || message.video() == null) {
            log.warn("Видео не обнаружено, updateId: {}", update.updateId());
            return;
        }
        log.info("Получено видео-вложение, updateId: {}", update.updateId());

        // Получаем идентификатор чата
        String chatId = message.chat().id().toString();

        // Заглушка: отправляем пользователю текстовое сообщение, что обработка видео пока не реализована
        SendMessage stubResponse = new SendMessage(chatId, "Получено видео, обработка пока не реализована.");
        try {
            SendResponse response = telegramBot.execute(stubResponse);
            if (response.isOk()) {
                log.info("Заглушка для видео успешно обработана, updateId: {}", update.updateId());
            } else {
                log.error("Ошибка при отправке заглушки для видео, updateId: {}. Код ошибки: {}",
                        update.updateId(), response.errorCode());
            }
        } catch (Exception e) {
            log.error("Ошибка при отправке заглушки для видео, updateId: {}", update.updateId(), e);
        }
    }
}