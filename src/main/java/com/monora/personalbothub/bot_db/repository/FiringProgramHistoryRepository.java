package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.modbus.FiringProgramHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiringProgramHistoryRepository extends JpaRepository<FiringProgramHistoryEntity, Integer> {

}