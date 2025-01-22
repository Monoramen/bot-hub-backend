package com.monora.personalbothub.bot_db.entity;


import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "inline_keyboard_id", referencedColumnName = "id")
    private InlineKeyboardEntity inlineKeyboard;


}