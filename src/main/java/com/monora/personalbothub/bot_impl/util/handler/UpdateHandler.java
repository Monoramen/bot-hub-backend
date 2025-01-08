package com.monora.personalbothub.bot_impl.util.handler;


import com.pengrad.telegrambot.model.Update;

public interface UpdateHandler {
    void handle(Update update);
}
