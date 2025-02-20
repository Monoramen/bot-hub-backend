package com.monora.personalbothub.bot_impl.service;

import com.pengrad.telegrambot.model.request.Keyboard;

public record KeyboardResult(Keyboard keyboard, boolean autoRemove) { }