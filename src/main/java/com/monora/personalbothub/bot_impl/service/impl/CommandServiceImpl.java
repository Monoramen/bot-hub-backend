package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.CommandRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.CommandResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_db.repository.AttachmentRepository;
import com.monora.personalbothub.bot_db.repository.CommandRepository;
import com.monora.personalbothub.bot_impl.mapper.CommandMapper;
import com.monora.personalbothub.bot_impl.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final CommandRepository commandRepository;
    private final CommandMapper commandMapper;
    private final AttachmentRepository attachmentRepository;

    @Override
    public CommandResponseDTO create(CommandRequestDTO commandRequestDTO) {
        if (commandRepository.findByCommand(commandRequestDTO.command()).isPresent()) {
            log.warn("Command already exists: {}", commandRequestDTO.command());
            throw new ApiException(ApiErrorType.BAD_REQUEST, "Command already exists: " + commandRequestDTO.command());
        }

        CommandEntity commandEntity = commandMapper.toEntity(commandRequestDTO);
        CommandEntity savedCommand = commandRepository.save(commandEntity);
        return commandMapper.toResponseDTO(savedCommand);
    }

    @Override
    public CommandResponseDTO update(Long id, CommandRequestDTO commandRequestDTO) {

        CommandEntity existingCommand = commandRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Command with ID: " + id + " not found"));

        existingCommand.setCommand(commandRequestDTO.command());
        existingCommand.setResponse(commandRequestDTO.response());


        CommandEntity updatedCommand = commandRepository.save(existingCommand);
        return commandMapper.toResponseDTO(updatedCommand);
    }

    @Override
    public void delete(Long id) {
        CommandEntity existingCommand = commandRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Command with ID: " + id + " not found"));

        commandRepository.delete(existingCommand);
    }

    @Override
    public CommandResponseDTO findById(Long id) {
        log.info("Searching for command with ID: {}", id);
        CommandEntity commandEntity = commandRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Command with ID: " + id + " not found"));
        return commandMapper.toResponseDTO(commandEntity);
    }

    @Override
    public List<CommandResponseDTO> findAll() {
        List<CommandEntity> commands = commandRepository.findAll();
        if (commands.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "Commands not found");
        }
        return commandMapper.toResponseDTOList(commands);
    }

    @Override
    public CommandResponseDTO findByCommand(String command) {
        CommandEntity commandEntity = commandRepository.findByCommand(command)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Command with name: " + command + " not found"));
        return commandMapper.toResponseDTO(commandEntity);
    }


}
