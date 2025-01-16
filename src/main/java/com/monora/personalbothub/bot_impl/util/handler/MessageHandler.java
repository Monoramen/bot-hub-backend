package com.monora.personalbothub.bot_impl.util.handler;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
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
    private final InlineKeyboardService inlineKeyboardService;
    private final TelegramBot telegramBot;

    @Override
    public void handle(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String chatId = update.message().chat().id().toString();
            String text = update.message().text();
            String responseText = getCommand(text).getResponse();
            var inlineKeyboard = inlineKeyboardService.getInlineKeyboardByCommandId(getCommand(text).getId());
            SendMessage request = new SendMessage(chatId, responseText);
            if (inlineKeyboard != null) {
                request.replyMarkup(inlineKeyboard);
            }

            telegramBot.execute(request);
            log.info("Handled message: {}", text);
        }
    }

    private CommandEntity getCommand(String command) {
        return commandService.findCommand(command);
    }



}

