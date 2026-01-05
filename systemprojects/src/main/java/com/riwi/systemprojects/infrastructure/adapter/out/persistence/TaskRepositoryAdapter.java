package com.riwi.systemprojects.infrastructure.adapter.out.persistence;

import com.riwi.systemprojects.application.port.out.TaskRepositoryPort;
import com.riwi.systemprojects.domain.model.Task;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.entity.TaskEntity;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.mapper.TaskMapper;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.repository.JpaTaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter that implements TaskRepositoryPort using JPA.
 * This is part of the infrastructure layer.
 */
@Component
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final JpaTaskRepository jpaTaskRepository;

    public TaskRepositoryAdapter(JpaTaskRepository jpaTaskRepository) {
        this.jpaTaskRepository = jpaTaskRepository;
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = TaskMapper.toEntity(task);
        TaskEntity saved = jpaTaskRepository.save(entity);
        return TaskMapper.toDomain(saved);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaTaskRepository.findById(id)
                .map(TaskMapper::toDomain);
    }

    @Override
    public List<Task> findByProjectId(UUID projectId) {
        return jpaTaskRepository.findByProjectId(projectId)
                .stream()
                .map(TaskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findAll() {
        return jpaTaskRepository.findAll()
                .stream()
                .map(TaskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        jpaTaskRepository.deleteById(id);
    }
}
