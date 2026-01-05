# Project Management System

Sistema de gestión de proyectos y tareas desarrollado con **Clean Architecture** y **Hexagonal Architecture (Ports & Adapters)** usando Spring Boot 3 y Java 17.

## 🏗️ Arquitectura

El proyecto implementa **Clean Architecture** con enfoque **Hexagonal (Ports & Adapters)**:

```
src/main/java/com/riwi/systemprojects/
├── domain/                    # Capa de Dominio (sin dependencias de frameworks)
│   ├── model/                # Entidades de negocio (User, Project, Task)
│   └── exception/            # Excepciones de dominio
├── application/              # Capa de Aplicación
│   ├── port/
│   │   ├── in/              # Puertos de entrada (Use Cases interfaces)
│   │   └── out/             # Puertos de salida (Repository, Audit, Notification)
│   └── usecase/             # Implementación de casos de uso
├── infrastructure/           # Capa de Infraestructura
│   ├── adapter/
│   │   └── out/
│   │       ├── persistence/ # Adaptador JPA
│   │       ├── audit/       # Adaptador de auditoría
│   │       ├── notification/# Adaptador de notificaciones
│   │       └── security/    # Adaptador de seguridad
│   └── config/              # Configuraciones (Security, Swagger, Use Cases)
└── presentation/            # Capa de Presentación
    └── rest/
        ├── controller/      # REST Controllers
        ├── dto/            # Data Transfer Objects
        └── mapper/         # Mappers DTO <-> Domain
```

### Principios Aplicados

- ✅ **Independencia del dominio**: El dominio no depende de Spring, JPA ni frameworks
- ✅ **Inversión de dependencias**: Las dependencias apuntan hacia el dominio
- ✅ **Separación de responsabilidades**: Cada capa tiene una responsabilidad clara
- ✅ **Testeable**: Los casos de uso se prueban sin levantar el contexto de Spring

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.5.9**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger/OpenAPI**
- **JUnit 5 + Mockito**
- **Docker & Docker Compose**
- **Frontend**: HTML + JavaScript (Vanilla)

## 📋 Funcionalidades

### Autenticación
- ✅ Registro de usuarios
- ✅ Login con JWT
- ✅ Protección de endpoints con JWT

### Proyectos
- ✅ Crear proyectos (estado inicial: DRAFT)
- ✅ Listar proyectos del usuario autenticado
- ✅ Activar proyectos (solo si tienen al menos una tarea activa)

### Tareas
- ✅ Crear tareas para un proyecto
- ✅ Listar tareas de un proyecto
- ✅ Completar tareas

### Reglas de Negocio
1. ✅ Un proyecto solo puede activarse si tiene al menos una tarea activa
2. ✅ Solo el propietario puede modificar un proyecto o sus tareas
3. ✅ Una tarea completada no puede modificarse
4. ✅ Todas las eliminaciones son lógicas (soft delete)
5. ✅ La activación de proyectos y finalización de tareas generan auditoría
6. ✅ La activación de proyectos y finalización de tareas generan notificación

## 🔧 Instalación y Ejecución

### Opción 1: Con Docker Compose (Recomendado)

```bash
# Clonar el repositorio
git clone https://github.com/And-Anillo/SystemProjects.git
cd SystemProjects/systemprojects

# Ejecutar con Docker Compose
docker compose up --build
```

La aplicación estará disponible en:
- **Backend API**: http://localhost:8080
- **Frontend**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

### Opción 2: Ejecución Local

#### Requisitos
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

#### Pasos

1. **Configurar la base de datos**

```sql
CREATE DATABASE projectsdb;
```

2. **Configurar application.properties** (ya está configurado por defecto)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/projectsdb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

3. **Ejecutar la aplicación**

```bash
./mvnw spring-boot:run
```

## 🧪 Pruebas Unitarias

El proyecto incluye pruebas unitarias para los casos de uso críticos:

```bash
# Ejecutar todas las pruebas
./mvnw test

# Ejecutar solo las pruebas de casos de uso
./mvnw test -Dtest=ActivateProjectUseCaseTest,CompleteTaskUseCaseTest
```

