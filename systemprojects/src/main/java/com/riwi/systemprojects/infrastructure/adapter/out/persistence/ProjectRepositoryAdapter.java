package com.riwi.systemprojects.infrastructure.adapter.out.persistence;

import com.riwi.systemprojects.application.port.out.ProjectRepositoryPort;
import com.riwi.systemprojects.domain.model.Project;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.mapper.ProjectMapper;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.repository.JpaProjectRepository;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.repository.JpaTaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter that implements ProjectRepositoryPort using JPA.
 * This is part of the infrastructure layer.
 */
@Component
public class ProjectRepositoryAdapter implements ProjectRepositoryPort {

    private final JpaProjectRepository jpaProjectRepository;
    private final JpaTaskRepository jpaTaskRepository;

    public ProjectRepositoryAdapter(JpaProjectRepository jpaProjectRepository, JpaTaskRepository jpaTaskRepository) {
        this.jpaProjectRepository = jpaProjectRepository;
        this.jpaTaskRepository = jpaTaskRepository;
    }

    @Override
    public Project save(Project project) {
        ProjectEntity entity = ProjectMapper.toEntity(project);
        ProjectEntity saved = jpaProjectRepository.save(entity);
        return ProjectMapper.toDomain(saved);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return jpaProjectRepository.findById(id)
                .map(ProjectMapper::toDomain);
    }

    @Override
    public List<Project> findByOwnerId(UUID ownerId) {
        return jpaProjectRepository.findByOwnerId(ownerId)
                .stream()
                .map(ProjectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> findAll() {
        return jpaProjectRepository.findAll()
                .stream()
                .map(ProjectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        jpaProjectRepository.deleteById(id);
    }

    @Override
    public long countActiveTasksByProjectId(UUID projectId) {
        return jpaTaskRepository.countActiveTasksByProjectId(projectId);
    }
}
