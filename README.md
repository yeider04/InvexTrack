# InvexTrack - Sistema de Gestión de Inventarios

InvexTrack es una API REST desarrollada con **Java 21** y **Spring Boot 3.4.5**, diseñada para centralizar el control de existencias, movimientos de almacén y gestión de proveedores.

---

## Estado del Proyecto

**Fase:** Backend Funcional — CRUD completo implementado para todas las entidades.

### Módulos implementados

| Módulo | GET (Listar) | GET (Por ID) | POST (Crear) | PUT (Actualizar) | DELETE (Eliminar) |
|---|---|---|---|---|---|
| Productos | ✅ | ✅ | ✅ | ✅ | ✅ |
| Categorías | ✅ | ✅ | ✅ | ✅ | ✅ |
| Proveedores | ✅ | ✅ | ✅ | ✅ | ✅ |
| Usuarios | ✅ | ✅ | ✅ | ✅ | ✅ |
| Movimientos | ✅ | ✅ | ✅ | — | ✅ |

---

## Tecnologías Utilizadas

- **Lenguaje:** Java 21
- **Framework:** Spring Boot 3.4.5
- **Base de Datos:** MySQL 8.0
- **ORM:** Spring Data JPA / Hibernate
- **Documentación:** SpringDoc OpenAPI 2.8.6 (Swagger UI)
- **Validación:** Jakarta Validation (Bean Validation)
- **Gestión de Dependencias:** Maven
- **CI/CD:** GitHub Actions

---

## Estructura del Proyecto

```
src/main/java/com/invextrack/api/
├── config/
│   └── OpenApiConfig.java          # Configuración de Swagger UI
├── controller/
│   ├── CategoriaController.java    # Endpoints CRUD de categorías
│   ├── MovimientoController.java   # Endpoints de movimientos de inventario
│   ├── ProductoController.java     # Endpoints CRUD de productos
│   ├── ProveedorController.java    # Endpoints CRUD de proveedores
│   └── UsuarioController.java      # Endpoints CRUD de usuarios
├── dto/
│   ├── CategoriaRequestDTO.java    # DTO para creación/actualización de categorías
│   ├── MovimientoRequestDTO.java   # DTO para registro de movimientos
│   ├── ProductoRequestDTO.java     # DTO para creación/actualización de productos
│   ├── ProveedorRequestDTO.java    # DTO para creación/actualización de proveedores
│   └── UsuarioRequestDTO.java      # DTO para registro/actualización de usuarios
├── exception/
│   └── GlobalExceptionHandler.java # Manejo centralizado de errores
├── model/
│   ├── Categoria.java              # Entidad JPA - tabla categorias
│   ├── MovimientoInventario.java   # Entidad JPA - tabla movimientos_inventario
│   ├── Producto.java               # Entidad JPA - tabla productos
│   ├── Proveedor.java              # Entidad JPA - tabla proveedores
│   └── Usuario.java                # Entidad JPA - tabla usuarios
├── repository/
│   ├── CategoriaRepository.java    # Repositorio JPA de categorías
│   ├── MovimientoRepository.java   # Repositorio JPA de movimientos
│   ├── ProductoRepository.java     # Repositorio JPA de productos
│   ├── ProveedorRepository.java    # Repositorio JPA de proveedores
│   └── UsuarioRepository.java      # Repositorio JPA de usuarios
├── service/
│   └── ProductoService.java        # Capa de servicio con validaciones de negocio
└── ApiApplication.java             # Clase principal Spring Boot
```

---

## Requisitos Previos

- Java 21 instalado
- MySQL Server 8.0 en ejecución
- Maven (o usar el wrapper `./mvnw` incluido)

---

## Configuración e Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/yeider04/InvexTrack.git
cd InvexTrack
```

### 2. Configurar variables de entorno

Copia el archivo de ejemplo y completa tus credenciales:

```bash
cp .env.example .env
```

Edita `.env` con tus datos de MySQL:

```
DB_URL=jdbc:mysql://localhost:3306/invextrack_db?createDatabaseIfNotExist=true
DB_USER=root
DB_PASSWORD=tu_contrasena
```

> ⚠️ **Nunca subas el archivo `.env` a Git.** Ya está incluido en `.gitignore`.

### 3. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

Al iniciar, Spring Boot ejecutará automáticamente `schema.sql` y `data.sql`, creando las tablas e insertando datos de prueba.

### 4. Probar la API con Swagger UI

Abre tu navegador en:

```
http://localhost:8080/swagger-ui/index.html
```

Desde Swagger UI puedes ejecutar todas las operaciones GET, POST, PUT y DELETE con formularios pre-llenados de ejemplo.

---

## Endpoints Disponibles

### Productos — `/api/v1/productos`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/productos` | Listar todos los productos |
| GET | `/api/v1/productos/{id}` | Consultar producto por ID |
| POST | `/api/v1/productos` | Crear nuevo producto |
| PUT | `/api/v1/productos/{id}` | Actualizar producto existente |
| DELETE | `/api/v1/productos/{id}` | Eliminar producto |

### Categorías — `/api/v1/categorias`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/categorias` | Listar todas las categorías |
| GET | `/api/v1/categorias/{id}` | Consultar categoría por ID |
| POST | `/api/v1/categorias` | Crear nueva categoría |
| PUT | `/api/v1/categorias/{id}` | Actualizar categoría |
| DELETE | `/api/v1/categorias/{id}` | Eliminar categoría |

### Proveedores — `/api/v1/proveedores`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/proveedores` | Listar todos los proveedores |
| GET | `/api/v1/proveedores/{id}` | Consultar proveedor por ID |
| POST | `/api/v1/proveedores` | Crear nuevo proveedor |
| PUT | `/api/v1/proveedores/{id}` | Actualizar proveedor |
| DELETE | `/api/v1/proveedores/{id}` | Eliminar proveedor |

### Usuarios — `/api/v1/usuarios`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/usuarios` | Listar todos los usuarios |
| GET | `/api/v1/usuarios/{id}` | Consultar usuario por ID |
| POST | `/api/v1/usuarios` | Registrar nuevo usuario |
| PUT | `/api/v1/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/v1/usuarios/{id}` | Eliminar usuario |

### Movimientos — `/api/v1/movimientos`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/movimientos` | Listar historial de movimientos |
| GET | `/api/v1/movimientos/{id}` | Consultar movimiento por ID |
| POST | `/api/v1/movimientos` | Registrar entrada o salida |
| DELETE | `/api/v1/movimientos/{id}` | Eliminar registro de movimiento |

---

## Estándares de Codificación

El proyecto sigue las convenciones estándar de Java:

- **Clases:** `PascalCase` → `ProductoController`, `GlobalExceptionHandler`
- **Métodos y variables:** `camelCase` → `listarTodos()`, `nuevoProducto`
- **Paquetes:** `lowercase` → `com.invextrack.api.controller`
- **Constantes:** `UPPER_SNAKE_CASE` → `HttpStatus.BAD_REQUEST`
- **DTOs:** sufijo `RequestDTO` → `ProductoRequestDTO`
- **Repositorios:** sufijo `Repository` → `ProductoRepository`

---

## Flujo de Trabajo con Git

```
main          ← código estable y entregable
└── develop   ← integración de features
```

---

## Ejecución de Pruebas

```bash
./mvnw test
```

---

## CI/CD

El proyecto incluye un workflow de GitHub Actions (`.github/workflows/ci.yml`) que se ejecuta automáticamente en cada push a `main` o `develop`, compilando el proyecto y ejecutando las pruebas.

