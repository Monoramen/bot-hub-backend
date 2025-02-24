package com.monora.personalbothub.bot_db.entity.attachment;

import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("KEYBOARD")
public class KeyboardAttachmentEntity extends AttachmentEntity {
    @ManyToOne
    @JoinColumn(name = "keyboard_id", nullable = true)
    private KeyboardEntity keyboard;
}