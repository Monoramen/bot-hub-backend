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
    @Query("SELECT inlinekb FROM InlineKeyboardEntity inlinekb WHERE inlinekb.id = (SELECT c.inlineKeyboard.id FROM CommandEntity c WHERE c.id = :commandId)")
    Optional<InlineKeyboardEntity> findByCommandId(@Param("commandId") Long commandId);

    Optional<InlineKeyboardEntity> findById(Long id);

    List<InlineKeyboardEntity> findAll();

    Optional<InlineKeyboardEntity> findByInlineKeyboardName(String inlineKeyboardName);
}
