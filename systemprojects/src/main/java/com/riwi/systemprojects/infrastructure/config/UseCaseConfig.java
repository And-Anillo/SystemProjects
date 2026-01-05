package com.riwi.systemprojects.infrastructure.config;

import com.riwi.systemprojects.domain.ports.in.*;
import com.riwi.systemprojects.domain.ports.out.*;
import com.riwi.systemprojects.application.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Use Cases.
 * This wires up the use cases with their dependencies (ports).
 */
@Configuration
public class UseCaseConfig {

    @Bean
    public CreateProjectUseCase createProjectUseCase(ProjectRepositoryPort projectRepository) {
        return new CreateProjectService(projectRepository);
    }

    @Bean
    public ActivateProjectUseCase activateProjectUseCase(
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort,
            AuditLogPort auditLogPort,
            NotificationPort notificationPort) {
        return new ActivateProjectService(projectRepository, currentUserPort, auditLogPort, notificationPort);
    }

    @Bean
    public CreateTaskUseCase createTaskUseCase(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort) {
        return new CreateTaskService(taskRepository, projectRepository, currentUserPort);
    }

    @Bean
    public CompleteTaskUseCase completeTaskUseCase(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort,
            AuditLogPort auditLogPort,
            NotificationPort notificationPort) {
        return new CompleteTaskService(taskRepository, projectRepository, currentUserPort, auditLogPort,
                notificationPort);
    }

    @Bean
    public GetProjectsUseCase getProjectsUseCase(
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort) {
        return new GetProjectsService(projectRepository, currentUserPort);
    }

    @Bean
    public GetTasksUseCase getTasksUseCase(TaskRepositoryPort taskRepository) {
        return new GetTasksService(taskRepository);
    }
}
