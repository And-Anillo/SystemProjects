package com.riwi.systemprojects.application.port.in;

import com.riwi.systemprojects.domain.model.Task;

import java.util.UUID;

/**
 * Input port (use case interface) for completing a task.
 */
public interface CompleteTaskUseCase {
    Task execute(UUID taskId);
}
