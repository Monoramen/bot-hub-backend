package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.modbus.FiringSessionEntity;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiringSessionRepository extends JpaRepository<FiringSessionEntity, Long> {
    List<FiringSessionEntity> findByStatus(FiringStatus status);
    Optional<FiringSessionEntity> findFirstByStatusOrderByStartTimeDesc(FiringStatus status);

    @Query("SELECT s FROM FiringSessionEntity s ORDER BY s.startTime DESC")
    List<FiringSessionEntity> findTopNByOrderByStartTimeDesc(@Param("limit") int limit);

    // 1. Получить сессию вместе с программой и шагами (жадная загрузка)
    @Query("SELECT fs FROM FiringSessionEntity fs " +
            "JOIN FETCH fs.program p " +
            "LEFT JOIN FETCH p.steps " +
            "WHERE fs.id = :sessionId")
    Optional<FiringSessionEntity> findWithProgramById(@Param("sessionId") Long sessionId);

    // 2. Получить все показания температуры для сессии, отсортированные по времени
    @Query("SELECT t FROM TemperatureEntity t " +
            "WHERE t.session.id = :sessionId " +
            "ORDER BY t.timestamp ASC")
    List<TemperatureEntity> findTemperatureReadingsBySessionId(@Param("sessionId") Long sessionId);

    // 3. Получить последнюю запись температуры для сессии
    @Query("SELECT t FROM TemperatureEntity t " +
            "WHERE t.session.id = :sessionId " +
            "ORDER BY t.timestamp DESC")
    Optional<TemperatureEntity> findLatestTemperatureBySessionId(@Param("sessionId") Long sessionId);
}