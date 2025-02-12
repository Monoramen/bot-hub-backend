package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineButtonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InlineButtonRepository extends JpaRepository<InlineButtonEntity, Long> {
    List<InlineButtonEntity> findAllByInlineKeyboardId(Long inlineKeyboardId);


    //        @Query("SELECT b FROM InlineButtonEntity b WHERE b.inlineKeyboard.id = :inlineKeyboardId")
//        List<InlineButtonEntity> findAllByInlineKeyboardId(@Param("inlineKeyboardId") Long inlineKeyboardId);
//
    Optional<InlineButtonEntity> findByText(String text);
}
