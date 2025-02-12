package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.InlineKeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineKeyboardResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineButtonEntity;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class InlineKeyboardServiceImpl implements InlineKeyboardService {

    private final InlineButtonService inlineButtonService;
    private final InlineKeyboardMapper inlineKeyboardMapper;
    private final InlineKeyboardRepository inlineKeyboardRepository;
    private final InlineButtonRepository inlineButtonRepository;


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

        // Если есть кнопки, создаем их и привязываем к клавиатуре
        if (inlineKeyboardRequestDTO.buttons() != null) {
            List<InlineButtonEntity> buttons = inlineKeyboardRequestDTO.buttons().stream()
                    .map(buttonDTO -> {
                        InlineButtonEntity button = new InlineButtonEntity();
                        button.setText(buttonDTO.text());
                        button.setUrl(buttonDTO.url());
                        button.setCallbackData(buttonDTO.callbackData());
                        button.setSwitchInlineQuery(buttonDTO.switchInlineQuery());
                        button.setRow(buttonDTO.row());
                        button.setPosition(buttonDTO.position());
                        button.setInlineKeyboard(inlineKeyboardEntity); // Привязываем кнопку к клавиатуре
                        return button;
                    })
                    .toList();

            inlineKeyboardEntity.setButtons((Set<InlineButtonEntity>) buttons);
        }

        // Сохраняем клавиатуру (кнопки сохранятся каскадно)
        return inlineKeyboardRepository.save(inlineKeyboardEntity);
    }



    @Override
    @Transactional
    public InlineKeyboardEntity update(InlineKeyboardRequestDTO inlineKeyboardRequestDTO) {
        // Проверка существования клавиатуры
        InlineKeyboardEntity existingKeyboard = inlineKeyboardRepository.findById(inlineKeyboardRequestDTO.id())
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Keyboard not found"));

        // Обновление свойств клавиатуры
        existingKeyboard.setInlineKeyboardName(inlineKeyboardRequestDTO.inlineKeyboardName());

        // Удаляем старые кнопки
        existingKeyboard.getButtons().clear();

        // Добавляем новые кнопки
        if (inlineKeyboardRequestDTO.buttons() != null) {
            Set<InlineButtonEntity> buttons = (Set<InlineButtonEntity>) inlineKeyboardRequestDTO.buttons().stream()
                    .map(buttonDTO -> {
                        InlineButtonEntity button = new InlineButtonEntity();
                        button.setText(buttonDTO.text());
                        button.setUrl(buttonDTO.url());
                        button.setCallbackData(buttonDTO.callbackData());
                        button.setSwitchInlineQuery(buttonDTO.switchInlineQuery());
                        button.setRow(buttonDTO.row());
                        button.setPosition(buttonDTO.position());
                        button.setInlineKeyboard(existingKeyboard); // Привязываем кнопку к клавиатуре
                        return button;
                    })
                    .toList();

            existingKeyboard.setButtons(buttons);
        }

        // Сохраняем обновленную клавиатуру
        return inlineKeyboardRepository.save(existingKeyboard);
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
            InlineKeyboardButton[][] buttonRows = inlineButtonService.getInlineButtonRowByKeyboardId(keyboard.getId());

            log.info("keyboard: {}", keyboard.getId());
            log.info("Buttons: {}", Arrays.deepToString(buttonRows));

            return new InlineKeyboardMarkup(buttonRows);
        }

        log.warn("No inline keyboard found for commandId: {}", commandId);
        return null;  // Или выбросить исключение, если требуется
    }

}
