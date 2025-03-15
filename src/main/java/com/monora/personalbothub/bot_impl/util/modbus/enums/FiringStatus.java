package com.monora.personalbothub.bot_impl.util.modbus.enums;

import java.util.Arrays;
import java.util.Optional;

public enum FiringStatus {
    STOPPED(0, "Режим Стоп"),
    RUNNING(1, "Режим Работа"),
    CRITICAL_ERROR(2, "Режим Критическая Авария"),
    PROGRAM_COMPLETED(3, "Программа технолога завершена"),
    PID_AUTOTUNE_RUNNING(4, "Режим Автонастройка ПИД-регулятора"),
    PID_AUTOTUNE_WAITING(5, "Ожидание запуска режима Автонастройка"),
    PID_AUTOTUNE_COMPLETED(6, "Автонастройка ПИД-регулятора завершена"),
    SETTINGS(7, "Режим Настройка");

    private final int code;
    private final String description;

    FiringStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Статический метод для поиска FiringStatus по его числовому коду.
     * @param code числовой код статуса
     * @return Optional, содержащий FiringStatus, если код найден, иначе пустой Optional
     */
    public static Optional<FiringStatus> fromCode(int code) {
        return Arrays.stream(values())
                .filter(status -> status.getCode() == code)
                .findFirst();
    }
}