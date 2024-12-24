package com.monora.personalbothub.bot_impl.util;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class TelegramBotUpdater {

    private final TelegramBot telegramBot;
    private int offset = 2; // Значение по умолчанию для offset

    public TelegramBotUpdater(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
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

                if (updates != null) { // Проверяем, что updates не null
                    updates.forEach(update -> {
                        if (update.message() != null && update.message().text() != null) {
                            String chatId = update.message().chat().id().toString();
                            String text = update.message().text();

                            telegramBot.execute(new SendMessage(chatId, "Вы сказали: " + text));

                            // Обновляем offset
                            offset = update.updateId() + 1;
                        }
                    });
                } else {
                    System.out.println("Нет новых обновлений.");
                }
            }

            @Override
            public void onFailure(GetUpdates request, IOException e) {
                System.err.println("Ошибка при получении обновлений: " + e.getMessage());
            }
        });
    }
}
