package com.monora.personalbothub.bot_impl.service.impl;
import com.monora.personalbothub.bot_api.dto.response.*;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.modbus.FiringProgramHistoryEntity;
import com.monora.personalbothub.bot_db.entity.modbus.FiringSessionEntity;
import com.monora.personalbothub.bot_db.entity.modbus.TemperatureEntity;
import com.monora.personalbothub.bot_db.repository.FiringProgramHistoryRepository;
import com.monora.personalbothub.bot_db.repository.FiringSessionRepository;
import com.monora.personalbothub.bot_impl.mapper.FiringProgramMapper;
import com.monora.personalbothub.bot_impl.mapper.FiringSessionMapper;
import com.monora.personalbothub.bot_impl.service.FiringProgramService;
import com.monora.personalbothub.bot_impl.service.TechProgramReadParameterService;
import com.monora.personalbothub.bot_impl.service.TechProgramWriteParameterService;
import com.monora.personalbothub.bot_impl.util.modbus.enums.FiringStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FiringSessionService {

    private final FiringSessionRepository firingSessionRepository;
    private final FiringProgramHistoryRepository firingProgramHistoryRepository;
    private final TechProgramReadParameterService readParameterService;
    private final FiringProgramService firingProgramService;
    private final FiringSessionMapper firingSessionMapper;
    private final FiringProgramMapper firingProgramMapper;
    private final TemperatureService temperatureService;
    /**
     * Создает и сохраняет новый сеанс обжига.
     * @param programNumber Программа обжига, которая будет выполняться.
     * @return Созданный объект FiringSessionEntity.
     */
    @Transactional
    public FiringProgramResponseDTO createNewSession(int programNumber, int unitId) throws ExecutionException, InterruptedException {
        FiringProgramResponseDTO programFromDevice = firingProgramService.getOneTechProgramParametersAsFiringProgramDTO(programNumber, unitId);

        FiringProgramHistoryEntity entity = firingProgramMapper.toEntityHistory(programFromDevice);

        firingProgramHistoryRepository.save(entity);

        CompletableFuture<FiringStatus> future = readParameterService.readStatusDevice(unitId);
        FiringStatus status = future.get();

        FiringSessionEntity session = new FiringSessionEntity();
        session.setProgram(entity);
        session.setStartTime(LocalDateTime.now());
        session.setStatus(status);
        log.info("Starting new firing session for program: {}", programNumber);
        firingSessionRepository.save(session);
        firingSessionRepository.save(session);
        return firingProgramMapper.toResponseHistoryDTO(entity);
    }


    @Transactional
    public FiringProgramResponseDTO startSession(Long sessionId) throws ExecutionException, InterruptedException {
        FiringSessionEntity session = firingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Firing program not found"));
        CompletableFuture<FiringStatus> future = readParameterService.readStatusDevice(16);
        FiringStatus status = future.get();
        session.setStatus(status);
        return firingSessionMapper.toDto(session).program();
    }


    @Transactional
    public Optional<FiringSessionEntity> endSession(Long sessionId, FiringStatus status) {

        return firingSessionRepository.findById(sessionId)
                .map(session -> {
                    session.setEndTime(LocalDateTime.now());
                    session.setStatus(status); // обновляем статус на фактический (например, COMPLETED, FAILED и т.д.)

                    long duration = ChronoUnit.MINUTES.between(session.getStartTime(), session.getEndTime());
                    session.setActualDurationMinutes((int) duration);

                    log.info("⏹️ Завершение сессии {} со статусом {}. Длительность: {} мин", sessionId, status, duration);
                    return firingSessionRepository.save(session);
                });
    }



    @Scheduled(fixedRate = 60 * 1000) // каждую минуту
    @Transactional
    public void monitorRunningSessions() {
        log.debug("🔍 Проверка сессий со статусом RUNNING...");

        List<FiringSessionEntity> runningSessions = firingSessionRepository.findByStatus(FiringStatus.RUNNING);

        if (runningSessions.isEmpty()) {
            log.debug("Нет сессий в статусе RUNNING для мониторинга.");
            return;
        }
        int unitId = 16; // можно вынести в конфиг или хранить в сессии

        for (FiringSessionEntity session : runningSessions) {
            try {
                // Читаем текущий статус УСТРОЙСТВА
                CompletableFuture<FiringStatus> future = readParameterService.readStatusDevice(unitId);
                FiringStatus deviceStatus = future.get();

                if (FiringStatus.RUNNING.equals(deviceStatus)) {
                    // Устройство всё ещё в режиме работы — записываем температуру
                    recordTemperatureForSession(session, unitId);
                } else {
                    // Устройство вышло из режима работы — завершаем сессию
                    endSession(session.getId(), deviceStatus);
                    log.info("✅ Сессия {} завершена. Новый статус устройства: {}", session.getId(), deviceStatus);
                }
            } catch (Exception e) {
                log.error("❌ Ошибка при мониторинге сессии {}: {}", session.getId(), e.getMessage(), e);
            }
        }
    }

    @Transactional(readOnly = true)
    public Optional<FiringSessionEntity> getCurrentRunningSession() {
        return firingSessionRepository.findFirstByStatusOrderByStartTimeDesc(FiringStatus.RUNNING);
    }


    @Transactional(readOnly = true)
    public List<FiringSessionResponseDTO> getRecentSessions(int limit) {
        List<FiringSessionEntity> entities = (List<FiringSessionEntity>) firingSessionRepository.findTopNByOrderByStartTimeDesc(limit);
        return entities.stream()
                .map(firingSessionMapper::toListItemDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public FiringSessionResponseDTO getSessionById(Long sessionId) {
        FiringSessionEntity entity = firingSessionRepository.findById(sessionId).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Firing session not found")
        );
        return firingSessionMapper.toDto(entity);
    }


    private void recordTemperatureForSession(FiringSessionEntity session, int unitId)
            throws ExecutionException, InterruptedException {

        CompletableFuture<Float> future = temperatureService.readCurrentDeviceTemp(unitId);
        Float temperatureValue = future.get();

        if (temperatureValue == null) {
            log.warn("Не удалось прочитать температуру для сессии {}", session.getId());
            return;
        }

        // Создаём новое показание температуры
        TemperatureEntity tempReading = new TemperatureEntity();
        tempReading.setTemperature(temperatureValue);
        tempReading.setTimestamp(LocalDateTime.now().withSecond(0).withNano(0));
        tempReading.setSession(session);

        // Обновляем максимальную температуру в сессии
        if (session.getMaxRecordedTemperature() == null || temperatureValue > session.getMaxRecordedTemperature()) {
            session.setMaxRecordedTemperature((double) temperatureValue);
        }

        // Сохраняем показание и обновляем сессию
        session.getTemperatureReadings().add(tempReading);
        firingSessionRepository.save(session);

        log.debug("🌡️ Записана температура {}°C для сессии {}", temperatureValue, session.getId());
    }


    // Получить сессию с программой
    public Optional<FiringSessionEntity> getSessionWithProgram(Long sessionId) {
        return firingSessionRepository.findWithProgramById(sessionId);
    }

    // Получить все показания температуры
    public List<TemperatureEntity> getAllTemperatureReadings(Long sessionId) {
        return firingSessionRepository.findTemperatureReadingsBySessionId(sessionId);
    }

    @Transactional(readOnly = true)
    public SessionDataResponseDTO getSessionDataDTO(Long sessionId) {
        FiringSessionEntity session = getSessionWithProgram(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));

        List<TemperatureEntity> allTemps = getAllTemperatureReadings(sessionId);
        // latestTemp не нужен — мы найдём его сами из allTemps

        // 1. Получаем базовый DTO от MapStruct
        SessionDataResponseDTO baseDto = firingSessionMapper.toSessionDataDto(session, allTemps);

        // 2. Дорабатываем кастомные поля
        Double maxTemp = firingSessionMapper.calculateMaxTemperature(allTemps);
        TemperatureResponseDTO latestTemp = firingSessionMapper.findLatestValidTemperature(allTemps);

        // 3. Создаём финальный DTO (если record не позволяет — создай билдер или изменяемый класс)
        return new SessionDataResponseDTO(
                baseDto.id(),
                baseDto.program(),
                baseDto.startTime(),
                baseDto.endTime(),
                baseDto.status(),
                baseDto.actualDurationMinutes(),
                maxTemp,
                baseDto.temperatureReadings(), // ← уже отфильтровано и замаплено MapStruct
                latestTemp
        );
    }
    @Transactional(readOnly = true)
    public FiringProgramResponseDTO getProgramDataForSession(Long sessionId) {
        FiringProgramHistoryEntity program  = firingProgramHistoryRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Session not found: " + sessionId));
        return firingProgramMapper.toResponseHistoryDTO(program);
    }


}