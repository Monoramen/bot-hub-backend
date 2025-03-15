package com.monora.personalbothub.bot_impl.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature; // Импортируем SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Импортируем модуль времени
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 1. Регистрируем модуль, который научит Jackson работать с LocalDateTime, LocalDate и т.д.
        mapper.registerModule(new JavaTimeModule());

        // 2. Отключаем стандартное поведение записи дат в виде числовых таймстэмпов.
        //    Это позволит использовать форматирование из аннотации @JsonFormat("yyyy-MM-dd HH:mm:ss")
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Ваша настройка может остаться, если она нужна
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Рекомендую false, чтобы не падать из-за лишних полей

        return mapper;
    }
}