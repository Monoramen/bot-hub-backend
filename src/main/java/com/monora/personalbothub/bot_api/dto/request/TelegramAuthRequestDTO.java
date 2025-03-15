package com.monora.personalbothub.bot_api.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TelegramAuthRequestDTO(
        @Nullable
        Long id,
        @NotNull
        String firstName,
        String lastName,
        String username,
        String photoUrl,
        Long authDate,
        String hash
){}

