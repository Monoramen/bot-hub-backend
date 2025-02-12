package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InlineKeyboardRepository extends JpaRepository<InlineKeyboardEntity, Long> {
    @Query("SELECT ik FROM InlineKeyboardEntity ik JOIN AttachmentEntity a ON ik.id = a.id WHERE a.command.id = :commandId")
     Optional<InlineKeyboardEntity> findByCommandId(@Param("commandId") Long commandId);

    Optional<InlineKeyboardEntity> findById(Long id);

    List<InlineKeyboardEntity> findAll();

    Optional<InlineKeyboardEntity> findByInlineKeyboardName(String inlineKeyboardName);
}
