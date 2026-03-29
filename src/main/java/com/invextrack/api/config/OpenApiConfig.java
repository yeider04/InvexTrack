package com.invextrack.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger / OpenAPI para la documentación de la API InvexTrack.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI configuracionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("InvexTrack API")
                        .version("1.0.0")
                        .description("Sistema de Gestión de Inventarios — Documentación completa de endpoints REST")
                        .contact(new Contact()
                                .name("InvexTrack")
                                .email("admin@invextrack.com")));
    }
}
