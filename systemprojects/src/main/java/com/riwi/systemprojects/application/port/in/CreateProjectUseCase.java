package com.riwi.systemprojects.application.port.in;

import com.riwi.systemprojects.domain.model.Project;

import java.util.UUID;

/**
 * Input port (use case interface) for creating a project.
 */
public interface CreateProjectUseCase {
    Project execute(String name, UUID ownerId);
}
