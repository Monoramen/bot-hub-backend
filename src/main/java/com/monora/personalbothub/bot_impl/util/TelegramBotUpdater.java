package com.monora.personalbothub.bot_impl.util;

import com.monora.personalbothub.bot_impl.util.handler.UpdateDispatcher;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class TelegramBotUpdater {

    private final TelegramBot telegramBot;
    private final UpdateDispatcher updateDispatcher;
    private int offset = 0; // Значение по умолчанию для offset

    public TelegramBotUpdater(TelegramBot telegramBot, UpdateDispatcher updateDispatcher) {
        this.telegramBot = telegramBot;
        this.updateDispatcher = updateDispatcher;
    }

    @PostConstruct
    public void init() {
        new Thread(this::pollUpdates).start();
    }

    public void pollUpdates() {
        while (true) {
            fetchUpdates();

            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Update thread was interrupted: ", e);
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
                log.error("Error fetching updates: ", e);
            }
        });
    }

    private void updateListener(List<Update> updates) {
        if (updates != null && !updates.isEmpty()) { // Проверяем на пустоту списка обновлений
            updates.forEach(update -> {
                updateDispatcher.dispatch(update); // Передаем обработку обновления в UpdateDispatcher
                offset = update.updateId() + 1; // Обновляем offset
            });
        } else {
            log.info("Update list is empty");
        }
    }
}

