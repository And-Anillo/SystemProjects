# Guía Rápida de Ejecución

## 🚀 Inicio Rápido con Docker

```bash
# 1. Navegar al directorio del proyecto
cd systemprojects

# 2. Ejecutar con Docker Compose
docker compose up --build

# 3. Esperar a que los servicios estén listos
# La aplicación estará disponible en http://localhost:8080
```

## 🧪 Probar la Aplicación

### Opción 1: Usar el Frontend
1. Abrir http://localhost:8080 en el navegador
2. Registrar un nuevo usuario
3. Crear proyectos y tareas

### Opción 2: Usar Swagger UI
1. Abrir http://localhost:8080/swagger-ui.html
2. Usar el endpoint `/api/auth/register` para crear un usuario
3. Usar el endpoint `/api/auth/login` para obtener el token
4. Hacer clic en "Authorize" y pegar el token
5. Probar los demás endpoints

### Opción 3: Usar cURL

```bash
# Registrar usuario
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'

# Login (guardar el token de la respuesta)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'

# Crear proyecto (reemplazar TOKEN con el token obtenido)
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "name": "My First Project"
  }'

# Listar proyectos
curl -X GET http://localhost:8080/api/projects \
  -H "Authorization: Bearer TOKEN"

# Crear tarea (reemplazar PROJECT_ID)
curl -X POST http://localhost:8080/api/projects/PROJECT_ID/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "title": "My First Task"
  }'

# Activar proyecto (reemplazar PROJECT_ID)
curl -X PATCH http://localhost:8080/api/projects/PROJECT_ID/activate \
  -H "Authorization: Bearer TOKEN"

# Completar tarea (reemplazar TASK_ID)
curl -X PATCH http://localhost:8080/api/tasks/TASK_ID/complete \
  -H "Authorization: Bearer TOKEN"
```

## 📊 Ejecutar Pruebas

```bash
# Todas las pruebas
./mvnw test

# Solo pruebas de casos de uso
./mvnw test -Dtest=ActivateProjectUseCaseTest,CompleteTaskUseCaseTest
```

## 🛑 Detener la Aplicación

```bash
# Detener contenedores
docker compose down

# Detener y eliminar volúmenes (elimina la base de datos)
docker compose down -v
```

## 🔍 Ver Logs

```bash
# Ver logs de todos los servicios
docker compose logs -f

# Ver logs solo de la aplicación
docker compose logs -f app

# Ver logs solo de la base de datos
docker compose logs -f db
```

## 🗄️ Cargar Datos de Prueba

```bash
# Conectarse a la base de datos
docker exec -it projectsdb psql -U postgres -d projectsdb

# Ejecutar el script de datos de prueba
\i /path/to/init-data.sql

# O copiar y pegar el contenido del archivo init-data.sql
```

## 📝 Credenciales de Prueba

Después de cargar el script `init-data.sql`:

- **Usuario 1**: admin / password123
- **Usuario 2**: user1 / password123
- **Usuario 3**: user2 / password123

## ⚠️ Solución de Problemas

### Puerto 8080 ya en uso
```bash
# Cambiar el puerto en docker-compose.yml
ports:
  - "8081:8080"  # Usar 8081 en lugar de 8080
```

### Puerto 5432 ya en uso
```bash
# Cambiar el puerto de PostgreSQL
ports:
  - "5433:5432"  # Usar 5433 en lugar de 5432
```

### Error de conexión a la base de datos
```bash
# Verificar que la base de datos esté lista
docker compose logs db

# Reiniciar los servicios
docker compose restart
```

## 🏗️ Desarrollo Local (sin Docker)

```bash
# 1. Instalar PostgreSQL localmente
# 2. Crear base de datos
createdb projectsdb

# 3. Configurar application.properties si es necesario
# 4. Ejecutar la aplicación
./mvnw spring-boot:run
```

## 📚 Recursos Adicionales

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **Frontend**: http://localhost:8080
- **README completo**: Ver README.md
