package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InlineKeyboardRepository extends JpaRepository<InlineKeyboardEntity, Long> {
    Optional<InlineKeyboardEntity> findById(Long id);

    @Query("SELECT inlinekb FROM InlineKeyboardEntity inlinekb JOIN FETCH inlinekb.commands c WHERE c.id = :commandId")
    InlineKeyboardEntity findByCommandId(@Param("commandId") Long commandId);

    List<InlineKeyboardEntity> findAll();
}
