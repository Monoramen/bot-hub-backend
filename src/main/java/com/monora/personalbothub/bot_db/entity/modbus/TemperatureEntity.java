package com.monora.personalbothub.bot_db.entity.modbus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "temperature")
public class TemperatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temperature", nullable = false)
    private float temperature;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // Новое поле для связи с сеансом обжига
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private FiringSessionEntity session;

}
