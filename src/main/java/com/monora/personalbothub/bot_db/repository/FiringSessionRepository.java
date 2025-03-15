package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.modbus.FiringSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiringSessionRepository extends JpaRepository<FiringSessionEntity, Long> {
}