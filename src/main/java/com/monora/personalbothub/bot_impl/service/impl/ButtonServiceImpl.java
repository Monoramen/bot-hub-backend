package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.request.ButtonRequestDTO;
import com.monora.personalbothub.bot_api.dto.response.ButtonResponseDTO;
import com.monora.personalbothub.bot_api.exception.ApiErrorType;
import com.monora.personalbothub.bot_api.exception.ApiException;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.ButtonEntity;
import com.monora.personalbothub.bot_db.entity.attachment.keyboard.KeyboardEntity;
import com.monora.personalbothub.bot_db.repository.ButtonRepository;
import com.monora.personalbothub.bot_impl.mapper.ButtonMapper;
import com.monora.personalbothub.bot_impl.service.ButtonService;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ButtonServiceImpl implements ButtonService {

    private final ButtonRepository buttonRepository;
    private final ButtonMapper buttonMapper;

    @Override
    @Transactional
    public ButtonEntity create(ButtonRequestDTO buttonRequestDTO, KeyboardEntity keyboard) {
        Optional<ButtonEntity> existingButton = buttonRepository.findByText(buttonRequestDTO.text());

        if (existingButton.isPresent()) {
            // Если кнопка с таким текстом уже существует, выбрасываем исключение
            throw new ApiException(ApiErrorType.BAD_REQUEST, "Button with text: "
                    + buttonRequestDTO.text() + " already exists");
        } else {
            ButtonEntity button = new ButtonEntity();
            button.setText(buttonRequestDTO.text());
            button.setRow(buttonRequestDTO.row());
            button.setPosition(buttonRequestDTO.position());
            button.setRequestLocation(buttonRequestDTO.requestLocation());
            button.setRequestContact(buttonRequestDTO.requestContact());
            button.setKeyboard(keyboard); // Привязываем кнопку к клавиатуре
            return buttonRepository.save(button);
        }
    }

    @Override
    @Transactional
    public ButtonEntity update(ButtonRequestDTO buttonRequestDTO) {
        ButtonEntity existingButton = buttonRepository.findById(buttonRequestDTO.id())
                .orElseThrow(() -> new ApiException(ApiErrorType.NOT_FOUND, "Button not found"));

        existingButton.setText(buttonRequestDTO.text());
        existingButton.setRow(buttonRequestDTO.row());
        existingButton.setPosition(buttonRequestDTO.position());
        existingButton.setRequestLocation(buttonRequestDTO.requestLocation());
        existingButton.setRequestContact(buttonRequestDTO.requestContact());

        return buttonRepository.save(existingButton);
    }



    @Transactional
    @Override
    public void delete(Long id) {
        ButtonEntity buttonEntity = buttonRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Inline button with id: " + id + " not found"));
        buttonRepository.delete(buttonEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ButtonResponseDTO findById(Long id) {
        ButtonEntity buttonEntity = buttonRepository.findById(id).orElseThrow(
                () -> new ApiException(ApiErrorType.NOT_FOUND, "Button not found"));
        return buttonMapper.toResponse(buttonEntity);
    }
    @Transactional(readOnly = true)
    @Override
    public List<ButtonResponseDTO> findAll() {
        List<ButtonEntity> buttonEntities = buttonRepository.findAll();
        if (buttonEntities.isEmpty()) {
            throw new ApiException(ApiErrorType.NOT_FOUND, "Button not found");
        }
        return buttonMapper.toResponseList(buttonEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public KeyboardButton[][] getButtonRowByKeyboardId(Long keyboardId) {
        List<ButtonEntity> buttons = buttonRepository.findAllByKeyboardId(keyboardId);
        return buildKeyboard(buttons);
    }


    private KeyboardButton[][] buildKeyboard(List<ButtonEntity> buttons) {

        if (buttons.isEmpty()) {
            return new KeyboardButton[0][0];
        }
        int rowIndex = 0;

        Map<Integer, List<ButtonEntity>> rowMap = buttons.stream().collect(Collectors.groupingBy(ButtonEntity::getRow));
        KeyboardButton[][] buttonRows = new KeyboardButton[rowMap.size()][rowMap.size()];

        for (List<ButtonEntity> rowButtons : rowMap.values()) {
            buttonRows[rowIndex] = rowButtons.stream()
                    .sorted(Comparator.comparingInt(ButtonEntity::getPosition))
                    .map(button -> {
                        KeyboardButton keyboardButton = new KeyboardButton(button.getText());
                        keyboardButton.requestContact(button.getRequestContact());
                        keyboardButton.requestLocation(button.getRequestLocation());
                        return keyboardButton;
                    })
                    .toArray(KeyboardButton[]::new);
            rowIndex++;
        }
        return buttonRows;
    }


}
