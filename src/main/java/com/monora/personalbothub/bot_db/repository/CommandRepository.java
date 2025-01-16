package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<CommandEntity, Long> {

    CommandEntity findByCommand(String command);

    List<CommandEntity> findAll();

}