package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_impl.service.TechProgramReadParameterService;
import com.monora.personalbothub.bot_impl.service.TechProgramWriteParameterService;
import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/api/firing-management")
@AllArgsConstructor
public class FiringManagementController {

    private final TechProgramWriteParameterService writeService;
    private final TechProgramReadParameterService readService;

    @PostMapping("/start-stop")
    public ResponseEntity<Void> startStopManagement(@RequestParam("start") boolean start,
                                                    @RequestParam(value = "unitId", defaultValue = "16") int unitId) {
        writeService.startStopTechProgram(start, unitId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/select-program")
    public ResponseEntity<Void> selectProgram(@RequestParam("programNumber") int programNumber,
                                              @RequestParam(value = "unitId", defaultValue = "16") int unitId) {
        writeService.selectTechProgram(programNumber, unitId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<String> getDeviceStatus(@RequestParam(value = "unitId", defaultValue = "16") int unitId) {
        try {
            // Вызов асинхронного метода
            CompletableFuture<FiringStatus> futureStatus = readService.readStatusDevice(unitId);

            // Блокировка и ожидание результата
            FiringStatus status = futureStatus.get();

            return ResponseEntity.ok(status.getDescription());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при получении статуса устройства: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/current-program")
    public ResponseEntity<Integer> getCurrentDeviceProgram(@RequestParam(value = "unitId", defaultValue = "16") int unitId) {
        try {
            CompletableFuture<Optional<Integer>> future = readService.readProgramNumberDevice(unitId);
            Integer programNumber = future.get()
                    .orElseThrow(() -> new RuntimeException("Программа не найдена"));

            return ResponseEntity.ok(programNumber);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при получении программы устройства: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            log.error("Программа не найдена для unitId: {}", unitId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1);
        }
    }

    @GetMapping("/current-power")
    public ResponseEntity<Integer> getCurrentPowerDevice(@RequestParam(value = "unitId", defaultValue = "16") int unitId) {
        try {
            CompletableFuture<Optional<Integer>> future = readService.readCurrentPowerDevice(unitId);
            Integer programNumber = future.get()
                    .orElseThrow(() -> new RuntimeException("Программа не найдена"));

            return ResponseEntity.ok(programNumber);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при получении мощности устройства: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            log.error("Программа не найдена для unitId: {}", unitId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1);
        }
    }




}