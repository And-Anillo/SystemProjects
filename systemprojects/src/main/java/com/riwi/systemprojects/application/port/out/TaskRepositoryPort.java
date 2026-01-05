package com.riwi.systemprojects.application.port.out;

import com.riwi.systemprojects.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Task persistence operations.
 * This interface defines the contract that infrastructure adapters must
 * implement.
 */
public interface TaskRepositoryPort {
    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findByProjectId(UUID projectId);

    List<Task> findAll();

    void delete(UUID id);
}
