package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.response.TemperatureResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import com.monora.personalbothub.bot_db.repository.TemperatureRepository;
import com.monora.personalbothub.bot_impl.mapper.TemperatureMapper;
import com.monora.personalbothub.bot_impl.service.TechProgramReadParameterService;
import com.monora.personalbothub.bot_impl.util.modbus.ModbusClientReader;
import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j

@RequiredArgsConstructor
public class TemperatureService {

    private final ModbusClientReader modbusClientReader;
    private final TemperatureRepository temperatureRepository;
    private final TemperatureMapper temperatureMapper;
    private final TechProgramReadParameterService readService;

    @Async("modbusExecutor")
    public CompletableFuture<Float> readCurrentDeviceTemp(int unitId) {
        float temperature = modbusClientReader.readTemperature(unitId);
        return CompletableFuture.completedFuture(temperature);
    }

    // Получить температуры после указанного времени
    public Page<TemperatureEntity> getTemperaturesAfter(LocalDateTime time, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        return temperatureRepository.findByTimestampAfter(time, pageable);
    }


    @Transactional(readOnly = true)
    public List<TemperatureResponseDTO> getTemperaturesBySessionId(long sessionId) {
        List<TemperatureEntity> entities = temperatureRepository.findBySessionId(sessionId);
        return temperatureMapper.toResponseDTOList(entities); // ← используем маппер
    }

}