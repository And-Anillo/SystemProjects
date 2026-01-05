package com.riwi.systemprojects.application.usecase;

import com.riwi.systemprojects.application.port.in.CompleteTaskUseCase;
import com.riwi.systemprojects.application.port.out.AuditLogPort;
import com.riwi.systemprojects.application.port.out.CurrentUserPort;
import com.riwi.systemprojects.application.port.out.NotificationPort;
import com.riwi.systemprojects.application.port.out.ProjectRepositoryPort;
import com.riwi.systemprojects.application.port.out.TaskRepositoryPort;
import com.riwi.systemprojects.domain.exception.BusinessRuleViolationException;
import com.riwi.systemprojects.domain.exception.ResourceNotFoundException;
import com.riwi.systemprojects.domain.exception.UnauthorizedAccessException;
import com.riwi.systemprojects.domain.model.Project;
import com.riwi.systemprojects.domain.model.Task;

import java.util.UUID;

/**
 * Use case implementation for completing a task.
 * Business Rule: A completed task cannot be modified.
 * Business Rule: Only the project owner can complete tasks.
 */
public class CompleteTaskUseCaseImpl implements CompleteTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;
    private final CurrentUserPort currentUserPort;
    private final AuditLogPort auditLogPort;
    private final NotificationPort notificationPort;

    public CompleteTaskUseCaseImpl(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort,
            AuditLogPort auditLogPort,
            NotificationPort notificationPort) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.currentUserPort = currentUserPort;
        this.auditLogPort = auditLogPort;
        this.notificationPort = notificationPort;
    }

    @Override
    public Task execute(UUID taskId) {
        // Get current user
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // Find task
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        // Find project to verify ownership
        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + task.getProjectId()));

        // Verify ownership
        if (!project.getOwnerId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("You are not the owner of this task's project");
        }

        // Business Rule: Task cannot be completed if already completed
        if (task.isCompleted()) {
            throw new BusinessRuleViolationException("Task is already completed and cannot be modified");
        }

        // Complete task (domain logic)
        task.complete();

        // Save
        Task savedTask = taskRepository.save(task);

        // Audit and notification
        auditLogPort.register("TASK_COMPLETED", taskId);
        notificationPort.notify("Task '" + task.getTitle() + "' has been completed");

        return savedTask;
    }
}
