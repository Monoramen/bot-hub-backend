package com.monora.personalbothub.bot_impl.util.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class UpdateDispatcher {

    private final List<UpdateHandler> updateHandlers;
    private final List<AttachmentHandler> attachmentHandlers;

    public void dispatch(Update update) {
        log.info("Получено обновление: {}", update.updateId());

        // Обработка основного обновления (например, команды)
        for (UpdateHandler handler : updateHandlers) {
            if (handler.supports(update)) {
                handler.handle(update);
                // Если требуется, можно завершить цикл после первого подходящего обработчика.
                break;
            }
        }

        // Делегируем обработку вложений отдельным обработчикам
        for (AttachmentHandler attachmentHandler : attachmentHandlers) {
            if (attachmentHandler.supports(update)) {
                attachmentHandler.handle(update);
            }
        }
    }
}
