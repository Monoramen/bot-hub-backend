// src/main/java/com/monora/personalbothub/bot_db/entity/modbus/FiringSessionEntity.java
package com.monora.personalbothub.bot_db.entity.modbus;

import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "firing_session")
public class FiringSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private FiringProgramHistoryEntity program;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FiringStatus status;

    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

    @Column(name = "max_recorded_temperature")
    private Double maxRecordedTemperature;

    // Опционально: комментарий оператора
    @Column(name = "notes", length = 1000)
    private String notes;

    // Новое поле для связи с показаниями температуры
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemperatureEntity> temperatureReadings;

}