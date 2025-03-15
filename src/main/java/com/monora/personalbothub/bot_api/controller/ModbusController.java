package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.response.TemperatureResponseDTO;
import com.monora.personalbothub.bot_impl.service.impl.ParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/modbus")
@Slf4j

public class ModbusController {
    private final ParameterService parameterService;

    /**
     * Запись значения в параметр технологической программы (тестовый endpoint)
     */

    // === Твои существующие эндпоинты ===
    public ModbusController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }


    @GetMapping("/temp")
    public ResponseEntity<TemperatureResponseDTO> getTempParameters() {
        var temp = parameterService.readCurrentTemp();
        TemperatureResponseDTO responseDTO = new TemperatureResponseDTO(LocalDateTime.now(), temp);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/observe-temp")
    public ResponseEntity<List<TemperatureResponseDTO>> getObserveTempParameters() {
        List<TemperatureResponseDTO> temperatures = parameterService.getLastTwoDaysTemperatures();
        return ResponseEntity.ok(temperatures);
    }

}