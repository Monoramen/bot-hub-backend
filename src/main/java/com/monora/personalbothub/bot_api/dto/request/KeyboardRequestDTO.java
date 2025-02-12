package com.monora.personalbothub.bot_api.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KeyboardRequestDTO(
        @Nullable
        Long id,
        @NotNull
        String keyboardName,
        @Nullable
        Set<ButtonRequestDTO> buttons
) {}
