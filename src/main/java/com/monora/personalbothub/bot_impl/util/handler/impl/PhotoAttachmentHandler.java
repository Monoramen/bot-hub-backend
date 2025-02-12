package com.monora.personalbothub.bot_impl.util.handler.impl;


import com.monora.personalbothub.bot_impl.util.handler.AttachmentHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PhotoAttachmentHandler implements AttachmentHandler {

    private final TelegramBot telegramBot;

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        return message != null && message.photo() != null && message.photo().length > 0;
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        if (message == null || message.photo() == null || message.photo().length == 0) {
            log.warn("Нет фото во вложении, updateId: {}", update.updateId());
            return;
        }

        // Получаем идентификатор чата из входящего обновления
        String chatId = message.chat().id().toString();

        // Массив фотографий отсортирован по возрастанию размера, поэтому выбираем последнее фото
        PhotoSize[] photos = message.photo();
        PhotoSize largestPhoto = photos[photos.length - 1];
        String fileId = largestPhoto.fileId();

        // Создаём запрос на отправку фото с подписью и настройками форматирования
        SendPhoto sendPhoto = new SendPhoto(chatId, fileId)
                .caption("Спасибо за ваше фото!")
                .parseMode(ParseMode.HTML);
        // При необходимости можно добавить дополнительные параметры, например:
        // .disableNotification(true)
        // .replyToMessageId(message.messageId());

        try {
            // Отправка запроса синхронно
            SendResponse response = telegramBot.execute(sendPhoto);
            if (response.isOk()) {
                log.info("Фото успешно отправлено, updateId: {}", update.updateId());
            } else {
                log.error("Ошибка при отправке фото, updateId: {}. Код ошибки: {}", update.updateId(), response.errorCode());
            }
        } catch (Exception e) {
            log.error("Ошибка при отправке фото, updateId: {}", update.updateId(), e);
        }
    }
}