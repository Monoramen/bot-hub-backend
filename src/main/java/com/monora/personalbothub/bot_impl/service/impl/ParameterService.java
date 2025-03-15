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
public class ParameterService {

    private final ModbusClientReader modbusClientReader;
    private final TemperatureRepository temperatureRepository;
    private final TemperatureMapper temperatureMapper;
    private final TechProgramReadParameterService readService;



    @Scheduled(fixedRate = 4 * 60 * 1000) // каждые 4 минуты (или чаще, если нужно)
    public void scheduleTemperatureReading() {
        try {
            int unitId = 16; // или вынеси в конфиг
            CompletableFuture<FiringStatus> future = readService.readStatusDevice(unitId);
            FiringStatus status = future.get();
            // Сохраняем температуру ТОЛЬКО если статус "Режим Работа"
            if (status == FiringStatus.RUNNING) {
                float temperature = modbusClientReader.readTemperature();

                // Проверка на валидность температуры (опционально)
                if (temperature > 0f && temperature < 1400.0f) { // физически возможный диапазон
                    TemperatureEntity temp = new TemperatureEntity();
                    temp.setTemperature(temperature);
                    temp.setTimestamp(LocalDateTime.now().withSecond(0).withNano(0));
                    temperatureRepository.save(temp);
                    log.info("Температура сохранена при активном обжиге: {}°C", temperature);
                } else {
                    log.warn("Прочитана некорректная температура: {}°C", temperature);
                }
            } else {
                log.debug("Обжиг не активен (статус: {}). Температура не сохраняется.", status);
            }

        } catch (Exception e) {
            log.error("Ошибка при попытке сохранения температуры: {}", e.getMessage(), e);
        }
    }




    @Async("modbusExecutor")
    public float readCurrentTemp() {
         return modbusClientReader.readTemperature();
    }

    public List<TemperatureResponseDTO> getLastTwoDaysTemperatures() {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2).withSecond(0).withNano(0);
        List<TemperatureEntity> temps = temperatureRepository.findByTimestampAfter(twoDaysAgo);
        return temperatureMapper.toResponseDTOList(temps);
    }

}