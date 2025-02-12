package com.monora.personalbothub.bot_db.entity.attachment;

import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("INLINE_KEYBOARD")
public class InlineKeyboardAttachmentEntity extends AttachmentEntity {

    @ManyToOne
    @JoinColumn(name = "inline_keyboard_id", nullable = false)
    private InlineKeyboardEntity inlineKeyboard;

}