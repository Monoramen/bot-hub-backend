package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record KeyboardResponseDTO(
        @Nullable
        Long id,
        @NotNull
        String keyboardName,
        @Nullable
        Set<ButtonResponseDTO> buttons // DTO для кнопок ответа
) {}
