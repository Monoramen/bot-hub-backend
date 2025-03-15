package com.monora.personalbothub.bot_impl.util.modbus;

import com.digitalpetri.modbus.client.ModbusRtuClient;
import com.digitalpetri.modbus.serial.client.SerialPortClientTransport;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import java.time.Duration;

@Slf4j
@Component
public class ModbusConnectionManager {
    private static final int BAUD_RATE = 115200;
    private static final Duration REQUEST_TIMEOUT = Duration.ofMillis(80);
    private static final String[] PORT_DESCRIPTIONS = {
            "USB Serial Converter", "FTDI", "CH340", "Modbus", "Converter" // Ключевые слова для поиска порта
    };
    private volatile ModbusRtuClient client;
    private String selectedPortName;

    public ModbusRtuClient connect() throws Exception {
        if (client != null && client.isConnected()) {
            log.info("Клиент уже подключён к порту {}", selectedPortName);
            return client;
        }

        log.info("Поиск доступного COM-порта...");
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            log.error("COM-порты не найдены");
            throw new IllegalStateException("Нет доступных COM-портов");
        }

        // Перебираем доступные порты
        log.info("Доступные COM-порты:");
        SerialPort selectedPort = null;
        for (SerialPort port : ports) {
            String portName = port.getSystemPortName();
            String description = port.getPortDescription();
            log.info(" - {} (описание: {})", portName, description);

            // Проверяем, содержит ли описание порта одно из ключевых слов
            for (String keyword : PORT_DESCRIPTIONS) {
                if (description != null && description.toLowerCase().contains(keyword.toLowerCase())) {
                    selectedPort = port;
                    selectedPortName = portName;
                    log.info("Выбран порт {} с описанием '{}'", portName, description);
                    break;
                }
            }
            if (selectedPort != null) {
                break;
            }
        }

        if (selectedPort == null) {
            // Если подходящий порт не найден, можно выбрать первый доступный порт
            selectedPort = ports[0];
            selectedPortName = selectedPort.getSystemPortName();
            log.warn("Подходящий порт не найден, используется первый доступный порт: {}", selectedPortName);
        }

        try {
            SerialPortClientTransport transport = SerialPortClientTransport.create(cfg -> {
                cfg.serialPort = selectedPortName;
                cfg.baudRate = BAUD_RATE;
                cfg.dataBits = 8;
                cfg.parity = SerialPort.NO_PARITY;
                cfg.stopBits = SerialPort.ONE_STOP_BIT;
            });

            log.info("Создание Modbus RTU клиента для порта {}...", selectedPortName);
            client = ModbusRtuClient.create(transport, cfg -> {
                cfg.requestTimeout = REQUEST_TIMEOUT;
            });

            client.connect();
            log.info("Клиент успешно подключён к порту {}", selectedPortName);
            return client;
        } catch (Exception e) {
            log.error("Ошибка подключения к порту {}: {}", selectedPortName, e.getMessage(), e);
            client = null;
            throw e;
        }
    }

    @PreDestroy
    public void disconnect() {
        if (client != null) {
            try {
                log.info("Отключение клиента от порта {}...", selectedPortName);
                client.disconnect();
                client = null;
                selectedPortName = null;
            } catch (Exception e) {
                log.error("Ошибка при отключении: {}", e.getMessage(), e);
            }
        }
    }

    public ModbusRtuClient getClient() {
        if (client == null || !client.isConnected()) {
            log.warn("Клиент не подключён или отключён, попытка восстановления соединения...");
            try {
                connect();
            } catch (Exception e) {
                log.error("Не удалось восстановить соединение: {}", e.getMessage(), e);
                return null;
            }
        }
        return client;
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }
}