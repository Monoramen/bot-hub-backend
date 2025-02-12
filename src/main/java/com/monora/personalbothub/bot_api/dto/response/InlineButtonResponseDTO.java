package com.monora.personalbothub.bot_api.dto.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record InlineButtonResponseDTO(
        @NotNull
        String text,
        @Nullable
        String url,
        @Nullable
        String callbackData,
        @Nullable
        String switchInlineQuery,
        @Nullable
        Integer row,
        @Nullable
        Integer position,
        @Nullable
        String inlineKeyboardId
) {}
