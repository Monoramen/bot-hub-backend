package com.monora.personalbothub.bot_db.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "inline_keyboard")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InlineKeyboardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "inline_keyboard_name", nullable = false, unique = true)
    private String inlineKeyboardName;


    @ManyToMany (cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "inline_keyboard_button",
            joinColumns = @JoinColumn(name = "inline_keyboard_id"),
            inverseJoinColumns = @JoinColumn(name = "inline_button_id")
    )
    private List<InlineButtonEntity> buttons;

}
