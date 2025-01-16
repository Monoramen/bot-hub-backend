package com.monora.personalbothub.bot_api.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

import java.util.List;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommandDto(
        @NotNull
        String command,
        String response,
        List<InlineKeyboardDto> inlineKeyboards
) {

}
