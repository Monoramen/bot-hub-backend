package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.entity.InlineButtonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InlineButtonRepository extends JpaRepository<InlineButtonEntity, Long> {

}
