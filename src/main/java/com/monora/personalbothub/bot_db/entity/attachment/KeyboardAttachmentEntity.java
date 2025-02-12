package com.monora.personalbothub.bot_db.entity.attachment;

import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import com.monora.personalbothub.bot_db.enums.AttachmentTypeEnum;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
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
    @JoinColumn(name = "keyboard_id")
    private KeyboardEntity keyboard;

}