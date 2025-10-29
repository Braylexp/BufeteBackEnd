package com.bufete.backend.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;


@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de gestión de bufetes de abogados",
        version = "1.0.0",
        description = "Sistema Bufete de Abogados",
        contact = @Contact(
            name = "Soporte Técnico",
            email = "soporte@bufete.com"
        )
    )
)
public class OpenApiConfig {
}
