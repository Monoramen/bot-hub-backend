package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.attachment.KeyboardAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyboardAttachmentRepository extends JpaRepository<KeyboardAttachmentEntity, Long> {
    List<KeyboardAttachmentEntity> findAll();
}