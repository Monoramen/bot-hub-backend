package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.attachment.InlineKeyboardAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InlineKeyboardAttachmentRepository extends JpaRepository<InlineKeyboardAttachmentEntity, Long> {
    List<InlineKeyboardAttachmentEntity> findAll();

}