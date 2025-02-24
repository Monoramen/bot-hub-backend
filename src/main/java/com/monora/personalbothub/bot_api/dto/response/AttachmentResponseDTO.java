package com.monora.personalbothub.bot_api.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monora.personalbothub.bot_db.enums.AttachmentTypeEnum;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InlineKeyboardAttachmentResponseDTO.class, name = "INLINE_KEYBOARD"),
        @JsonSubTypes.Type(value = KeyboardAttachmentResponseDTO.class, name = "KEYBOARD")
})
@Getter
@Setter
public class AttachmentResponseDTO {
    @Nullable
    protected Long id;

    @Nullable
    protected AttachmentTypeEnum type;

    @Nullable
    protected Long commandId;
}
