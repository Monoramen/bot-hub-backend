package com.monora.personalbothub.bot_impl.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KeyboardRemoveService {

    private final TelegramBot telegramBot;

    public void removeKeyboard(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Thanx")
                .replyMarkup(new ReplyKeyboardRemove());
        telegramBot.execute(sendMessage);
    }

}
