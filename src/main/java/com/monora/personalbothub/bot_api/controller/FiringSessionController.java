package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.response.FiringProgramResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.FiringSessionResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.SessionDataResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.FiringProgramHistoryEntity;
import com.monora.personalbothub.bot_db.repository.FiringProgramHistoryRepository;
import com.monora.personalbothub.bot_impl.service.FiringProgramService;
import com.monora.personalbothub.bot_impl.service.impl.FiringSessionService;
import com.monora.personalbothub.bot_impl.service.impl.TemperatureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/session")
@Slf4j
@AllArgsConstructor
public class FiringSessionController {
    private final TemperatureService temperatureService;
    private final FiringSessionService firingSessionService;


    /**
     * Запись значения в параметр технологической программы (тестовый endpoint)
     */

    @PostMapping
    public ResponseEntity<FiringProgramResponseDTO> createFiringSession(
            @RequestParam(value = "unitId", defaultValue = "16") int unitId,
            @RequestParam("programNumber") int programNumber) throws ExecutionException, InterruptedException {

        log.info("Received request to create firing program: {}", programNumber);

        FiringProgramResponseDTO created = firingSessionService.createNewSession(programNumber, unitId);
        return ResponseEntity.ok(created);
    }

    @PostMapping("{sessionId}/start")
    public ResponseEntity<FiringProgramResponseDTO> startFiringSession(
            @PathVariable("sessionId") long sessionId) throws ExecutionException, InterruptedException {

        log.info("Received request to create firing program: {}", sessionId);

        return ResponseEntity.ok(firingSessionService.startSession(sessionId));

    }


    @GetMapping("/recent")
    public ResponseEntity<List<FiringSessionResponseDTO>> getRecentSessions(
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        List<FiringSessionResponseDTO> dtos = firingSessionService.getRecentSessions(limit);
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{sessionId}")
    public ResponseEntity<FiringSessionResponseDTO> getSessionById(
            @PathVariable("sessionId") long sessionId) {
        FiringSessionResponseDTO session = firingSessionService.getSessionById(sessionId);
        return ResponseEntity.ok(session);
    }
    @GetMapping("/{sessionId}/data")
    public ResponseEntity<SessionDataResponseDTO> getSessionData(
            @PathVariable("sessionId") Long sessionId) {
        log.info("Fetching full session data for session ID: {}", sessionId);

        SessionDataResponseDTO sessionData = firingSessionService.getSessionDataDTO(sessionId);
        return ResponseEntity.ok(sessionData);
    }


    @GetMapping("/program/{sessionId}/data")
    public ResponseEntity<FiringProgramResponseDTO> getSessionProgramData(
            @PathVariable("sessionId") Long sessionId) {
        log.info("Fetching full session data for session ID: {}", sessionId);
        FiringProgramResponseDTO programData = firingSessionService.getProgramDataForSession(sessionId);
        return ResponseEntity.ok(programData);
    }

}