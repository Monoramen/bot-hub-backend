package com.monora.personalbothub.bot_impl.util.modbus;

import com.monora.personalbothub.bot_impl.util.modbus.enums.ConfigParameter;
import com.monora.personalbothub.bot_impl.util.modbus.enums.RuntimeParameter;
import com.monora.personalbothub.bot_impl.util.modbus.enums.TechProgramParameter;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ParameterRegistry {

    private Map<String, RuntimeParameter> runtimeByName;
    private Map<String, TechProgramParameter> techProgramByName;

    @PostConstruct
    public void init() {

        // Инициализация мапы для оперативных параметров по имени
        runtimeByName = Stream.of(RuntimeParameter.values())
                .collect(Collectors.toMap(RuntimeParameter::getName, param -> param));

        // Инициализация мапы для параметров программ технолога по имени
        techProgramByName = Stream.of(TechProgramParameter.values())
                .collect(Collectors.toMap(TechProgramParameter::getName, param -> param));
    }

    public RuntimeParameter getRuntimeParameter(String name) {
        return runtimeByName.get(name);
    }

    public TechProgramParameter getTechProgramParameter(String name) {
        return techProgramByName.get(name);
    }
}