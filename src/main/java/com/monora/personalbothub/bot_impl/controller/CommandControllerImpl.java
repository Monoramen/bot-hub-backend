package com.monora.personalbothub.bot_impl.controller;

import com.monora.personalbothub.bot_api.controller.CommandController;
import com.monora.personalbothub.bot_api.dto.CommandDto;
import com.monora.personalbothub.bot_db.entity.CommandEntity;
import com.monora.personalbothub.bot_impl.mapper.CommandMapper;
import com.monora.personalbothub.bot_impl.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/commands")
//@RequiredArgsConstructor
//@Validated
public class CommandControllerImpl {

//    private final CommandService commandService;
//    private final CommandMapper commandMapper;

//    @Override
//    @GetMapping("/name")
//    public ResponseEntity<String> getCommandName(@RequestBody CommandDto commandDto) {
//        // Используем только command для поиска
//        CommandEntity foundCommand = commandService.findCommand(commandDto.command());
//
//        if (foundCommand != null) {
//            return ResponseEntity.ok(foundCommand.getResponse());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
