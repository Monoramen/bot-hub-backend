package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.InlineButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.request.InlineKeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineButtonResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineButtonEntity;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import com.monora.personalbothub.bot_db.repository.InlineButtonRepository;
import com.monora.personalbothub.bot_impl.mapper.InlineButtonMapper;
import com.monora.personalbothub.bot_impl.service.InlineButtonService;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class InlineButtonServiceImpl implements InlineButtonService {

    private final InlineButtonRepository inlineButtonRepository;
    private final InlineButtonMapper inlineButtonMapper;


    @Override
    @Transactional
    public InlineButtonEntity create(InlineKeyboardEntity keyboard, InlineButtonRequestDTO inlineButtonRequestDTO) {
        Optional<InlineButtonEntity> existingButton = inlineButtonRepository.findByText(inlineButtonRequestDTO.text());
        if (existingButton.isPresent()) {
            throw new ApiException(ApiErrorType.BAD_REQUEST, "Inline Button already exists");
        } else {
            InlineButtonEntity inlineButtonEntity = new InlineButtonEntity();
            inlineButtonEntity.setText(inlineButtonRequestDTO.text());
            inlineButtonEntity.setUrl(inlineButtonRequestDTO.url());
            inlineButtonEntity.setSwitchInlineQuery(inlineButtonRequestDTO.switchInlineQuery());
            inlineButtonEntity.setCallbackData(inlineButtonRequestDTO.callbackData());
            inlineButtonEntity.setRow(inlineButtonRequestDTO.row());
            inlineButtonEntity.setPosition(inlineButtonRequestDTO.position());
            inlineButtonEntity.setInlineKeyboard(keyboard);
            return inlineButtonRepository.save(inlineButtonEntity);
        }

    }


    @Override
    @Transactional
    public InlineButtonEntity update(InlineButtonRequestDTO inlineButtonRequestDTO) {
        // Проверка существования кнопки
        InlineButtonEntity existingButton = inlineButtonRepository.findById(inlineButtonRequestDTO.id()).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Button not found")
        );

        existingButton.setText(inlineButtonRequestDTO.text());
        existingButton.setRow(inlineButtonRequestDTO.row());
        existingButton.setPosition(inlineButtonRequestDTO.position());
        existingButton.setUrl(inlineButtonRequestDTO.url());
        existingButton.setCallbackData(inlineButtonRequestDTO.callbackData());
        existingButton.setSwitchInlineQuery(inlineButtonRequestDTO.switchInlineQuery());

        inlineButtonRepository.save(existingButton);
        return existingButton;

    }


    @Override
    @Transactional
    public void delete(Long id) {
        InlineButtonEntity inlineButtonEntity = inlineButtonRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline button with id: " + id + " not found"));
        inlineButtonRepository.delete(inlineButtonEntity);
    }


    @Override
    @Transactional(readOnly = true)
    public InlineButtonResponseDTO findById(Long id) {
        InlineButtonEntity inlineButtonEntity = inlineButtonRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline button with id: " + id + " not found"));
        return inlineButtonMapper.toResponse(inlineButtonEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InlineButtonResponseDTO> findAll() {
        List<InlineButtonEntity> inlineButtonEntities = inlineButtonRepository.findAll();
        if (inlineButtonEntities.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "Inline buttons not found");
        }

        return inlineButtonMapper.toResponseList(inlineButtonEntities);
    }


    @Override
    @Transactional(readOnly = true)
    public InlineKeyboardButton[][] getInlineButtonRowByKeyboardId(Long inlineKeyboardId) {
        List<InlineButtonEntity> buttons = inlineButtonRepository.findAllByInlineKeyboardId(inlineKeyboardId);
        return buildKeyboard(buttons);
    }

    private InlineKeyboardButton[][] buildKeyboard(List<InlineButtonEntity> buttons) {
        if (buttons.isEmpty()) {
            return new InlineKeyboardButton[0][0];
        }

        // Группируем кнопки по рядам
        Map<Integer, List<InlineButtonEntity>> rowMap = buttons.stream()
                .collect(Collectors.groupingBy(InlineButtonEntity::getRow));

        // Создаем двумерный массив кнопок
        InlineKeyboardButton[][] buttonRows = new InlineKeyboardButton[rowMap.size()][];
        int rowIndex = 0;

        for (List<InlineButtonEntity> rowButtons : rowMap.values()) {
            buttonRows[rowIndex] = rowButtons.stream()
                    .sorted(Comparator.comparingInt(InlineButtonEntity::getPosition))
                    .map(button -> {
                        InlineKeyboardButton inlineButton = new InlineKeyboardButton(button.getText());
                        if (button.getUrl() != null) {
                            inlineButton.url(button.getUrl());
                        } else if (button.getCallbackData() != null) {
                            inlineButton.callbackData(button.getCallbackData());
                        } else if (button.getSwitchInlineQuery() != null) {
                            inlineButton.switchInlineQuery(button.getSwitchInlineQuery());
                        }
                        return inlineButton;
                    }).toArray(InlineKeyboardButton[]::new);
            rowIndex++;
        }
        return buttonRows;
    }

    boolean checkButtonType(InlineButtonRequestDTO inlineButtonRequestDTO) {
        long count = Stream.of(
                inlineButtonRequestDTO.url(),
                inlineButtonRequestDTO.callbackData(),
                inlineButtonRequestDTO.switchInlineQuery()
        ).filter(Objects::nonNull).count();

        return count == 1;
    }

}