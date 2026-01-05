package com.riwi.systemprojects.application.port.in;

import com.riwi.systemprojects.domain.model.Task;

import java.util.List;
import java.util.UUID;

/**
 * Input port (use case interface) for getting tasks.
 */
public interface GetTasksUseCase {
    List<Task> getByProjectId(UUID projectId);

    Task getById(UUID taskId);
}
