package com.monora.personalbothub.bot_api.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ButtonRequestDTO(
        @Nullable
        Long id,
        @NotNull
        String text,
        @NotNull
        Long keyboardId,
        @Nullable
        int position,
        @Nullable
        int row,
        @Nullable
        boolean requestContact,
        @Nullable
        boolean requestLocation
) {}


