package com.riwi.systemprojects.application.services;

import com.riwi.systemprojects.domain.ports.out.AuditLogPort;
import com.riwi.systemprojects.domain.ports.out.CurrentUserPort;
import com.riwi.systemprojects.domain.ports.out.NotificationPort;
import com.riwi.systemprojects.domain.ports.out.ProjectRepositoryPort;
import com.riwi.systemprojects.domain.ports.out.TaskRepositoryPort;
import com.riwi.systemprojects.domain.exception.BusinessRuleViolationException;
import com.riwi.systemprojects.domain.model.Project;
import com.riwi.systemprojects.domain.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CompleteTaskUseCase.
 * Tests are focused on business logic without Spring context.
 */
@ExtendWith(MockitoExtension.class)
class CompleteTaskUseCaseTest {

    @Mock
    private TaskRepositoryPort taskRepository;

    @Mock
    private ProjectRepositoryPort projectRepository;

    @Mock
    private CurrentUserPort currentUserPort;

    @Mock
    private AuditLogPort auditLogPort;

    @Mock
    private NotificationPort notificationPort;

    private CompleteTaskService completeTaskUseCase;

    @BeforeEach
    void setUp() {
        completeTaskUseCase = new CompleteTaskService(
                taskRepository,
                projectRepository,
                currentUserPort,
                auditLogPort,
                notificationPort);
    }

    @Test
    void CompleteTask_AlreadyCompleted_ShouldFail() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Task task = new Task();
        task.setId(taskId);
        task.setProjectId(projectId);
        task.setTitle("Test Task");
        task.setCompleted(true); // Already completed
        task.setDeleted(false);

        Project project = new Project();
        project.setId(projectId);
        project.setOwnerId(ownerId);

        when(currentUserPort.getCurrentUserId()).thenReturn(ownerId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> completeTaskUseCase.execute(taskId));

        assertTrue(exception.getMessage().contains("already completed"));
        verify(taskRepository, never()).save(any(Task.class));
        verify(auditLogPort, never()).register(anyString(), any(UUID.class));
        verify(notificationPort, never()).notify(anyString());
    }

    @Test
    void CompleteTask_ShouldGenerateAuditAndNotification() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Task task = new Task();
        task.setId(taskId);
        task.setProjectId(projectId);
        task.setTitle("Test Task");
        task.setCompleted(false);
        task.setDeleted(false);

        Project project = new Project();
        project.setId(projectId);
        project.setOwnerId(ownerId);

        when(currentUserPort.getCurrentUserId()).thenReturn(ownerId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task result = completeTaskUseCase.execute(taskId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isCompleted());
        verify(taskRepository, times(1)).save(task);
        verify(auditLogPort, times(1)).register(eq("TASK_COMPLETED"), eq(taskId));
        verify(notificationPort, times(1)).notify(anyString());
    }
}
