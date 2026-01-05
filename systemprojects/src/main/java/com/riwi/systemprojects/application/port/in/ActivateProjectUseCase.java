package com.riwi.systemprojects.application.port.in;

import com.riwi.systemprojects.domain.model.Project;

import java.util.UUID;

/**
 * Input port (use case interface) for activating a project.
 */
public interface ActivateProjectUseCase {
    Project execute(UUID projectId);
}
