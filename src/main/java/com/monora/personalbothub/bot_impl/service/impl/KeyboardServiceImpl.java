package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.KeyboardRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.KeyboardResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.attachment.inlinekeyboard.InlineKeyboardEntity;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import com.monora.personalbothub.bot_db.repository.KeyboardRepository;
import com.monora.personalbothub.bot_impl.mapper.KeyboardMapper;
import com.monora.personalbothub.bot_impl.service.ButtonService;
import com.monora.personalbothub.bot_impl.service.KeyboardResult;
import com.monora.personalbothub.bot_impl.service.KeyboardService;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class KeyboardServiceImpl implements KeyboardService {

    private final ButtonService buttonService;
    private final KeyboardMapper keyboardMapper;
    private final KeyboardRepository keyboardRepository;

    @Override
    public KeyboardEntity create(KeyboardRequestDTO keyboardRequestDTO) {
        return null;
    }

    @Override
    public KeyboardEntity update(KeyboardRequestDTO keyboardRequestDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {
        KeyboardEntity keyboardEntity = keyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Keyboard not found"));
        keyboardRepository.delete(keyboardEntity);
    }

    @Override
    public KeyboardResponseDTO findById(Long id) {
        KeyboardEntity keyboardEntity = keyboardRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Keyboard not found"));
        return keyboardMapper.toResponse(keyboardEntity);
    }

    @Override
    public List<KeyboardResponseDTO> findAll() {
        List<KeyboardEntity> keyboardEntities = keyboardRepository.findAll();
        if (keyboardEntities.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "No inline keyboard found");
        }
        return keyboardMapper.toResponseList(keyboardEntities);
    }

    @Override
    public KeyboardResult getKeyboardByCommandId(Long commandId) {
        boolean autoRemove = false;
        Optional<KeyboardEntity> optionalKeyboard = keyboardRepository.findByCommandId(commandId);

        if (optionalKeyboard.isPresent()) {
            KeyboardEntity keyboard = optionalKeyboard.get();
            KeyboardButton[][] buttonRows = buttonService.getButtonRowByKeyboardId(keyboard.getId());
            log.info("keyboard: {}", keyboard.getId());
            log.info("Buttons: {}", Arrays.deepToString(buttonRows));
            if (keyboard.isAutoRemove()){
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
}
