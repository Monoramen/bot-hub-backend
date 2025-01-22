package com.monora.personalbothub.bot_api.dto.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommandResponseDTO(
        long id,
        String command,
        String response,
        Long inlineKeyboardId // DTO для клавиатуры ответа
) {}
