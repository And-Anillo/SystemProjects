package com.riwi.systemprojects.application.usecase;

import com.riwi.systemprojects.application.port.in.GetProjectsUseCase;
import com.riwi.systemprojects.application.port.out.CurrentUserPort;
import com.riwi.systemprojects.application.port.out.ProjectRepositoryPort;
import com.riwi.systemprojects.domain.exception.ResourceNotFoundException;
import com.riwi.systemprojects.domain.model.Project;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use case implementation for getting projects.
 * Returns only projects owned by the current user.
 */
public class GetProjectsUseCaseImpl implements GetProjectsUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final CurrentUserPort currentUserPort;

    public GetProjectsUseCaseImpl(ProjectRepositoryPort projectRepository, CurrentUserPort currentUserPort) {
        this.projectRepository = projectRepository;
        this.currentUserPort = currentUserPort;
    }

    @Override
    public List<Project> execute() {
        UUID currentUserId = currentUserPort.getCurrentUserId();
        return projectRepository.findByOwnerId(currentUserId)
                .stream()
                .filter(p -> !p.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Project getById(UUID projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
    }
}
