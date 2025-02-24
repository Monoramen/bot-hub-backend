package com.monora.personalbothub.bot_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;
import com.monora.personalbothub.bot_db.enums.AttachmentTypeEnum;
import jakarta.annotation.Nullable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AttachmentRequestDTO(
        @Nullable
        @JsonProperty("id")
        Long id,
        @Nullable
        @JsonProperty("type")
        AttachmentTypeEnum type,
        @Nullable
        @JsonProperty("command_id")
        Long commandId,
        @Nullable
        @JsonProperty("inline_keyboard_id")
        Long inlineKeyboardId,
        @Nullable
        @JsonProperty("keyboard_id")
        Long keyboardId
) {

}
