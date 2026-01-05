# Sistema de Gestión de Proyectos y Tareas

Este proyecto implementa un sistema backend para la gestión de proyectos y tareas, desarrollado bajo los principios de **Clean Architecture** y **Hexagonal Architecture** (Ports & Adapters) utilizando **Java 17** y **Spring Boot 3.4.1**.

## Visión General

El objetivo principal es proporcionar una API REST robusta, mantenible y desacoplada de frameworks en su núcleo de negocio. El sistema permite la creación de usuarios, gestión de proyectos y seguimiento de tareas, garantizando reglas de negocio estrictas como la integridad de activación de proyectos y permisos de propiedad.

## Arquitectura

El proyecto sigue una estructura estricta de capas concéntricas, garantizando que las dependencias fluyan únicamente hacia el interior (hacia el Dominio).

### Estructura de Directorios (Clean Architecture)

```text
src/main/java/com/riwi/systemprojects/
├── domain/                                     # NÚCLEO: Java puro, sin frameworks
│   ├── model/                                  # Entidades del dominio (User, Project, Task)
│   ├── exception/                              # Excepciones de negocio
│   └── ports/                                  # CONTRATOS (Interfaces)
│       ├── in/                                 # Casos de Uso (Input Ports)
│       └── out/                                # Puertos de Salida (Output Ports)
├── application/                                # APLICACIÓN: Orquestación
│   └── services/                               # Implementación de Casos de Uso
└── infrastructure/                             # INFRAESTRUCTURA: Adaptadores y Config
    ├── adapters/
    │   ├── input/                              # Driving Adapters (REST Controllers)
    │   └── output/                             # Driven Adapters (Persistence, Security, etc.)
    └── config/                                 # Configuración de Spring y Seguridad
```

## Tecnologías

*   **Lenguaje**: Java 17
*   **Framework**: Spring Boot 3.4.1
*   **Base de Datos**: PostgreSQL 15 (Puerto 5433 local / 5432 Docker)
*   **Seguridad**: Spring Security + JWT (JSON Web Tokens)
*   **Documentación API**: Springdoc OpenAPI 2.6.0 (Swagger UI)
*   **Contenedores**: Docker, Docker Compose

## Instalación y Ejecución

### Prerrequisitos
*   **Docker** y **Docker Compose** instalados.
*   **Java 17 JDK** y **Maven** (para construir el JAR).

### Pasos para el Despliegue

Para desplegar la aplicación correctamente, siga estos pasos:

1.  **Construir el archivo JAR**:
    La imagen de Docker utiliza el artefacto generado localmente. Ejecute:
    ```bash
    ./mvnw clean package -DskipTests
    ```

2.  **Levantar los contenedores**:
    Una vez generado el archivo `.jar` en la carpeta `target/`, ejecute:
    ```bash
    docker-compose down
    docker-compose up -d --build
    ```

3.  **Verificar estado**:
    Puede seguir los logs del inicio con:
    ```bash
    docker-compose logs -f app
    ```

La aplicación estará disponible en: **[http://localhost:8081](http://localhost:8081)**

## Documentación de la API (Swagger)

La documentación interactiva está disponible una vez que la aplicación esté corriendo en:

*   **Swagger UI**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
*   **OpenAPI JSON**: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

### Datos de Prueba (Carga Automática)
Al iniciar con Docker, se cargan automáticamente los siguientes datos desde `init-data.sql`:
*   **Usuario**: `user1` / **Contraseña**: `password123`
*   **Admin**: `admin` / **Contraseña**: `password123`

## Pruebas con Postman

1.  **Login**: Envíe un `POST` a `/api/auth/login` con las credenciales de prueba.
2.  **Token**: Copie el `token` de la respuesta.
3.  **Autorización**: En Postman, configure el tipo de autorización como **Bearer Token** y pegue el código.
4.  **Endpoints**: Ahora podrá acceder a rutas protegidas como `/api/projects` (POST).

## Ejecución de Pruebas Unitarias

Para ejecutar el set de pruebas locales:
```bash
./mvnw test
```
