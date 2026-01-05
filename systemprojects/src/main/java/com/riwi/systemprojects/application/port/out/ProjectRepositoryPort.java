package com.riwi.systemprojects.application.port.out;

import com.riwi.systemprojects.domain.model.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Project persistence operations.
 * This interface defines the contract that infrastructure adapters must
 * implement.
 */
public interface ProjectRepositoryPort {
    Project save(Project project);

    Optional<Project> findById(UUID id);

    List<Project> findByOwnerId(UUID ownerId);

    List<Project> findAll();

    void delete(UUID id);

    long countActiveTasksByProjectId(UUID projectId);
}
