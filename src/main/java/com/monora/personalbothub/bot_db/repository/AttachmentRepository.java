package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.attachment.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
    List<AttachmentEntity> findAll();

    List<AttachmentEntity> findByCommandId(Long commandId);
}