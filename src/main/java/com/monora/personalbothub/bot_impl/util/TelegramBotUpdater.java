package com.monora.personalbothub.bot_impl.util;

import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_impl.mapper.CommandMapper;
import com.monora.personalbothub.bot_impl.service.CommandService;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class TelegramBotUpdater {

    private final CommandService commandService;
    private final TelegramBot telegramBot;
    private final CommandMapper commandMapper;
    private int offset = 0; // Значение по умолчанию для offset

    public TelegramBotUpdater(TelegramBot telegramBot, CommandService commandService, CommandMapper commandMapper) {
        this.telegramBot = telegramBot;
        this.commandService = commandService;
        this.commandMapper = commandMapper;
    }

    @PostConstruct
    public void init() {
        // Создаем поток для постоянного получения обновлений
        new Thread(this::pollUpdates).start();
    }

    public void pollUpdates() {
        while (true) {
            fetchUpdates();

            try {
                // Пауза между запросами, чтобы не перегружать сервер Telegram
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Поток обновлений был прерван: " + e.getMessage());
            }
        }
    }

    public void fetchUpdates() {
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(offset).timeout(10);

        telegramBot.execute(getUpdates, new Callback<GetUpdates, GetUpdatesResponse>() {
            @Override
            public void onResponse(GetUpdates request, GetUpdatesResponse response) {
                List<Update> updates = response.updates();
                updateListener(updates);
            }

            @Override
            public void onFailure(GetUpdates request, IOException e) {
                System.err.println("Ошибка при получении обновлений: " + e.getMessage());
            }
        });
    }


    private void updateListener(List<Update> updates) {
        if (updates != null) { // Проверяем, что updates не null
            updates.forEach(update -> {
                if (update.message() != null && update.message().text() != null) {
                    String chatId = update.message().chat().id().toString();
                    String text = update.message().text();

                    Keyboard keyboard = new ReplyKeyboardMarkup(
                            new KeyboardButton[]{
                                    new KeyboardButton("text"),
                                    new KeyboardButton("contact").requestContact(true),
                                    new KeyboardButton("location").requestLocation(true)
                            }
                    );
                    InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                            new InlineKeyboardButton[]{
                                    new InlineKeyboardButton("url").url("www.google.com"),
                                    new InlineKeyboardButton("callback_data").callbackData("callback_data"),
                                    new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query")
                            });
                    SendMessage request = new SendMessage(chatId, getCommand(text))
                            .parseMode(ParseMode.HTML)
                            .disableWebPagePreview(true)
                            .disableNotification(true)
                            .replyMarkup(inlineKeyboard);

                    telegramBot.execute(request);

                    log.info(update.message().text());
                    // Обновляем offset
                    offset = update.updateId() + 1;
                }
            });
        } else {
            log.info("Update list is empty");
        }
    }

    public String getCommand(String command) {
        CommandEntity commandEntity = commandService.findCommand(command);
        log.info(commandEntity.getResponse().toString());
        return commandEntity.getResponse();
    }



}
