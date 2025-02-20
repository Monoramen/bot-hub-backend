package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KeyboardResponseDTO(
        @Nullable
        Long id,
        @NotNull
        String keyboardName,
        @Nullable
        Set<ButtonResponseDTO> buttons,
        @Nullable
        boolean oneTimeKeyboard,
        @Nullable
        boolean resizeKeyboard,
        @Nullable
        boolean selective,
        @Nullable
        boolean autoRemove

) {
}
