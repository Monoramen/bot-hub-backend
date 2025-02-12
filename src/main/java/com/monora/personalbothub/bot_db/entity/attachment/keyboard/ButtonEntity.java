package com.monora.personalbothub.bot_db.entity.attachment.keyboard;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "button")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ButtonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "text", nullable = false, unique = true)
    private String text;

    @ManyToOne
    @JoinColumn(name = "keyboard_id", nullable = false)
    private KeyboardEntity keyboard;
}

