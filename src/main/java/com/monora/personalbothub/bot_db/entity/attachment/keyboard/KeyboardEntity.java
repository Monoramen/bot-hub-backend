package com.monora.personalbothub.bot_db.entity.attachment.keyboard;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "keyboard")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KeyboardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "keyboard_name", nullable = false, unique = true)
    private String keyboardName;


    @Column(name = "one_time_keyboard")
    private Boolean oneTimeKeyboard = true;

    @Column(name = "resize_keyboard")
    private Boolean resizeKeyboard = false;

    @Column(name = "selective")
    private Boolean selective = false;

    @Column(name = "auto_remove")
    private boolean autoRemove = false;

    @OneToMany(mappedBy = "keyboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ButtonEntity> buttons;
}

