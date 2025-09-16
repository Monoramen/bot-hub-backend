package com.monora.personalbothub.bot_api.controller;

import com.monora.personalbothub.bot_impl.util.modbus.ModbusConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ModbusHealthController {

    private final ModbusConnectionManager modbusManager;

    @GetMapping("/actuator/modbus")
    public Map<String, Object> modbusStatus() {
        return Map.of(
                "connected", modbusManager.isConnected()
//                "connectionType", modbusManager.getConnectionType(), // добавьте геттер
//                "target", modbusManager.getSelectedPortName()         // добавьте геттер
        );
    }
}