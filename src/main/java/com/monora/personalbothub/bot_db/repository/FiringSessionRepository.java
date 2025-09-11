package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.modbus.FiringSessionEntity;
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
}