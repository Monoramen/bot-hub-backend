package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.InlineButtonDto;
import com.monora.personalbothub.bot_api.dto.request.InlineButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.InlineButtonResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.InlineButtonEntity;
import com.monora.personalbothub.bot_db.repository.InlineButtonRepository;
import com.monora.personalbothub.bot_impl.mapper.InlineButtonMapper;
import com.monora.personalbothub.bot_impl.service.InlineButtonService;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InlineButtonServiceImpl implements InlineButtonService {

    private final InlineButtonRepository inlineButtonRepository;
    private final InlineButtonMapper inlineButtonMapper;


    @Override
    public void create(InlineButtonRequestDTO inlineButtonRequestDTO) {
        if (checkButtonType(inlineButtonRequestDTO) == false) {
            throw new ApiException(ApiErrorType.BAD_REQUEST, "The button can to have one of exist type");
        }

        InlineButtonEntity inlineButtonEntity = inlineButtonMapper.toEntity(inlineButtonRequestDTO);
        inlineButtonRepository.save(inlineButtonEntity);
    }



    @Override
    public void update(InlineButtonRequestDTO inlineButtonRequestDTO) {
        // Проверка существования кнопки
        InlineButtonEntity existingButton = inlineButtonRepository.findById(inlineButtonRequestDTO.id()).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Button not found")
        );

        // Проверка типа кнопки
        if (!checkButtonType(inlineButtonRequestDTO)) {
            throw new ApiException(ApiErrorType.BAD_REQUEST, "The button must be one of the following types: URL, callback, or switchInlineQuery");
        }

        // Обновление кнопки
        existingButton.setText(inlineButtonRequestDTO.text());
        existingButton.setUrl(inlineButtonRequestDTO.url());
        existingButton.setCallbackData(inlineButtonRequestDTO.callbackData());
        existingButton.setSwitchInlineQuery(inlineButtonRequestDTO.switchInlineQuery());

        inlineButtonRepository.save(existingButton);
    }


    @Override
    public void delete(Long id) {
        InlineButtonEntity inlineButtonEntity = inlineButtonRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline button with id: " + id + " not found"));
        inlineButtonRepository.delete(inlineButtonEntity);
    }


    @Override
    public InlineButtonResponseDTO findById(Long id) {
        InlineButtonEntity inlineButtonEntity = inlineButtonRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline button with id: " + id + " not found"));
        return inlineButtonMapper.toResponse(inlineButtonEntity);
    }

    @Override
    public List<InlineButtonResponseDTO> findAll() {
        List<InlineButtonEntity> inlineButtonEntitys = inlineButtonRepository.findAll();
        if (inlineButtonEntitys.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "Inline buttons not found");
        }

        return inlineButtonMapper.toResponseList(inlineButtonEntitys);
    }

    @Override
    public InlineKeyboardButton[] getInlineButtonRowByKeyboardId(Long inlineKeyboardId) {
        List<InlineButtonEntity> buttons = inlineButtonRepository.findAllByInlineKeyboardId(inlineKeyboardId);

        if (buttons.isEmpty()) {
            return new InlineKeyboardButton[0]; // Возвращаем пустой массив, если кнопок нет
        }

        InlineKeyboardButton[] buttonRow = new InlineKeyboardButton[buttons.size()];

        for (int i = 0; i < buttons.size(); i++) {
            InlineButtonEntity button = buttons.get(i);
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(button.getText());

            if (button.getUrl() != null) {
                inlineKeyboardButton.url(button.getUrl());
            } else if (button.getCallbackData() != null) {
                inlineKeyboardButton.callbackData(button.getCallbackData());
            } else if (button.getSwitchInlineQuery() != null) {
                inlineKeyboardButton.switchInlineQuery(button.getSwitchInlineQuery());
            }

            buttonRow[i] = inlineKeyboardButton;
        }
        return buttonRow;
    }

    boolean checkButtonType(InlineButtonRequestDTO inlineButtonRequestDTO) {
        int count = 0;
        if ( inlineButtonRequestDTO.url() != null) count++;
        if ( inlineButtonRequestDTO.callbackData() != null) count++;
        if ( inlineButtonRequestDTO.switchInlineQuery() != null) count++;

        if (count != 1) return false;
        else return true;
    }
}