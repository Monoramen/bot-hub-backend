package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_impl.mapper.CommandMapper;
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
    private final CommandMapper commandMapper;

    @GetMapping("/command")
    public ResponseEntity<CommandDto> getCommandName(@RequestParam(name = "command") String command) {
        log.info("Received request for command name: {}", command);
        CommandEntity commandEntity = commandService.findCommand(command);

        if (commandEntity != null) {
            log.info("Found command: {}", commandEntity);
            return ResponseEntity.ok(commandMapper.toDto(commandEntity));
        }

        log.warn("Command not found for name: {}", command);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommandDto>> getAllCommands() {
        List<CommandEntity> commandEntities = commandService.findAllCommands();

        // Преобразуем список CommandEntity в список CommandDto с помощью маппера
        List<CommandDto> commandDtos = commandMapper.toDtoList(commandEntities);

        return ResponseEntity.ok(commandDtos);
    }

    @GetMapping("/command/{id}")
    public ResponseEntity<CommandDto> getCommandById(Long id) {
            log.info("Received request for command ID: {}", id);

            return commandService.findById(id)
                    .map(commandMapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("Command not found for ID: {}", id);
                        return ResponseEntity.notFound().build();
                    });
    }


    @PostMapping("/add")
    public ResponseEntity<CommandDto> createCommand(@RequestBody CommandDto commandDto) {
        log.info("Received request for command: {}", commandDto);
        commandService.addCommand(commandDto);
        return ResponseEntity.ok().build();
    }


//
//    @PutMapping("/employees/{id}")
//    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//
//        return repository.findById(id)
//                .map(employee -> {
//                    employee.setName(newEmployee.getName());
//                    employee.setRole(newEmployee.getRole());
//                    return repository.save(employee);
//                })
//                .orElseGet(() -> {
//                    return repository.save(newEmployee);
//                });
//    }
//
//    @DeleteMapping("/employees/{id}")
//    void deleteEmployee(@PathVariable Long id) {
//        repository.deleteById(id);
//    }
}