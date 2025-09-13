package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TemperatureRepository  extends JpaRepository<TemperatureEntity, Long> {
    List<TemperatureEntity> findAll();

    Page<TemperatureEntity> findAll(Pageable pageable);

    // Фильтр по времени — тоже с пагинацией
    Page<TemperatureEntity> findByTimestampAfter(LocalDateTime time, Pageable pageable);

    List<TemperatureEntity> findBySessionId(long sessionId);
}