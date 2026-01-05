package com.riwi.systemprojects.domain.ports.in;

import com.riwi.systemprojects.domain.model.Project;

import java.util.List;
import java.util.UUID;

/**
 * Input port (use case interface) for getting projects.
 */
public interface GetProjectsUseCase {
    List<Project> execute();

    Project getById(UUID projectId);
}
