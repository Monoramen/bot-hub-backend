package com.monora.personalbothub.bot_api.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record InlineButtonRequestDTO(
        Long id,
        @NotNull
        String text,
        String url,
        String callbackData,
        String switchInlineQuery
) {
}
