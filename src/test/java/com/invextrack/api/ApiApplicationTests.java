package com.invextrack.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Prueba de carga del contexto de Spring Boot.
 * Verifica que la aplicación levanta correctamente con su configuración.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.sql.init.mode=never",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ApiApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring Boot carga sin errores
    }
}
