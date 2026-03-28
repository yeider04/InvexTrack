# InvexTrack - Sistema de Gestión de Inventarios 

InvexTrack es una API REST profesional desarrollada con **Java 21** y **Spring Boot 4.0.5**, diseñada para centralizar el control de existencias, movimientos de almacén y gestión de proveedores.

## Estado del Proyecto: Fase de Construcción
El proyecto se encuentra en su fase de **Backend Funcional**. Se ha implementado una arquitectura por capas que garantiza la escalabilidad y el cumplimiento de los artefactos de diseño previos (Diagramas de Clases y Casos de Uso).

### Características Implementadas
* **Inicialización Automática:** El sistema crea la estructura de tablas e inserta datos maestros (Categorías, Usuarios, Proveedores) automáticamente al iniciar.
* **Documentación Viva:** Integración total con **Swagger UI** para probar cada endpoint sin necesidad de herramientas externas.
* **Mapeo de Datos Robusto:** Entidades JPA configuradas para coincidir con el esquema relacional de `invextrack_db`.
* **Gestión de Excepciones:** Manejo global de errores para respuestas API limpias y legibles.

---

## Tecnologías Utilizadas
* **Lenguaje:** Java 21.
* **Framework:** Spring Boot 4.0.5.
* **Base de Datos:** MySQL 8.0.
* **Documentación:** SpringDoc OpenAPI (Swagger).
* **Gestión de Dependencias:** Maven.

---

## Cómo clonar y probar el proyecto

### 1. Requisitos Previos
* Tener instalado **Java 17 o 21**.
* Tener **MySQL Server** en ejecución.
* Crear una base de datos vacía llamada `invextrack_db`.

### 2. Clonar el Repositorio
```bash
git clone https://github.com/yeider04/InvexTrack.git
cd api
```

### 3. Configuración del Entorno
Edita el archivo src/main/resources/application.yml con tus credenciales locales de MySQL:
```bash
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/invextrack_db?createDatabaseIfNotExist=true
    username: TU_USUARIO_AQUÍ
    password: TU_CONTRASEÑA_AQUÍ
```

### 4. Ejecución
En la raíz del proyecto, ejecuta el siguiente comando:
```bash
./mvnw spring-boot:run
```

### 5. Probar la API
Una vez que la consola muestre Started ApiApplication, abre tu navegador en:
 http://localhost:8080/swagger-ui/index.html

Desde allí podrás ejecutar peticiones GET y POST para ver cómo interactúan los Productos, Usuarios y Movimientos.

---

## Estructura del Código
* **model/** : Entidades JPA que representan las tablas de la DB.
* **repository/** : Interfaces para la persistencia de datos (Spring Data JPA).
* **controller/** : Endpoints de la API documentados con Swagger.
* **resources/** Scripts SQL (schema.sql y data.sql) para la portabilidad del sistema.

