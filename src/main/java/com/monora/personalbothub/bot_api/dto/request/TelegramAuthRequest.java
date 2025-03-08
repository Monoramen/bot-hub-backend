package com.monora.personalbothub.bot_api.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TelegramAuthRequest {
    @Nullable
    private String id;
    private String hash;
    private String authDate;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
}
