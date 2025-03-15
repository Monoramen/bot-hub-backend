package com.monora.personalbothub.bot_impl.util.modbus.enums;

public enum RuntimeParameter {
    READ("rEAd", 0x0004, "Результат измерения на Входе 1 (float32)",
            "float32", "-999.0..9999.0 или коды ошибок 0xF6..0xFF", "Занимает 2 регистра (0x0004 и 0x0005)"),
    R_OUT("r.oUt", 0x000C, "Выходная мощность","int16", "0..1000", "0.1 ед. = 1% выходной мощности"),
    R_SIG("r.SiG", 0x000E, "Состояние Устройства сигнализации","int16", "0..1", "0 – в норме, 1 – за пределами диапазона"),
    R_PRG("r.PrG", 0x000F, "Номер текущей Программы технолога", "int16", "0..3", "Записывается только в режиме Стоп"),
    R_STP("r.StP", 0x0010, "Номер текущего шага Программы технолога","int16", "1..5", "Записывается только в режиме Стоп"),
    R_ST("r.St", 0x0011, "Режим работы прибора","int16", "0..7", "0 – Стоп, 1 – Работа, 2 – Критическая авария, 3 – программа завершена, 4 – автоналадка ПИД, 5 – ожидание запуска автоналадки, 6 – автоналадка завершена, 7 – настройка"),
    SET_P("SEt.P", 0x000D, "Мгновенное значение уставки","int16", "-999..9999", "0.1 ед. = 1 единица уставки"),
    R_S("r-S", 0x0050, "Пуск/останов Программы технолога","coil/int16", "0..1", "0: Стоп, 1: Работа");

    private final String name;
    private final int address;
    private final String description;
    private final String dataType;
    private final String range;
    private final String comment;

    RuntimeParameter(String name, int address, String description, String dataType, String range, String comment) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.dataType = dataType;
        this.range = range;
        this.comment = comment;
    }

    public int getAddress() { return address; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDataType() { return dataType; }
    public String getRange() { return range; }
    public String getComment() { return comment; }
}
