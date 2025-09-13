package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.modbus.FiringProgramHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FiringProgramHistoryRepository extends JpaRepository<FiringProgramHistoryEntity, Long> {
    @Query("SELECT p FROM FiringProgramHistoryEntity p " +
            "JOIN FiringSessionEntity s ON p.id = s.program.id " +
            "WHERE s.id = :sessionId")
    Optional<FiringProgramHistoryEntity> findBySessionId(@Param("sessionId") Long sessionId);
}