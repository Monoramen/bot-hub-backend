package com.monora.personalbothub.bot_db.entity.modbus;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "firing_program_history")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FiringProgramHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "firing_step", joinColumns = @JoinColumn(name = "program_id"))
    private List<FiringStep> steps = new ArrayList<>(); // ровно 5
}
