package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_impl.mapper.CommandMapper;
import com.monora.personalbothub.bot_impl.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commands")
@RequiredArgsConstructor
public class CommandController {

    private final CommandService commandService;
    private final CommandMapper commandMapper;

    @GetMapping("/name")
    public ResponseEntity<String> getCommandName(@RequestBody CommandDto commandDto) {
        CommandEntity commandEntity = commandService.findCommand(commandDto.command());
        if (commandEntity != null) {
            return ResponseEntity.ok(commandEntity.getResponse());
        }
        return ResponseEntity.notFound().build();
    }
}
