package com.monora.personalbothub.bot_impl.service;

import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_db.entity.CommandEntity;

import java.util.List;
import java.util.Optional;

public interface CommandService {

    CommandEntity  findCommand(String command);

    List<CommandEntity> findAllCommands();

    void addCommand(CommandDto commandDto);

    Optional<CommandEntity> findById(Long id);

}
