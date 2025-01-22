package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.InlineKeyboardDto;
import com.monora.personalbothub.bot_api.dto.response.InlineKeyboardResponseDTO;
import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;
import com.monora.personalbothub.bot_impl.mapper.InlineKeyboardMapper;
import com.monora.personalbothub.bot_impl.service.InlineKeyboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inline-keyboards")
@RequiredArgsConstructor
public class InlineKeyboardController {

    private final InlineKeyboardService inlineKeyboardService;
    private final InlineKeyboardMapper inlineKeyboardMapper;



}