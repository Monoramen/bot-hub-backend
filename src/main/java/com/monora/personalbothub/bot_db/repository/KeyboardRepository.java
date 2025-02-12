package com.monora.personalbothub.bot_db.repository;


import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyboardRepository extends JpaRepository<KeyboardEntity, Long> {
}
