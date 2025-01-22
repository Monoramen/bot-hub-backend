package com.monora.personalbothub.bot_api.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommandRequestDTO(
        @NotNull
        String command,
        String response,
        InlineKeyboardRequestDTO inlineKeyboards // DTO для клавиатуры запроса
) {}
