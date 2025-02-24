package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.request.CommandRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.CommandResponseDTO;
import com.monora.personalbothub.bot_impl.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/commands")
@RequiredArgsConstructor
public class CommandController {

    private final CommandService commandService;

    /**
     * Получение команды по имени
     */
    @GetMapping("/command")
    public ResponseEntity<CommandResponseDTO> getCommandByName(@RequestParam(name = "command") String command) {
        log.info("Fetching command by name: {}", command);
        CommandResponseDTO commandResponse = commandService.findByCommand(command);
        return ResponseEntity.ok(commandResponse);
    }

    /**
     * Получение всех команд
     */
    @GetMapping
    public ResponseEntity<List<CommandResponseDTO>> getAllCommands() {
        log.info("Fetching all commands");
        List<CommandResponseDTO> commands = commandService.findAll();
        return ResponseEntity.ok(commands);
    }

    /**
     * Добавление новой команды с возможностью прикрепить клавиатуру
     */
    @PostMapping("/add")
    public ResponseEntity<CommandResponseDTO> createCommand(@RequestBody CommandRequestDTO commandRequestDTO) {
        log.info("Received request to create command: {}", commandRequestDTO);
        CommandResponseDTO createdCommand = commandService.create(commandRequestDTO);
        return ResponseEntity.ok(createdCommand);  // Возвращаем созданную команду
    }

    /**
     * Обновление команды по ID, включая обновление клавиатуры
     */
    @PutMapping("{id}")
    public ResponseEntity<CommandResponseDTO> updateCommand(@PathVariable Long id, @RequestBody CommandRequestDTO commandRequestDTO) {
        log.info("Received request to update command with ID: {}", id);
        CommandResponseDTO updatedCommand = commandService.update(id, commandRequestDTO);
        return ResponseEntity.ok(updatedCommand);  // Возвращаем обновленную команду
    }

    /**
     * Удаление команды по ID
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        log.info("Received request to delete command with ID: {}", id);
        commandService.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение команды по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommandResponseDTO> getCommandById(@PathVariable Long id) {
        log.info("Fetching command by ID: {}", id);
        CommandResponseDTO commandResponse = commandService.findById(id);
        return ResponseEntity.ok(commandResponse);
    }
}
