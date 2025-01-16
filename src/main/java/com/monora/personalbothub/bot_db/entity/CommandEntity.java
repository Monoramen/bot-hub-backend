package com.monora.personalbothub.bot_db.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "command")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "command", nullable = false)
    private String command;

    @Column(name = "response")
    private String response;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "command_inline_keyboard",
            joinColumns = @JoinColumn(name = "command_id"),
            inverseJoinColumns = @JoinColumn(name = "inline_keyboard_id")
    )
    private Set<InlineKeyboardEntity> inlineKeyboards = new HashSet<>();

}