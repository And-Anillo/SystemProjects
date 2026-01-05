package com.riwi.systemprojects.application.usecase;

import com.riwi.systemprojects.application.port.in.ActivateProjectUseCase;
import com.riwi.systemprojects.application.port.out.AuditLogPort;
import com.riwi.systemprojects.application.port.out.CurrentUserPort;
import com.riwi.systemprojects.application.port.out.NotificationPort;
import com.riwi.systemprojects.application.port.out.ProjectRepositoryPort;
import com.riwi.systemprojects.domain.exception.BusinessRuleViolationException;
import com.riwi.systemprojects.domain.exception.ResourceNotFoundException;
import com.riwi.systemprojects.domain.exception.UnauthorizedAccessException;
import com.riwi.systemprojects.domain.model.Project;

import java.util.UUID;

/**
 * Use case implementation for activating a project.
 * Business Rule: A project can only be activated if it has at least one active
 * task.
 * Business Rule: Only the owner can activate the project.
 */
public class ActivateProjectUseCaseImpl implements ActivateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final CurrentUserPort currentUserPort;
    private final AuditLogPort auditLogPort;
    private final NotificationPort notificationPort;

    public ActivateProjectUseCaseImpl(
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort,
            AuditLogPort auditLogPort,
            NotificationPort notificationPort) {
        this.projectRepository = projectRepository;
        this.currentUserPort = currentUserPort;
        this.auditLogPort = auditLogPort;
        this.notificationPort = notificationPort;
    }

    @Override
    public Project execute(UUID projectId) {
        // Get current user
        UUID currentUserId = currentUserPort.getCurrentUserId();

        // Find project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        // Verify ownership
        if (!project.getOwnerId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("You are not the owner of this project");
        }

        // Business Rule: Project must have at least one active task
        long activeTasksCount = projectRepository.countActiveTasksByProjectId(projectId);
        if (activeTasksCount == 0) {
            throw new BusinessRuleViolationException("Project cannot be activated without at least one active task");
        }

        // Activate project (domain logic)
        project.activate();

        // Save
        Project savedProject = projectRepository.save(project);

        // Audit and notification
        auditLogPort.register("PROJECT_ACTIVATED", projectId);
        notificationPort.notify("Project '" + project.getName() + "' has been activated");

        return savedProject;
    }
}
