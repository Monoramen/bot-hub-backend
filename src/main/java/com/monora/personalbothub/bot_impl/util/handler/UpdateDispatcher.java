package com.monora.personalbothub.bot_impl.util.handler;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateDispatcher {

    private final List<UpdateHandler> handlers;

    public UpdateDispatcher(List<UpdateHandler> handlers) {
        this.handlers = handlers;
    }

    public void dispatch(Update update) {
        for (UpdateHandler handler : handlers) {
            handler.handle(update);
        }
    }
}
