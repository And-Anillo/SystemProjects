package com.riwi.systemprojects.domain.ports.in;

import com.riwi.systemprojects.domain.model.Task;

import java.util.UUID;

/**
 * Input port (use case interface) for creating a task.
 */
public interface CreateTaskUseCase {
    Task execute(UUID projectId, String title);
}
