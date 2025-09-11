package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.response.TemperatureDeviceResponseDTO;
import com.monora.personalbothub.bot_api.dto.response.TemperatureResponseDTO;
import com.monora.personalbothub.bot_impl.service.impl.TemperatureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/observe-temp")
    public ResponseEntity<List<TemperatureResponseDTO>> getObserveTempParameters() {
        List<TemperatureResponseDTO> temperatures = temperatureService.getLastTwoDaysTemperatures();
        return ResponseEntity.ok(temperatures);
    }

}