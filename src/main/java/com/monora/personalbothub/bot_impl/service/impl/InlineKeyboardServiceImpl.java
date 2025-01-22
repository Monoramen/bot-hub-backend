package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.InlineButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.request.InlineKeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineKeyboardResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.InlineButtonEntity;
import com.monora.personalbothub.bot_db.entity.InlineKeyboardEntity;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class InlineKeyboardServiceImpl implements InlineKeyboardService {

    private final InlineButtonService inlineButtonService;
    private final InlineKeyboardMapper inlineKeyboardMapper;
    private final InlineKeyboardRepository inlineKeyboardRepository;
    private final InlineButtonRepository inlineButtonRepository;




    @Override
    public void create(InlineKeyboardRequestDTO inlineKeyboardRequestDTO) {
        if (inlineKeyboardRepository.findByInlineKeyboardName(inlineKeyboardRequestDTO.inlineKeyboardName()).isPresent()) {
            throw new ApiException(ApiErrorType.BAD_REQUEST, "Keyboard already exists");
        }

        InlineKeyboardEntity inlineKeyboardEntity = inlineKeyboardMapper.toEntity(inlineKeyboardRequestDTO);
        inlineKeyboardRepository.save(inlineKeyboardEntity); // Сначала сохраняем клавиатуру

        // Создаем кнопки и добавляем их к клавиатуре
        if (inlineKeyboardRequestDTO.buttons() != null) {
            for (InlineButtonRequestDTO buttonDTO : inlineKeyboardRequestDTO.buttons()) {
                inlineButtonService.create(buttonDTO);
                // Получаем только что созданную кнопку
                InlineButtonEntity buttonEntity = inlineButtonRepository.findByText(buttonDTO.text())
                        .orElseThrow(() -> new ApiException(ApiErrorType.INTERNAL_SERVER_ERROR, "Button not found after creation"));

                inlineKeyboardEntity.getButtons().add(buttonEntity);
            }
            inlineKeyboardRepository.save(inlineKeyboardEntity);
        }
    }


    @Override
    public void update(InlineKeyboardRequestDTO inlineKeyboardRequestDTO) {
        // Проверка существования клавиатуры
        InlineKeyboardEntity existingKeyboard = inlineKeyboardRepository.findByInlineKeyboardName(inlineKeyboardRequestDTO.inlineKeyboardName())
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Keyboard not found"));

        // Обновление свойств клавиатуры
        existingKeyboard.setInlineKeyboardName(inlineKeyboardRequestDTO.inlineKeyboardName());

        // Обновление кнопок клавиатуры
        if (inlineKeyboardRequestDTO.buttons() != null) {
            // Удаление существующих кнопок
            existingKeyboard.getButtons().clear();

            // Создание новых кнопок и добавление их к клавиатуре
            for (InlineButtonRequestDTO buttonDTO : inlineKeyboardRequestDTO.buttons()) {
                inlineButtonService.create(buttonDTO); // Создаем кнопку

                // Получаем только что созданную кнопку
                InlineButtonEntity buttonEntity = inlineButtonRepository.findByText(buttonDTO.text())
                        .orElseThrow(() -> new ApiException(ApiErrorType.INTERNAL_SERVER_ERROR, "Button not found after creation"));

                existingKeyboard.getButtons().add(buttonEntity);
            }
        }

        // Сохранение обновленной клавиатуры
        inlineKeyboardRepository.save(existingKeyboard);
    }


    @Override
    public void delete(Long id) {
        InlineKeyboardEntity inlineKeyboardEntity = inlineKeyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline keyboard not found"));
        inlineKeyboardRepository.delete(inlineKeyboardEntity);
    }

    @Override
    public InlineKeyboardResponseDTO findById(Long id) {
        InlineKeyboardEntity inlineKeyboardEntity = inlineKeyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline keyboard not found"));
        return inlineKeyboardMapper.toResponse(inlineKeyboardEntity);
    }

    @Override
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
            InlineKeyboardButton[] buttons = inlineButtonService.getInlineButtonRowByKeyboardId(keyboard.getId());

            log.info("keyboard: {}", keyboard.getId());
            log.info("Buttons: {}", buttons);

            return new InlineKeyboardMarkup(buttons);
        }

        log.warn("No inline keyboard found for commandId: {}", commandId);
        return null;  // Или выбросить исключение, если требуется
    }

}
