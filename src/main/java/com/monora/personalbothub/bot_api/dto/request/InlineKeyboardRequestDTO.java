package com.monora.personalbothub.bot_api.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record InlineKeyboardRequestDTO(
        @NotNull
        Long id, // ID нужен для запроса
        @NotNull
        String inlineKeyboardName,
        List<InlineButtonRequestDTO> buttons // DTO для кнопок запроса
) {}
