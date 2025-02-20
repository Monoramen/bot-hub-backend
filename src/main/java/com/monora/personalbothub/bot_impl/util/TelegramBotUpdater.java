package com.monora.personalbothub.bot_impl.util;

import com.monora.personalbothub.bot_impl.service.KeyboardResult;
import com.monora.personalbothub.bot_impl.util.handler.UpdateDispatcher;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
@Component
@Slf4j
public class TelegramBotUpdater {

    private final TelegramBot telegramBot;
    private final UpdateDispatcher updateDispatcher;

    private int offset = 0;

    // Очередь для обновлений
    private final BlockingQueue<Update> updateQueue = new LinkedBlockingQueue<>();

    // Множество для отслеживания уже добавленных updateId
    private final Set<Integer> processedUpdateIds = ConcurrentHashMap.newKeySet();

    public TelegramBotUpdater(TelegramBot telegramBot, UpdateDispatcher updateDispatcher) {
        this.telegramBot = telegramBot;
        this.updateDispatcher = updateDispatcher;
    }

    @PostConstruct
    public void init() {
        // Можно оставить пустым
    }

    @Scheduled(fixedDelay = 2500)
    public void pollUpdates() {
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(offset).timeout(10);
        telegramBot.execute(getUpdates, new Callback<GetUpdates, GetUpdatesResponse>() {
            @Override
            public void onResponse(GetUpdates request, GetUpdatesResponse response) {
                List<Update> updates = response.updates();
                if (updates != null && !updates.isEmpty()) {
                    int maxUpdateId = 0;
                    for (Update update : updates) {
                        // Если обновление ещё не обрабатывалось, добавляем его в очередь
                        if (processedUpdateIds.add(update.updateId())) {
                            updateQueue.offer(update);
                            log.info("Enqueued update: {}", update.updateId());

                        } else {
                            log.info("Skipped duplicate update: {}", update.updateId());
                        }
                        if (update.updateId() > maxUpdateId) {
                            maxUpdateId = update.updateId();
                        }
                    }
                    offset = maxUpdateId + 1;
                } else {
                    log.info("Update list is empty");
                }
            }

            @Override
            public void onFailure(GetUpdates request, IOException e) {
                log.error("Error fetching updates: ", e);
            }
        });
    }

    @Scheduled(fixedDelay = 500)
    public void processUpdateQueue() {
        Update update;
        while ((update = updateQueue.poll()) != null) {
            log.info("Processing update: {}", update.updateId());
            updateDispatcher.dispatch(update);
            // После обработки можно удалить id из множества, если такая логика подходит
            // processedUpdateIds.remove(update.updateId());
        }
    }
}
