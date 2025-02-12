package com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard;


import com.monora.personalbothub.bot_db.entity.CommandEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "inline_keyboard")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InlineKeyboardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "inline_keyboard_name", nullable = false, unique = true)
    private String inlineKeyboardName;


    @OneToMany(mappedBy = "inlineKeyboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InlineButtonEntity> buttons;

}
