// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// State
let token = localStorage.getItem('token');
let currentProjectId = null;
let currentProjectName = null;

// DOM Elements
const loginSection = document.getElementById('login-section');
const projectsSection = document.getElementById('projects-section');
const tasksSection = document.getElementById('tasks-section');
const logoutBtn = document.getElementById('logout-btn');

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    if (token) {
        showProjectsSection();
    }
    setupEventListeners();
});

// Event Listeners
function setupEventListeners() {
    document.getElementById('login-btn').addEventListener('click', login);
    document.getElementById('register-btn').addEventListener('click', register);
    document.getElementById('logout-btn').addEventListener('click', logout);
    document.getElementById('create-project-btn').addEventListener('click', createProject);
    document.getElementById('create-task-btn').addEventListener('click', createTask);
    document.getElementById('back-to-projects-btn').addEventListener('click', showProjectsSection);
}

// Authentication
async function register() {
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    if (!username || !email || !password) {
        showMessage('auth-message', 'Please fill all fields', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });

        if (response.ok) {
            const data = await response.json();
            token = data.token;
            localStorage.setItem('token', token);
            showMessage('auth-message', 'Registration successful!', 'success');
            setTimeout(showProjectsSection, 1000);
        } else {
            const error = await response.text();
            showMessage('auth-message', error, 'error');
        }
    } catch (error) {
        showMessage('auth-message', 'Error: ' + error.message, 'error');
    }
}

async function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    if (!username || !password) {
        showMessage('auth-message', 'Please fill username and password', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            token = data.token;
            localStorage.setItem('token', token);
            showMessage('auth-message', 'Login successful!', 'success');
            setTimeout(showProjectsSection, 1000);
        } else {
            const error = await response.text();
            showMessage('auth-message', error, 'error');
        }
    } catch (error) {
        showMessage('auth-message', 'Error: ' + error.message, 'error');
    }
}

function logout() {
    token = null;
    localStorage.removeItem('token');
    loginSection.style.display = 'block';
    projectsSection.style.display = 'none';
    tasksSection.style.display = 'none';
    logoutBtn.style.display = 'none';
}

// Projects
async function showProjectsSection() {
    loginSection.style.display = 'none';
    projectsSection.style.display = 'block';
    tasksSection.style.display = 'none';
    logoutBtn.style.display = 'block';
    await loadProjects();
}

async function loadProjects() {
    try {
        const response = await fetch(`${API_BASE_URL}/projects`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const projects = await response.json();
            displayProjects(projects);
        } else {
            showMessage('project-message', 'Error loading projects', 'error');
        }
    } catch (error) {
        showMessage('project-message', 'Error: ' + error.message, 'error');
    }
}

function displayProjects(projects) {
    const container = document.getElementById('projects-items');
    container.innerHTML = '';

    if (projects.length === 0) {
        container.innerHTML = '<p>No projects yet. Create your first project!</p>';
        return;
    }

    projects.forEach(project => {
        const projectDiv = document.createElement('div');
        projectDiv.className = 'project-item';
        projectDiv.innerHTML = `
            <h4>${project.name}</h4>
            <span class="status ${project.status.toLowerCase()}">${project.status}</span>
            <div class="item-actions">
                <button class="btn btn-primary" onclick="viewTasks('${project.id}', '${project.name}')">View Tasks</button>
                ${project.status === 'DRAFT' ? `<button class="btn btn-success" onclick="activateProject('${project.id}')">Activate</button>` : ''}
            </div>
        `;
        container.appendChild(projectDiv);
    });
}

async function createProject() {
    const name = document.getElementById('project-name').value;

    if (!name) {
        showMessage('project-message', 'Please enter project name', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/projects`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ name })
        });

        if (response.ok) {
            showMessage('project-message', 'Project created successfully!', 'success');
            document.getElementById('project-name').value = '';
            await loadProjects();
        } else {
            const error = await response.text();
            showMessage('project-message', error, 'error');
        }
    } catch (error) {
        showMessage('project-message', 'Error: ' + error.message, 'error');
    }
}

async function activateProject(projectId) {
    try {
        const response = await fetch(`${API_BASE_URL}/projects/${projectId}/activate`, {
            method: 'PATCH',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            showMessage('project-message', 'Project activated successfully!', 'success');
            await loadProjects();
        } else {
            const error = await response.json();
            showMessage('project-message', error.message || 'Error activating project', 'error');
        }
    } catch (error) {
        showMessage('project-message', 'Error: ' + error.message, 'error');
    }
}

// Tasks
async function viewTasks(projectId, projectName) {
    currentProjectId = projectId;
    currentProjectName = projectName;
    document.getElementById('current-project-name').textContent = projectName;

    projectsSection.style.display = 'none';
    tasksSection.style.display = 'block';

    await loadTasks();
}

async function loadTasks() {
    try {
        const response = await fetch(`${API_BASE_URL}/projects/${currentProjectId}/tasks`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const tasks = await response.json();
            displayTasks(tasks);
        } else {
            showMessage('task-message', 'Error loading tasks', 'error');
        }
    } catch (error) {
        showMessage('task-message', 'Error: ' + error.message, 'error');
    }
}

function displayTasks(tasks) {
    const container = document.getElementById('tasks-items');
    container.innerHTML = '';

    if (tasks.length === 0) {
        container.innerHTML = '<p>No tasks yet. Create your first task!</p>';
        return;
    }

    tasks.forEach(task => {
        const taskDiv = document.createElement('div');
        taskDiv.className = 'task-item';
        const status = task.completed ? 'completed' : 'pending';
        taskDiv.innerHTML = `
            <h4>${task.title}</h4>
            <span class="status ${status}">${task.completed ? 'Completed' : 'Pending'}</span>
            <div class="item-actions">
                ${!task.completed ? `<button class="btn btn-success" onclick="completeTask('${task.id}')">Complete</button>` : ''}
            </div>
        `;
        container.appendChild(taskDiv);
    });
}

async function createTask() {
    const title = document.getElementById('task-title').value;

    if (!title) {
        showMessage('task-message', 'Please enter task title', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/projects/${currentProjectId}/tasks`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ title })
        });

        if (response.ok) {
            showMessage('task-message', 'Task created successfully!', 'success');
            document.getElementById('task-title').value = '';
            await loadTasks();
        } else {
            const error = await response.text();
            showMessage('task-message', error, 'error');
        }
    } catch (error) {
        showMessage('task-message', 'Error: ' + error.message, 'error');
    }
}

async function completeTask(taskId) {
    try {
        const response = await fetch(`${API_BASE_URL}/tasks/${taskId}/complete`, {
            method: 'PATCH',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            showMessage('task-message', 'Task completed successfully!', 'success');
            await loadTasks();
        } else {
            const error = await response.json();
            showMessage('task-message', error.message || 'Error completing task', 'error');
        }
    } catch (error) {
        showMessage('task-message', 'Error: ' + error.message, 'error');
    }
}

// Utility
function showMessage(elementId, message, type) {
    const messageEl = document.getElementById(elementId);
    messageEl.textContent = message;
    messageEl.className = `message ${type}`;
    messageEl.style.display = 'block';

    setTimeout(() => {
        messageEl.style.display = 'none';
    }, 5000);
}
