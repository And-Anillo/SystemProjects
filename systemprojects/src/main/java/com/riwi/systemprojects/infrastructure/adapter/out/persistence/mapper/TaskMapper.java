package com.riwi.systemprojects.infrastructure.adapter.out.persistence.mapper;

import com.riwi.systemprojects.domain.model.Task;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.entity.TaskEntity;

/**
 * Mapper between Task domain model and TaskEntity JPA entity.
 */
public class TaskMapper {

    public static Task toDomain(TaskEntity entity) {
        if (entity == null)
            return null;
        return new Task(
                entity.getId(),
                entity.getProjectId(),
                entity.getTitle(),
                entity.isCompleted(),
                entity.isDeleted());
    }

    public static TaskEntity toEntity(Task domain) {
        if (domain == null)
            return null;
        return new TaskEntity(
                domain.getId(),
                domain.getProjectId(),
                domain.getTitle(),
                domain.isCompleted(),
                domain.isDeleted());
    }
}
