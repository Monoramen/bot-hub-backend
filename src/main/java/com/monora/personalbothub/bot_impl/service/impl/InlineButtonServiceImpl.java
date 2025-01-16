package com.monora.personalbothub.bot_impl.service.impl;

import com.monora.personalbothub.bot_api.dto.InlineButtonDto;
import com.monora.personalbothub.bot_db.entity.InlineButtonEntity;
import com.monora.personalbothub.bot_db.repository.InlineButtonRepository;
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


    @Override
    public void addInlineButton(InlineButtonDto inlineButtonDto) {

    }

    @Override
    public void updateInlineButton(InlineButtonDto inlineButtonDto) {

    }

    @Override
    public InlineKeyboardButton[] getInlineButtonRowByKeyboardId(Long inlineKeyboardId) {
        List<InlineButtonEntity> buttons = inlineButtonRepository.findAllByInlineKeyboardId(inlineKeyboardId);

        // Проверяем, есть ли кнопки
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

            buttonRow[i] = inlineKeyboardButton; // Присваиваем созданный объект в массив
        }
        return buttonRow;
    }



}