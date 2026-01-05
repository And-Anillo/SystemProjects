# Sistema de Gestion de Proyectos y Tareas

Este proyecto implementa un sistema backend para la gestion de proyectos y tareas, desarrollado bajo los principios de Clean Architecture y Hexagonal Architecture (Ports & Adapters) utilizando Java 17 y Spring Boot 3.

## Vision General

El objetivo principal es proporcionar una API REST robusta, mantenible y desacoplada de frameworks en su nucleo de negocio. El sistema permite la creacion de usuarios, gestion de proyectos y seguimiento de tareas, garantizando reglas de negocio estrictas como la integridad de activacion de proyectos y permisos de propiedad.

## Arquitectura

El proyecto sigue una estructura estricta de capas concentricas, garantizando que las dependencias fluyan unicamente hacia el interior (hacia el Dominio).

### Estructura de Directorios

src/main/java/com/riwi/systemprojects/
├── domain/                                     # NUCLEO: Java puro, sin frameworks
│   ├── model/                                  # Entidades del dominio (User, Project, Task)
│   ├── exception/                              # Excepciones de negocio
│   └── ports/                                  # CONTRATOS (Interfaces)
│       ├── in/                                 # Casos de Uso (Input Ports)
│       └── out/                                # Puertos de Salida (Output Ports)
├── application/                                # APLICACION: Orquestacion
│   └── services/                               # Implementacion de Casos de Uso
└── infrastructure/                             # INFRAESTRUCTURA: Adaptadores y Config
    ├── adapters/
    │   ├── input/                              # Driving Adapters (REST Controllers)
    │   └── output/                             # Driven Adapters (Persistence, Security, etc.)
    └── config/                                 # Configuracion de Spring

### Decisiones de Diseno

1.  **Pureza del Dominio**: El paquete `domain` no tiene dependencias de Spring Boot, JPA ni Hibernate. Solo contiene logica de negocio pura.
2.  **Puertos y Adaptadores**:
    *   **Puertos (Interfaces)**: Definen los contratos en el Dominio.
    *   **Adaptadores (Implementaciones)**: Residen en Infraestructura e implementan los puertos de salida (ej. `JpaProjectRepository`) o usan los puertos de entrada (ej. `ProjectController`).
3.  **Inversion de Dependencias**: La capa de Aplicacion depende de abstracciones (Puertos), no de implementaciones concretas.

## Tecnologias

*   **Lenguaje**: Java 17
*   **Framework**: Spring Boot 3.5.9
*   **Base de Datos**: PostgreSQL 15
*   **Seguridad**: Spring Security + JWT (JSON Web Tokens)
*   **Pruebas**: JUnit 5, Mockito, H2 Database (para entorno de test)
*   **Documentacion API**: Springdoc OpenAPI (Swagger UI)
*   **Contenedores**: Docker, Docker Compose

## Reglas de Negocio

El sistema hace cumplir estrictamente las siguientes reglas:

1.  **Activacion de Proyectos**: Un proyecto solo puede cambiar su estado a ACTIVO si tiene asociada al menos una tarea activa.
2.  **Propiedad**: Solo el usuario creador (propietario) de un proyecto puede modificarlo o gestionar sus tareas.
3.  **Inmutabilidad de Tareas Completadas**: Una tarea marcada como completada no puede ser modificada.
4.  **Eliminacion Logica**: Los recursos no se eliminan fisicamente de la base de datos; se utiliza un indicador de `deleted`.

## Instalacion y Ejecucion

### Prerrequisitos
*   Docker y Docker Compose instalados.
*   Java 17 JDK (opcional si se usa Docker).

### Ejecucion con Docker (Recomendado)

El proyecto incluye una configuracion de Docker Compose que levanta la aplicacion, la base de datos y carga datos iniciales de prueba.

1.  Clone el repositorio.
2.  Ejecute el siguiente comando en la raiz del proyecto:

    docker compose up --build

3.  La aplicacion estara disponible en el puerto 8080.

### Datos Iniciales
Al iniciar con Docker, se cargan automaticamente los siguientes usuarios de prueba:
*   **Admin**: admin / password123
*   **User1**: user1 / password123

## Documentacion de la API

La documentacion interactiva de OpenAPI (Swagger) esta disponible en:

    http://localhost:8080/swagger-ui.html

### Endpoints Principales

#### Autenticacion
*   `POST /api/auth/register`: Registro de nuevos usuarios.
*   `POST /api/auth/login`: Inicio de sesion (retorna JWT).

#### Proyectos
*   `GET /api/projects`: Listar proyectos del usuario autenticado.
*   `POST /api/projects`: Crear nuevo proyecto (Estado inicial: DRAFT).
*   `PATCH /api/projects/{id}/activate`: Activar proyecto (requiere tareas).

#### Tareas
*   `GET /api/projects/{projectId}/tasks`: Listar tareas de un proyecto.
*   `POST /api/projects/{projectId}/tasks`: Crear tarea.
*   `PATCH /api/tasks/{id}/complete`: Marcar tarea como completada.

## Pruebas

El proyecto cuenta con una suite de pruebas unitarias para validar la logica de negocio y los casos de uso. Para ejecutarlas:

    ./mvnw test

Se utiliza una base de datos H2 en memoria para garantizar la independencia del entorno de ejecucion.
