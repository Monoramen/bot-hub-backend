package com.monora.personalbothub.bot_api.dto.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ButtonResponseDTO(
        @Nullable
        Long id,
        @NotNull
        String text,
        Long keyboardId
) {}
