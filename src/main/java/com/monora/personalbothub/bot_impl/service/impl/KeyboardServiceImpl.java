package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.ButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.request.KeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.KeyboardResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.ButtonEntity;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import com.monora.personalbothub.bot_db.repository.KeyboardRepository;
import com.monora.personalbothub.bot_impl.mapper.KeyboardMapper;
import com.monora.personalbothub.bot_impl.service.ButtonService;
import com.monora.personalbothub.bot_impl.service.KeyboardResult;
import com.monora.personalbothub.bot_impl.service.KeyboardService;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class KeyboardServiceImpl implements KeyboardService {

    private final ButtonService buttonService;
    private final KeyboardMapper keyboardMapper;
    private final KeyboardRepository keyboardRepository;


    @Override
    @Transactional
    public KeyboardEntity create(KeyboardRequestDTO keyboardRequestDTO) {
        if (keyboardRepository.findByKeyboardName(keyboardRequestDTO.keyboardName()).isPresent()) {
            throw new ApiException(ApiErrorType.BAD_REQUEST, "Keyboard already exists");
        }

        // Создаем клавиатуру
        KeyboardEntity keyboardEntity = new KeyboardEntity();
        keyboardEntity.setKeyboardName(keyboardRequestDTO.keyboardName());

        // Сохраняем клавиатуру (без кнопок)
        keyboardEntity = keyboardRepository.save(keyboardEntity);

        // Добавляем кнопки
        if (keyboardRequestDTO.buttons() != null) {
            Set<ButtonEntity> buttons = new HashSet<>();
            for (ButtonRequestDTO buttonDTO : keyboardRequestDTO.buttons()) {
                ButtonEntity button = buttonService.create(buttonDTO, keyboardEntity);
                buttons.add(button);
            }
            keyboardEntity.setButtons(buttons);
        }

        // Сохраняем клавиатуру с кнопками
        return keyboardRepository.save(keyboardEntity);
    }

    @Override
    @Transactional
    public KeyboardEntity update(Long id, KeyboardRequestDTO keyboardRequestDTO) {
        KeyboardEntity existingKeyboard = keyboardRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Keyboard not found"));

        // Обновление свойств клавиатуры
        existingKeyboard.setKeyboardName(keyboardRequestDTO.keyboardName());

        // Удаляем кнопки, которые больше не нужны
        Set<ButtonEntity> existingButtons = existingKeyboard.getButtons();
        Set<Long> newButtonIds = keyboardRequestDTO.buttons().stream()
                .map(ButtonRequestDTO::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingButtons.removeIf(button -> !newButtonIds.contains(button.getId()));

        // Обновляем или добавляем новые кнопки
        if (keyboardRequestDTO.buttons() != null) {
            for (ButtonRequestDTO buttonDTO : keyboardRequestDTO.buttons()) {
                if (buttonDTO.id() != null) {
                    // Используем ButtonService для обновления кнопок
                    buttonService.update(buttonDTO);
                } else {
                    // Используем ButtonService для создания новых кнопок
                    ButtonEntity newButton = buttonService.create(buttonDTO, existingKeyboard);
                    existingButtons.add(newButton);
                }
            }
        }

        // Сохраняем обновленную клавиатуру
        return keyboardRepository.save(existingKeyboard);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        KeyboardEntity keyboardEntity = keyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Keyboard not found"));
        keyboardRepository.delete(keyboardEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public KeyboardResponseDTO findById(Long id) {
        KeyboardEntity keyboardEntity = keyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Keyboard not found"));
        return keyboardMapper.toResponse(keyboardEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<KeyboardResponseDTO> findAll() {
        List<KeyboardEntity> keyboardEntities = keyboardRepository.findAll();
        if (keyboardEntities.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "No inline keyboard found");
        }
        return keyboardMapper.toResponseList(keyboardEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public KeyboardResult getKeyboardByCommandId(Long commandId) {
        boolean autoRemove = false;
        Optional<KeyboardEntity> optionalKeyboard = keyboardRepository.findByCommandId(commandId);

        if (optionalKeyboard.isPresent()) {
            KeyboardEntity keyboard = optionalKeyboard.get();
            KeyboardButton[][] buttonRows = buttonService.getButtonRowByKeyboardId(keyboard.getId());
            log.info("keyboard: {}", keyboard.getId());
            log.info("Buttons: {}", Arrays.deepToString(buttonRows));
            if (keyboard.isAutoRemove()) {
                autoRemove = true;
            }

            Keyboard replyKeyboard = new ReplyKeyboardMarkup(buttonRows)
                    .resizeKeyboard(keyboard.getResizeKeyboard())
                    .oneTimeKeyboard(keyboard.getOneTimeKeyboard());
            return new KeyboardResult(replyKeyboard, autoRemove);
        }
        log.warn("No keyboard found for commandId: {}", commandId);
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public KeyboardEntity getById(Long id) {
        KeyboardEntity keyboardEntity = keyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline keyboard not found"));
        return keyboardEntity;
    }
}
