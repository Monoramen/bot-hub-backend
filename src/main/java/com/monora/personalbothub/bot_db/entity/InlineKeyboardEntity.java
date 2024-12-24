package com.monora.personalbothub.bot_db.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "row")
    private String row;

}
