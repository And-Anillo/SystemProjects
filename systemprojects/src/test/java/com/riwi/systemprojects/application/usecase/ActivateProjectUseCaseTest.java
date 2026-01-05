package com.riwi.systemprojects.application.usecase;

import com.riwi.systemprojects.application.port.out.AuditLogPort;
import com.riwi.systemprojects.application.port.out.CurrentUserPort;
import com.riwi.systemprojects.application.port.out.NotificationPort;
import com.riwi.systemprojects.application.port.out.ProjectRepositoryPort;
import com.riwi.systemprojects.domain.exception.BusinessRuleViolationException;
import com.riwi.systemprojects.domain.exception.ResourceNotFoundException;
import com.riwi.systemprojects.domain.exception.UnauthorizedAccessException;
import com.riwi.systemprojects.domain.model.Project;
import com.riwi.systemprojects.domain.model.ProjectStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ActivateProjectUseCase.
 * Tests are focused on business logic without Spring context.
 */
@ExtendWith(MockitoExtension.class)
class ActivateProjectUseCaseTest {

    @Mock
    private ProjectRepositoryPort projectRepository;

    @Mock
    private CurrentUserPort currentUserPort;

    @Mock
    private AuditLogPort auditLogPort;

    @Mock
    private NotificationPort notificationPort;

    private ActivateProjectUseCaseImpl activateProjectUseCase;

    @BeforeEach
    void setUp() {
        activateProjectUseCase = new ActivateProjectUseCaseImpl(
                projectRepository,
                currentUserPort,
                auditLogPort,
                notificationPort);
    }

    @Test
    void ActivateProject_WithTasks_ShouldSucceed() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Project project = new Project();
        project.setId(projectId);
        project.setOwnerId(ownerId);
        project.setName("Test Project");
        project.setStatus(ProjectStatus.DRAFT);
        project.setDeleted(false);

        when(currentUserPort.getCurrentUserId()).thenReturn(ownerId);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.countActiveTasksByProjectId(projectId)).thenReturn(1L);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = activateProjectUseCase.execute(projectId);

        // Assert
        assertNotNull(result);
        assertEquals(ProjectStatus.ACTIVE, result.getStatus());
        verify(auditLogPort, times(1)).register(eq("PROJECT_ACTIVATED"), eq(projectId));
        verify(notificationPort, times(1)).notify(anyString());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void ActivateProject_WithoutTasks_ShouldFail() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Project project = new Project();
        project.setId(projectId);
        project.setOwnerId(ownerId);
        project.setName("Test Project");
        project.setStatus(ProjectStatus.DRAFT);
        project.setDeleted(false);

        when(currentUserPort.getCurrentUserId()).thenReturn(ownerId);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.countActiveTasksByProjectId(projectId)).thenReturn(0L);

        // Act & Assert
        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> activateProjectUseCase.execute(projectId));

        assertTrue(exception.getMessage().contains("without at least one active task"));
        verify(projectRepository, never()).save(any(Project.class));
        verify(auditLogPort, never()).register(anyString(), any(UUID.class));
        verify(notificationPort, never()).notify(anyString());
    }

    @Test
    void ActivateProject_ByNonOwner_ShouldFail() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID differentUserId = UUID.randomUUID();

        Project project = new Project();
        project.setId(projectId);
        project.setOwnerId(ownerId);
        project.setName("Test Project");
        project.setStatus(ProjectStatus.DRAFT);
        project.setDeleted(false);

        when(currentUserPort.getCurrentUserId()).thenReturn(differentUserId);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act & Assert
        UnauthorizedAccessException exception = assertThrows(
                UnauthorizedAccessException.class,
                () -> activateProjectUseCase.execute(projectId));

        assertTrue(exception.getMessage().contains("not the owner"));
        verify(projectRepository, never()).save(any(Project.class));
        verify(auditLogPort, never()).register(anyString(), any(UUID.class));
        verify(notificationPort, never()).notify(anyString());
    }

    @Test
    void ActivateProject_NotFound_ShouldFail() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(currentUserPort.getCurrentUserId()).thenReturn(userId);
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> activateProjectUseCase.execute(projectId));
        verify(projectRepository, never()).save(any(Project.class));
    }
}
