package com.monora.personalbothub.bot_db.entity.function;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "goal")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target")
    private String target;

    @Column(name = "date")
    private Date date;

    // Поле, указывающее, выполнена ли цель в текущий день
    @Column(name = "completed")
    private boolean completed;

    @Column(name = "comment")
    private String comment;
}
