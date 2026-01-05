package com.riwi.systemprojects.application.usecase;

import com.riwi.systemprojects.application.port.in.CreateProjectUseCase;
import com.riwi.systemprojects.application.port.out.ProjectRepositoryPort;
import com.riwi.systemprojects.domain.model.Project;
import com.riwi.systemprojects.domain.model.ProjectStatus;

import java.util.UUID;

/**
 * Use case implementation for creating a project.
 * Contains business logic without framework dependencies.
 */
public class CreateProjectUseCaseImpl implements CreateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;

    public CreateProjectUseCaseImpl(ProjectRepositoryPort projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project execute(String name, UUID ownerId) {
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName(name);
        project.setOwnerId(ownerId);
        project.setStatus(ProjectStatus.DRAFT);
        project.setDeleted(false);

        return projectRepository.save(project);
    }
}
