package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_api.dto.request.FiringProgramRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.FiringProgramResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_impl.service.FiringProgramService;
import com.monora.personalbothub.bot_impl.service.TechProgramWriteParameterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/firing-programs")
@AllArgsConstructor
public class FiringProgramController {

    private final FiringProgramService firingProgramService;
    private final TechProgramWriteParameterService programWriterService;


    /**
     * Получение программы обжига по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<FiringProgramResponseDTO> getFiringProgramById(@PathVariable("id") Integer id) {
        log.info("Fetching firing program by ID: {}", id);
        FiringProgramResponseDTO response = firingProgramService.findById(id);
        return ResponseEntity.ok(response);
    }


    /**
     * Получение всех программ обжига
     */
    @GetMapping
    public ResponseEntity<List<FiringProgramResponseDTO>> getAllFiringPrograms() {
        log.info("Fetching all firing programs");
        List<FiringProgramResponseDTO> allPrograms = firingProgramService.findAll();
        return ResponseEntity.ok(allPrograms);
    }

    /**
     * Создание новой программы обжига
     */
    @PostMapping("/add")
    public ResponseEntity<FiringProgramResponseDTO> createFiringProgram(@RequestBody FiringProgramRequestDTO requestDTO) {
        log.info("Received request to create firing program: {}", requestDTO);
        FiringProgramResponseDTO created = firingProgramService.create(requestDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Обновление программы обжига по ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<FiringProgramResponseDTO> updateFiringProgram(@PathVariable("id") Integer id,
                                                                        @RequestBody FiringProgramRequestDTO requestDTO) {
        log.info("Received request to update firing program with ID: {}", id);
        FiringProgramResponseDTO updated = firingProgramService.update(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Удаление программы обжига по ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiringProgram(@PathVariable("id") Integer id) {
        log.info("Received request to delete firing program with ID: {}", id);
        firingProgramService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/device/parameters/all")
    public ResponseEntity<List<FiringProgramResponseDTO>> getAllDeviceParameters(
            @RequestParam(value = "unitId", defaultValue = "16") int unitId) {
        log.info("Fetching all device parameters for unit ID: {}", unitId);
        List<FiringProgramResponseDTO> response = firingProgramService.getAllTechProgramParametersAsFiringProgramDTO(unitId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/device/program/{programNumber}")
    public ResponseEntity<FiringProgramResponseDTO> getProgramParameters(
            @PathVariable("programNumber") Integer programNumber,
            @RequestParam(value = "unitId", defaultValue = "16") int unitId) {



        FiringProgramResponseDTO response = firingProgramService.getOneTechProgramParametersAsFiringProgramDTO(programNumber,unitId);



        return ResponseEntity.ok(response);
    }



    @PostMapping("/{id}/deploy")
    public ResponseEntity<String> deployProgramToDevice(
            @PathVariable("id") Integer id,
            @RequestParam("deviceProgramId") int deviceProgramId, // 1, 2 или 3
            @RequestParam(value = "unitId", defaultValue = "16") int unitId) {

        FiringProgramResponseDTO program = firingProgramService.findById(id);

        // Если программа не найдена, выбрасываем исключение.
        if (program == null) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "Программа с ID " + id + " не найдена.");
        }

        // Просто вызываем метод. Если будет ошибка, он выбросит исключение,
        // которое обработает ваш ExceptionHandlerController.
        programWriterService.writeFiringProgram(program, deviceProgramId, unitId);

        // Если выполнение дошло до этой строки, значит, операция успешна.
        return ResponseEntity.ok("Программа успешно записана в слот P" + deviceProgramId);
    }


}
