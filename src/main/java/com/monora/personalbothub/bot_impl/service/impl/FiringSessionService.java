package com.monora.personalbothub.bot_impl.service.impl;
import com.monora.personalbothub.bot_api.dto.response.FiringSessionResponseDTO;
import com.monora.personalbothub.bot_db.entity.modbus.FiringProgramEntity;
import com.monora.personalbothub.bot_db.entity.modbus.FiringProgramHistoryEntity;
import com.monora.personalbothub.bot_db.entity.modbus.FiringSessionEntity;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import com.monora.personalbothub.bot_db.repository.FiringSessionRepository;
import com.monora.personalbothub.bot_impl.mapper.FiringSessionMapper;
import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FiringSessionService {

    private final FiringSessionRepository firingSessionRepository;
    private final FiringSessionMapper firingSessionMapper;

    /**
     * Создает и сохраняет новый сеанс обжига.
     * @param program Программа обжига, которая будет выполняться.
     * @return Созданный объект FiringSessionEntity.
     */
    @Transactional
    public FiringSessionEntity startNewSession(FiringProgramHistoryEntity program) {
        FiringSessionEntity session = new FiringSessionEntity();
        session.setProgram(program);
        session.setStartTime(LocalDateTime.now());
        session.setStatus(FiringStatus.RUNNING);
        log.info("Starting new firing session for program: {}", program.getName());
        return firingSessionRepository.save(session);
    }

    /**
     * Записывает новое показание температуры для текущего сеанса.
     * @param sessionId ID текущего сеанса.
     * @param temperatureValue Значение температуры.
     * @return Обновленный объект FiringSessionEntity, если найден.
     */
    @Transactional
    public Optional<FiringSessionEntity> recordTemperature(Long sessionId, float temperatureValue) {
        return firingSessionRepository.findById(sessionId)
                .map(session -> {
                    TemperatureEntity tempReading = new TemperatureEntity();
                    tempReading.setTemperature(temperatureValue);
                    tempReading.setTimestamp(LocalDateTime.now());
                    tempReading.setSession(session);
                    session.getTemperatureReadings().add(tempReading);

                    // Обновляем максимальную температуру
                    if (session.getMaxRecordedTemperature() == null || temperatureValue > session.getMaxRecordedTemperature()) {
                        session.setMaxRecordedTemperature((double) temperatureValue);
                    }
                    log.debug("Recorded temperature {}°C for session {}", temperatureValue, sessionId);
                    return firingSessionRepository.save(session);
                });
    }

    /**
     * Завершает сеанс обжига, обновляя его статус и продолжительность.
     * @param sessionId ID завершаемого сеанса.
     * @param status Статус завершения (COMPLETED, FAILED, etc.).
     * @return Обновленный объект FiringSessionEntity, если найден.
     */
    @Transactional
    public Optional<FiringSessionEntity> endSession(Long sessionId, FiringStatus status) {
        return firingSessionRepository.findById(sessionId)
                .map(session -> {
                    session.setEndTime(LocalDateTime.now());
                    session.setStatus(status);

                    long duration = ChronoUnit.MINUTES.between(session.getStartTime(), session.getEndTime());
                    session.setActualDurationMinutes((int) duration);
                    log.info("Ending session {} with status {}. Actual duration: {} minutes", sessionId, status, duration);

                    return firingSessionRepository.save(session);
                });
    }

    /**
     * Получает все данные о сеансе по его ID.
     * @param sessionId ID сеанса.
     * @return DTO с полной информацией о сеансе, включая показания температуры.
     */
    @Transactional(readOnly = true)
    public Optional<FiringSessionResponseDTO> getSessionData(Long sessionId) {
        return firingSessionRepository.findById(sessionId)
                .map(firingSessionMapper::toDto);
    }
}