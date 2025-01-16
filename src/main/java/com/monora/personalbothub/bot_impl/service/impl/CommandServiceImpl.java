package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.repository.CommandRepository;
import com.monora.personalbothub.bot_impl.mapper.CommandMapper;
import com.monora.personalbothub.bot_impl.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final CommandRepository commandRepository;
    private final CommandMapper commandMapper;

    @Override
    public CommandEntity findCommand(String command) {
        return commandRepository.findByCommand(command);
    }

    @Override
    public List<CommandEntity> findAllCommands() {
        return commandRepository.findAll();
    }

    @Override
    public void addCommand(CommandDto commandDto) {
        if (commandRepository.findByCommand(commandDto.command()) != null) {
            log.info("Command already exists: " + commandDto.command());
            throw new IllegalArgumentException("Command already exists: " + commandDto.command());
        }
        CommandEntity commandEntity = commandMapper.toEntity(commandDto);
        commandRepository.save(commandEntity);
    }

    @Override
    public Optional<CommandEntity> findById(Long id) {
        log.info("Searching for command with ID: {}", id);
        return commandRepository.findById(id);
    }

}