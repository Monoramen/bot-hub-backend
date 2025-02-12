package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.monora.personalbothub.bot_db.enums.AttachmentTypeEnum;
import jakarta.annotation.Nullable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record AttachmentResponseDTO(
        @JsonProperty("id")
        @Nullable
        Long id,
        @JsonProperty("type")
        @Nullable
        AttachmentTypeEnum type,
        @Nullable
        @JsonProperty("command_id")
        Long commandId,
        @Nullable
        @JsonProperty("inline_keyboard")
        InlineKeyboardResponseDTO inlineKeyboard,
        @Nullable
        @JsonProperty("keyboard")
        KeyboardResponseDTO keyboard
) {}
