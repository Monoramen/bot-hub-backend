package com.monora.personalbothub.bot_impl.util.handler;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_impl.service.CommandService;
import com.monora.personalbothub.bot_impl.service.InlineButtonService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MessageHandler implements UpdateHandler {

    private final CommandService commandService;
    private final InlineButtonService inlineButtonService;
    private final TelegramBot telegramBot;

//    public MessageHandler(TelegramBot telegramBot, CommandService commandService) {
//        this.telegramBot = telegramBot;
//        this.commandService = commandService;
//    }

    @Override
    public void handle(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String chatId = update.message().chat().id().toString();
            String text = update.message().text();
            String responseText = getCommand(text);
            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton("url").url("www.google.com"),
                            new InlineKeyboardButton("callback_data").callbackData("callback_data"),
                            new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query")
                    });
            inlineButtonService.getInlineButtonRowByKeyboardId(1);
            SendMessage request = new SendMessage(chatId, responseText).replyMarkup(inlineKeyboard);
            telegramBot.execute(request);
            log.info("Handled message: {}", text);
        }
    }

    private String getCommand(String command) {
        CommandEntity commandEntity = commandService.findCommand(command);
        return commandEntity.getResponse();
    }

}

