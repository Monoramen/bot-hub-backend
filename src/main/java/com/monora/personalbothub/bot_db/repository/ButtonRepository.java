package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.attachment.keyboard.ButtonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ButtonRepository extends JpaRepository<ButtonEntity, Long> {
    List<ButtonEntity> findAllByKeyboardId(Long keyboardId);

    Optional<ButtonEntity> findByText(String text);

}
