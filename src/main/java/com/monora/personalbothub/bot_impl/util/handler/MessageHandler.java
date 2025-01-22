package com.monora.personalbothub.bot_impl.util.handler;

import com.monora.personalbothub.bot_api.dto.response.CommandResponseDTO;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.repository.CommandRepository;
import com.monora.personalbothub.bot_impl.service.CommandService;
import com.monora.personalbothub.bot_impl.service.InlineKeyboardService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MessageHandler implements UpdateHandler {

    private final CommandService commandService;
    private final CommandRepository commandRepository;
    private final InlineKeyboardService inlineKeyboardService;
    private final TelegramBot telegramBot;
    @Override
    public void handle(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String chatId = update.message().chat().id().toString();
            String text = update.message().text();
            String responseText = getCommand(text).getResponse();
            CommandEntity command = getCommand(text);

            if (command.getInlineKeyboard() != null) {
                var inlineKeyboard = inlineKeyboardService.getInlineKeyboardByCommandId(command.getId());

                SendMessage request = new SendMessage(chatId, responseText);
                if (inlineKeyboard != null) {
                    request.replyMarkup(inlineKeyboard);
                }
                telegramBot.execute(request);
            } else {
                SendMessage request = new SendMessage(chatId, responseText);
                telegramBot.execute(request);
            }
        }
    }


    public CommandEntity getCommand(String command) {
        return commandRepository.findByCommand(command).orElse(null);
    }
    
}

