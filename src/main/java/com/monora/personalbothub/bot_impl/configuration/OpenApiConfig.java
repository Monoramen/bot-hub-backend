package com.monora.personalbothub.bot_impl.configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Backend REST API",
                description = "API documentation for the backend application with Telegram integration, firing program and temperature control",
                version = "1.0.0",
                contact = @Contact(
                        name = "Monoramen",
                        email = "monoramen@example.com"
                )
        )
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
    // This class is used only for Swagger/OpenAPI configuration via annotations
}