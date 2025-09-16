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
            "USB Serial Converter", "FTDI", "CH340", "Modbus", "Converter"
    };
    private volatile ModbusRtuClient client;
    private String selectedPortName;

    public ModbusRtuClient connect() {
        if (client != null && client.isConnected()) {
            log.info("Клиент уже подключён к порту {}", selectedPortName);
            return client;
        }

        log.info("Поиск доступного COM-порта...");
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            log.warn("COM-порты не найдены — клиент не будет подключён");
            return null; // Не падаем, просто возвращаем null
        }

        log.info("Доступные COM-порты:");
        SerialPort selectedPort = null;
        for (SerialPort port : ports) {
            String portName = port.getSystemPortName();
            String description = port.getPortDescription();
            log.info(" - {} (описание: {})", portName, description);

            for (String keyword : PORT_DESCRIPTIONS) {
                if (description != null && description.toLowerCase().contains(keyword.toLowerCase())) {
                    selectedPort = port;
                    selectedPortName = portName;
                    log.info("Выбран порт {} с описанием '{}'", portName, description);
                    break;
                }
            }
            if (selectedPort != null) break;
        }

        if (selectedPort == null) {
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
            selectedPortName = null;
            return null; // Не пробрасываем исключение — делаем отказоустойчиво
        }
    }

    @PreDestroy
    public void disconnect() {
        if (client != null) {
            try {
                log.info("Отключение клиента от порта {}...", selectedPortName);
                client.disconnect();
            } catch (Exception e) {
                log.error("Ошибка при отключении: {}", e.getMessage(), e);
            } finally {
                client = null;
                selectedPortName = null;
            }
        }
    }

    public ModbusRtuClient getClient() {
        // УБРАЛИ автоматическое переподключение — оно может блокировать или падать при старте
        if (client != null && client.isConnected()) {
            return client;
        } else {
            log.debug("Клиент Modbus не подключён. Вызовите connect() вручную при необходимости.");
            return null;
        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    // Добавим метод для ручного переподключения, если нужно
    public void reconnect() {
        try {
            disconnect();
            connect();
        } catch (Exception e) {
            log.error("Не удалось переподключиться: {}", e.getMessage(), e);
        }
    }
}