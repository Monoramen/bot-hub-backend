package com.monora.personalbothub.bot_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record InlineButtonRequestDTO(
        @Nullable
        Long id,
        @NotNull
        String text,
        @Nullable
        String url,
        @Nullable
        String callbackData,
        @Nullable
        String switchInlineQuery,
        @NotNull
        Integer row,
        @NotNull
        Integer position,
        @Nullable
        @JsonProperty("inline_keyboard_id")
        Long inlineKeyboardId
) {
        @AssertTrue(message = "Only one type of button (url, callbackData, or switchInlineQuery) must be set.")
        public boolean isValidButtonType() {
                long count = Stream.of(url, callbackData, switchInlineQuery)
                        .filter(Objects::nonNull).count();
                return count == 1;
        }
}
