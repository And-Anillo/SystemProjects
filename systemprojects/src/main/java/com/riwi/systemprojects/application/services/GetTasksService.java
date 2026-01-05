package com.riwi.systemprojects.application.services;

import com.riwi.systemprojects.domain.ports.in.GetTasksUseCase;
import com.riwi.systemprojects.domain.ports.out.TaskRepositoryPort;
import com.riwi.systemprojects.domain.exception.ResourceNotFoundException;
import com.riwi.systemprojects.domain.model.Task;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use case implementation for getting tasks.
 */
public class GetTasksService implements GetTasksUseCase {

    private final TaskRepositoryPort taskRepository;

    public GetTasksService(TaskRepositoryPort taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getByProjectId(UUID projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream()
                .filter(t -> !t.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Task getById(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    }
}
