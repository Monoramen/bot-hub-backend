package com.monora.personalbothub.bot_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommandRequestDTO(
        @JsonProperty("id")
        @Nullable Long id,
        @JsonProperty("command")
        @NotNull String command,
        @JsonProperty("response")
        @Nullable String response,
        @JsonProperty("attachment_ids")
        @Nullable Set<Long> attachmentIds
) {}
