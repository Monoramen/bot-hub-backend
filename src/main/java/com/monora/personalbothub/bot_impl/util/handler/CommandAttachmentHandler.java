package com.monora.personalbothub.bot_impl.util.handler;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandAttachmentHandler {
    boolean supports(CommandEntity command);
    void decorate(SendMessage sendMessage, CommandEntity command);
}
