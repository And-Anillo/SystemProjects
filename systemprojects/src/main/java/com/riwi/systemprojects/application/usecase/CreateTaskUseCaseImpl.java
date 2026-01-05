package com.riwi.systemprojects.application.usecase;

import com.riwi.systemprojects.application.port.in.CreateTaskUseCase;
import com.riwi.systemprojects.application.port.out.CurrentUserPort;
import com.riwi.systemprojects.application.port.out.ProjectRepositoryPort;
import com.riwi.systemprojects.application.port.out.TaskRepositoryPort;
import com.riwi.systemprojects.domain.exception.ResourceNotFoundException;
import com.riwi.systemprojects.domain.exception.UnauthorizedAccessException;
import com.riwi.systemprojects.domain.model.Project;
import com.riwi.systemprojects.domain.model.Task;

import java.util.UUID;

/**
 * Use case implementation for creating a task.
 * Business Rule: Only the project owner can create tasks for that project.
 */
public class CreateTaskUseCaseImpl implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;
    private final CurrentUserPort currentUserPort;

    public CreateTaskUseCaseImpl(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    public Task execute(UUID projectId, String title) {
        // Get current user
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // Verify project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        // Verify ownership
        if (!project.getOwnerId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("You are not the owner of this project");
        }

        // Create task
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setProjectId(projectId);
        task.setTitle(title);
        task.setCompleted(false);
        task.setDeleted(false);

        return taskRepository.save(task);
    }
}
