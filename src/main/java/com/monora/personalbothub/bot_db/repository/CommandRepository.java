package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandRepository extends JpaRepository<CommandEntity, Long> {

    Optional<CommandEntity> findByCommand(String command);

    List<CommandEntity> findAll();

}