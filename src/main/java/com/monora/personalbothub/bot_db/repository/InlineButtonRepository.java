package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.entity.InlineButtonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InlineButtonRepository extends JpaRepository<InlineButtonEntity, Long> {
    @Query("SELECT button FROM InlineButtonEntity button JOIN FETCH button.inlineKeyboards k WHERE k.id = :inlineKeyboardId")
    List<InlineButtonEntity> findAllByInlineKeyboardId(@Param("inlineKeyboardId") int inlineKeyboardId);
}
