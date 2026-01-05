package com.riwi.systemprojects.infrastructure.config;

import com.riwi.systemprojects.application.port.in.*;
import com.riwi.systemprojects.application.port.out.*;
import com.riwi.systemprojects.application.usecase.*;
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
        return new CreateProjectUseCaseImpl(projectRepository);
    }

    @Bean
    public ActivateProjectUseCase activateProjectUseCase(
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort,
            AuditLogPort auditLogPort,
            NotificationPort notificationPort) {
        return new ActivateProjectUseCaseImpl(projectRepository, currentUserPort, auditLogPort, notificationPort);
    }

    @Bean
    public CreateTaskUseCase createTaskUseCase(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort) {
        return new CreateTaskUseCaseImpl(taskRepository, projectRepository, currentUserPort);
    }

    @Bean
    public CompleteTaskUseCase completeTaskUseCase(
            TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort,
            AuditLogPort auditLogPort,
            NotificationPort notificationPort) {
        return new CompleteTaskUseCaseImpl(taskRepository, projectRepository, currentUserPort, auditLogPort,
                notificationPort);
    }

    @Bean
    public GetProjectsUseCase getProjectsUseCase(
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort) {
        return new GetProjectsUseCaseImpl(projectRepository, currentUserPort);
    }

    @Bean
    public GetTasksUseCase getTasksUseCase(TaskRepositoryPort taskRepository) {
        return new GetTasksUseCaseImpl(taskRepository);
    }
}
