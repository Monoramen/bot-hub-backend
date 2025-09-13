package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.response.TemperatureDeviceResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.TemperatureResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import com.monora.personalbothub.bot_impl.service.impl.TemperatureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/runtime")
@Slf4j
@AllArgsConstructor
public class TemperatureController {
    private final TemperatureService temperatureService;



    @GetMapping("/temp")
    public ResponseEntity<TemperatureDeviceResponseDTO> getObserveTempParameters(
            @RequestParam(value = "unitId", defaultValue = "16") int unitId
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<Float> future = temperatureService.readCurrentDeviceTemp(unitId);
        Float temperatures = future.get();
        TemperatureDeviceResponseDTO responseDTO = new TemperatureDeviceResponseDTO(LocalDateTime.now(), temperatures);
        return ResponseEntity.ok(responseDTO);
    }




    // GET /api/temperature/after?time=2025-04-05T10:00:00&page=0&size=50
    @GetMapping("/after")
    public ResponseEntity<Page<TemperatureEntity>> getTemperaturesAfter(
            @RequestParam LocalDateTime time,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<TemperatureEntity> temps = temperatureService.getTemperaturesAfter(time, page, size);
        return ResponseEntity.ok(temps);
    }

    @GetMapping // ← просто /api/runtime
    public ResponseEntity<List<TemperatureResponseDTO>> getTemperaturesAfter(
            @RequestParam(name = "sessionId", defaultValue = "0") long sessionId
    ) {
        List<TemperatureResponseDTO> temps = temperatureService.getTemperaturesBySessionId(sessionId);
        return ResponseEntity.ok(temps);
    }
}