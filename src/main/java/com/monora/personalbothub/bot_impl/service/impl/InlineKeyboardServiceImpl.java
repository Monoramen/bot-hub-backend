package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.ButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.request.InlineButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.request.InlineKeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineKeyboardResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineButtonEntity;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.ButtonEntity;
import com.monora.personalbothub.bot_db.repository.InlineButtonRepository;
import com.monora.personalbothub.bot_db.repository.InlineKeyboardRepository;
import com.monora.personalbothub.bot_impl.mapper.InlineKeyboardMapper;
import com.monora.personalbothub.bot_impl.service.InlineButtonService;
import com.monora.personalbothub.bot_impl.service.InlineKeyboardService;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor

public class InlineKeyboardServiceImpl implements InlineKeyboardService {

    private final InlineButtonService inlineButtonService;
    private final InlineKeyboardMapper inlineKeyboardMapper;
    private final InlineKeyboardRepository inlineKeyboardRepository;


    @Override
    @Transactional
    public InlineKeyboardEntity create(InlineKeyboardRequestDTO inlineKeyboardRequestDTO) {
        // Проверка на существование клавиатуры с таким именем
        if (inlineKeyboardRepository.findByInlineKeyboardName(inlineKeyboardRequestDTO.inlineKeyboardName()).isPresent()) {
            throw new ApiException(ApiErrorType.BAD_REQUEST, "Keyboard already exists");
        }
        // Создаем клавиатуру
        InlineKeyboardEntity inlineKeyboardEntity = new InlineKeyboardEntity();
        inlineKeyboardEntity.setInlineKeyboardName(inlineKeyboardRequestDTO.inlineKeyboardName());

        inlineKeyboardEntity = inlineKeyboardRepository.save(inlineKeyboardEntity);


        if (inlineKeyboardRequestDTO.buttons() != null) {
            Set<InlineButtonEntity> buttons = new HashSet<>();
            for (InlineButtonRequestDTO buttonDTO : inlineKeyboardRequestDTO.buttons()) {
                InlineButtonEntity button = inlineButtonService.create(inlineKeyboardEntity, buttonDTO);
                buttons.add(button);
            }
            inlineKeyboardEntity.setButtons(buttons);
        }


        // Сохраняем клавиатуру (кнопки сохранятся каскадно)
        return inlineKeyboardRepository.save(inlineKeyboardEntity);
    }



    @Override
    @Transactional
    public InlineKeyboardEntity update(Long id, InlineKeyboardRequestDTO inlineKeyboardRequestDTO) {
        // Проверка существования клавиатуры
        InlineKeyboardEntity existingKeyboard = inlineKeyboardRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Keyboard not found"));

        // Обновление свойств клавиатуры
        existingKeyboard.setInlineKeyboardName(inlineKeyboardRequestDTO.inlineKeyboardName());

        // Удаляем старые кнопки
        existingKeyboard.getButtons().clear();

        Set<InlineButtonEntity> existingButtons = existingKeyboard.getButtons();
        Set<Long> newButtonIds = inlineKeyboardRequestDTO.buttons().stream()
                .map(InlineButtonRequestDTO::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingButtons.removeIf(button -> !newButtonIds.contains(button.getId()));

        // Обновляем или добавляем новые кнопки
        if (inlineKeyboardRequestDTO.buttons() != null) {
            for (InlineButtonRequestDTO buttonDTO : inlineKeyboardRequestDTO.buttons()) {
                if (buttonDTO.id() != null) {
                    // Используем ButtonService для обновления кнопок
                    inlineButtonService.update(buttonDTO);
                } else {
                    // Используем ButtonService для создания новых кнопок
                    InlineButtonEntity newButton = inlineButtonService.create(existingKeyboard, buttonDTO);
                    existingButtons.add(newButton);
                }
            }
        }

        // Сохраняем обновленную клавиатуру
        return inlineKeyboardRepository.save(existingKeyboard);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        InlineKeyboardEntity inlineKeyboardEntity = inlineKeyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline keyboard not found"));
        inlineKeyboardRepository.delete(inlineKeyboardEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public InlineKeyboardResponseDTO findById(Long id) {
        InlineKeyboardEntity inlineKeyboardEntity = inlineKeyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline keyboard not found"));
        return inlineKeyboardMapper.toResponse(inlineKeyboardEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InlineKeyboardResponseDTO> findAll() {
        List<InlineKeyboardEntity> inlineKeyboardEntities = inlineKeyboardRepository.findAll();
        if (inlineKeyboardEntities.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "No inline keyboard found");
        }
        return inlineKeyboardMapper.toResponseList(inlineKeyboardEntities);
    }

    public InlineKeyboardMarkup getInlineKeyboardByCommandId(Long commandId) {
        Optional<InlineKeyboardEntity> optionalKeyboard = inlineKeyboardRepository.findByCommandId(commandId);

        if (optionalKeyboard.isPresent()) {
            InlineKeyboardEntity keyboard = optionalKeyboard.get();
            InlineKeyboardButton[][] buttonRows = inlineButtonService.getInlineButtonRowByKeyboardId(keyboard.getId());

            log.info("keyboard: {}", keyboard.getId());
            log.info("Buttons: {}", Arrays.deepToString(buttonRows));

            return new InlineKeyboardMarkup(buttonRows);
        }

        log.warn("No inline keyboard found for commandId: {}", commandId);
        return null;  // Или выбросить исключение, если требуется
    }

    @Override
    @Transactional(readOnly = true)
    public InlineKeyboardEntity getById(Long id) {
        InlineKeyboardEntity inlineKeyboardEntity = inlineKeyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline keyboard not found"));
        return inlineKeyboardEntity;
    }
}
