package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.function.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, Long> {
}
