package com.monora.personalbothub.bot_impl.util.handler.impl;

import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.repository.CommandRepository;
import com.monora.personalbothub.bot_impl.service.CommandService;
import com.monora.personalbothub.bot_impl.service.KeyboardService;
import com.monora.personalbothub.bot_impl.service.impl.KeyboardRemoveService;
import com.monora.personalbothub.bot_impl.util.handler.CommandAttachmentHandler;
import com.monora.personalbothub.bot_impl.util.handler.UpdateHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class MessageHandler implements UpdateHandler {

    private final CommandService commandService;
    private final KeyboardRemoveService keyboardRemoveService;
    private final KeyboardService keyboardService;
    private final CommandRepository commandRepository;
    private final TelegramBot telegramBot;
    private final List<CommandAttachmentHandler> commandAttachmentHandlers;

    @Override
    public boolean supports(Update update) {
        // Поддерживаем только текстовые сообщения
        return update.message() != null && update.message().text() != null;
    }

    @Override
    public void handle(Update update) {
        String chatId = update.message().chat().id().toString();
        String text = update.message().text();

        try {
            // Извлечение команды из БД
            CommandEntity command = getCommand(text);
            String responseText = command.getResponse();
            SendMessage request = new SendMessage(chatId, responseText);

            // Применяем декорирование в случае, если есть прикреплённые элементы
            for (CommandAttachmentHandler handler : commandAttachmentHandlers) {
                if (handler.supports(command)) {
                    handler.decorate(request, command);
                }
            }
            telegramBot.execute(request);

        } catch (ApiException e) {
            log.error("Ошибка обработки команды '{}': {}", text, e.getMessage());
            SendMessage errorMessage = new SendMessage(chatId, "Команда не найдена: " + text);
            telegramBot.execute(errorMessage);
        }
    }

    private CommandEntity getCommand(String commandText) {
        return commandRepository.findByCommand(commandText)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, commandText));
    }
}