### Pruebas Implementadas

1. ✅ `ActivateProject_WithTasks_ShouldSucceed`
2. ✅ `ActivateProject_WithoutTasks_ShouldFail`
3. ✅ `ActivateProject_ByNonOwner_ShouldFail`
4. ✅ `CompleteTask_AlreadyCompleted_ShouldFail`
5. ✅ `CompleteTask_ShouldGenerateAuditAndNotification`

## 📡 API Endpoints

### Autenticación

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

### Proyectos

```http
# Crear proyecto
POST /api/projects
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "My Project"
}

# Listar proyectos
GET /api/projects
Authorization: Bearer {token}

# Activar proyecto
PATCH /api/projects/{id}/activate
Authorization: Bearer {token}
```

### Tareas

```http
# Crear tarea
POST /api/projects/{projectId}/tasks
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "My Task"
}

# Listar tareas de un proyecto
GET /api/projects/{projectId}/tasks
Authorization: Bearer {token}

# Completar tarea
PATCH /api/tasks/{id}/complete
Authorization: Bearer {token}
```

## 🔐 Credenciales de Prueba

Puedes crear un usuario nuevo mediante el endpoint `/api/auth/register` o usar el frontend.

**Ejemplo de usuario de prueba:**
- Username: `admin`
- Email: `admin@example.com`
- Password: `admin123`

(Debes registrarlo primero usando el endpoint de registro)

## 📚 Documentación de la API

La documentación completa de la API está disponible en **Swagger UI**:

```
http://localhost:8080/swagger-ui.html
```

Desde Swagger puedes:
- Ver todos los endpoints disponibles
- Probar los endpoints directamente
- Autenticarte con JWT usando el botón "Authorize"

## 🎨 Frontend

El frontend es una aplicación simple en **HTML + JavaScript** que consume la API REST.

**Funcionalidades:**
- Login y registro de usuarios
- Listar proyectos del usuario
- Crear nuevos proyectos
- Activar proyectos
- Ver tareas de un proyecto
- Crear nuevas tareas
- Completar tareas

**Acceso:** http://localhost:8080

## 🏛️ Decisiones Técnicas

### 1. Clean Architecture + Hexagonal
- **Dominio puro**: Las entidades de dominio no tienen anotaciones de JPA
- **Puertos y Adaptadores**: Interfaces definen contratos, adaptadores implementan
- **Inversión de dependencias**: Todo apunta hacia el dominio

### 2. Seguridad
- **JWT**: Tokens con expiración de 24 horas
- **BCrypt**: Hash de contraseñas
- **Autorización**: Solo el propietario puede modificar sus recursos

### 3. Persistencia
- **JPA Entities separadas**: Mapeo entre entidades JPA y modelos de dominio
- **Soft Delete**: Eliminaciones lógicas con flag `deleted`
- **UUID**: Identificadores únicos universales

### 4. Auditoría y Notificaciones
- **Logging**: Implementación simple con SLF4J
- **Extensible**: Fácil reemplazar por servicios externos (email, SMS, etc.)

### 5. Testing
- **Unit Tests**: Pruebas de casos de uso sin Spring context
- **Mockito**: Mocking de dependencias
- **Cobertura**: Enfoque en reglas de negocio críticas

### 6. Docker
- **Multi-stage build**: Optimización del tamaño de imagen
- **Health checks**: Garantiza que la DB esté lista antes de iniciar la app
- **Networking**: Comunicación entre contenedores

## 📝 Modelo de Datos

```
User
├── id (UUID)
├── username (unique)
├── email (unique)
└── password (encrypted)

Project
├── id (UUID)
├── ownerId (UUID) → User
├── name
├── status (DRAFT | ACTIVE)
└── deleted (boolean)

Task
├── id (UUID)
├── projectId (UUID) → Project
├── title
├── completed (boolean)
└── deleted (boolean)
```

## 🤝 Contribución

Este proyecto fue desarrollado como una demostración de Clean Architecture y Hexagonal Architecture con Spring Boot.

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

**Desarrollado con ❤️ usando Clean Architecture y Hexagonal Architecture**
