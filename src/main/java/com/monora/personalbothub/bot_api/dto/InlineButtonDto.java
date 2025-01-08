package com.monora.personalbothub.bot_api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record InlineButtonDto(
        @NotNull
        String text,
        String url,
        String callback_data,
        String switch_inline_query
) {}
