package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
    List<AttachmentEntity> findAll();
    List<AttachmentEntity> findByCommandId(Long commandId);


}