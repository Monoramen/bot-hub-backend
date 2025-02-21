package com.monora.personalbothub.bot_db.entity.attachment.keyboard;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "button")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Генерировать equals и hashCode только для полей с @EqualsAndHashCode.Include
public class ButtonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Включаем только это поле в equals и hashCode
    private long id;

    @Column(name = "text", nullable = false, unique = true)
    private String text;

    @Column(name = "row")
    private int row;

    @Column(name = "position")
    private int position;

    @Column(name = "request_contact")
    private Boolean requestContact = false;

    @Column(name = "request_location")
    private Boolean requestLocation = false;

    @ManyToOne
    @JoinColumn(name = "keyboard_id", nullable = false)
    private KeyboardEntity keyboard;
}