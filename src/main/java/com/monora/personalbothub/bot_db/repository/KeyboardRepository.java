package com.monora.personalbothub.bot_db.repository;


import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeyboardRepository extends JpaRepository<KeyboardEntity, Long> {
    @Query("SELECT kae.keyboard FROM KeyboardAttachmentEntity kae WHERE kae.command.id = :commandId")
    Optional<KeyboardEntity> findByCommandId(@Param("commandId") Long commandId);

}
