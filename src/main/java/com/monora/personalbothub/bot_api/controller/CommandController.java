package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.request.CommandRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.CommandResponseDTO;
import  com.monora.personalbothub.bot_impl.service.CommandService;
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
    @GetMapping("/all")
    public ResponseEntity<List<CommandResponseDTO>> getAllCommands() {
        log.info("Fetching all commands");
        List<CommandResponseDTO> commands = commandService.findAll();
        return ResponseEntity.ok(commands);
    }

    /**
     * Добавление новой команды
     */
    @PostMapping("/add")
    public ResponseEntity<Void> createCommand(@RequestBody CommandRequestDTO commandRequestDTO) {
        log.info("Received request to create command: {}", commandRequestDTO);
        commandService.create(commandRequestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление команды по ID
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateCommand(@PathVariable Long id, @RequestBody CommandRequestDTO commandRequestDTO) {
        log.info("Received request to update command with ID: {}", id);
        commandService.update(id, commandRequestDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление команды по ID
     */
    @DeleteMapping("/delete/{id}")
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
