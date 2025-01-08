package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InlineKeyboardRepository extends JpaRepository<InlineKeyboardEntity, Long> {
    Optional<InlineKeyboardEntity> findById(Long id);
}
