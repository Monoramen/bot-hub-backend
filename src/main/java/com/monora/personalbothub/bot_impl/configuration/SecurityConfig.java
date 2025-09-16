package com.monora.personalbothub.bot_impl.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securedFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/**").permitAll()  // ✅ ВСЁ открыто — для dev-режима
                        // ❌ УДАЛЕНО: .anyRequest().authenticated() — несовместимо с /** + permitAll
                )
                .csrf(csrf -> csrf.disable())           // Отключаем CSRF для API
                .httpBasic(Customizer.withDefaults())   // Включаем базовую аутентификацию (если понадобится)
                .formLogin(Customizer.withDefaults());  // Включаем форму логина (если понадобится)

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:3000",
                                "http://frontend:3000",
                                "http://192.168.0.128:3000"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600); // Кэширование preflight-запросов
            }
        };
    }
}