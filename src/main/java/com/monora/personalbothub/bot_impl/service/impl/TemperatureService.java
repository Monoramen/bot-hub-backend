package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.response.TemperatureResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import com.monora.personalbothub.bot_db.repository.TemperatureRepository;
import com.monora.personalbothub.bot_impl.mapper.TemperatureMapper;
import com.monora.personalbothub.bot_impl.service.TechProgramReadParameterService;
import com.monora.personalbothub.bot_impl.util.modbus.ModbusClientReader;
import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Transactional
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

    public List<TemperatureResponseDTO> getLastTwoDaysTemperatures() {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2).withSecond(0).withNano(0);
        List<TemperatureEntity> temps = temperatureRepository.findByTimestampAfter(twoDaysAgo);
        return temperatureMapper.toResponseDTOList(temps);
    }



}