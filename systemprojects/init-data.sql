-- Esquema de Base de Datos y Datos de Prueba

-- Crear tablas
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS projects (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS tasks (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (project_id) REFERENCES projects(id)
);

-- Datos de prueba
-- Contraseña original para todos los usuarios: "password123"

-- Usuario de prueba 1
INSERT INTO users (id, username, email, password) 
VALUES 
    ('550e8400-e29b-41d4-a716-446655440001', 'admin', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
    ('550e8400-e29b-41d4-a716-446655440002', 'user1', 'user1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
    ('550e8400-e29b-41d4-a716-446655440003', 'user2', 'user2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON CONFLICT (id) DO NOTHING;

-- Proyectos de prueba
INSERT INTO projects (id, owner_id, name, status, deleted)
VALUES
    ('650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 'Website Redesign', 'ACTIVE', false),
    ('650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', 'Mobile App Development', 'DRAFT', false),
    ('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440002', 'Marketing Campaign', 'ACTIVE', false)
ON CONFLICT (id) DO NOTHING;

-- Tareas de prueba
INSERT INTO tasks (id, project_id, title, completed, deleted)
VALUES
    -- Tareas para Website Redesign (proyecto activo)
    ('750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440001', 'Design homepage mockup', true, false),
    ('750e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440001', 'Implement responsive layout', false, false),
    ('750e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440001', 'Add contact form', false, false),
    
    -- Tareas para Mobile App Development (proyecto draft)
    ('750e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440002', 'Setup React Native project', false, false),
    ('750e8400-e29b-41d4-a716-446655440005', '650e8400-e29b-41d4-a716-446655440002', 'Design app screens', false, false),
    
    -- Tareas para Marketing Campaign
    ('750e8400-e29b-41d4-a716-446655440006', '650e8400-e29b-41d4-a716-446655440003', 'Create social media content', true, false),
    ('750e8400-e29b-41d4-a716-446655440007', '650e8400-e29b-41d4-a716-446655440003', 'Launch email campaign', false, false)
ON CONFLICT (id) DO NOTHING;

-- Verificar datos insertados
SELECT 'Users created:' as info, COUNT(*) as count FROM users;
SELECT 'Projects created:' as info, COUNT(*) as count FROM projects;
SELECT 'Tasks created:' as info, COUNT(*) as count FROM tasks;
