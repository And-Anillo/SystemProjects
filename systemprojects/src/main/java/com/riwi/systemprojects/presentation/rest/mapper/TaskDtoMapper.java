package com.riwi.systemprojects.presentation.rest.mapper;

import com.riwi.systemprojects.domain.model.Task;
import com.riwi.systemprojects.presentation.rest.dto.TaskResponse;

/**
 * Mapper for converting Task domain model to TaskResponse DTO.
 */
public class TaskDtoMapper {

    public static TaskResponse toResponse(Task task) {
        if (task == null)
            return null;
        return new TaskResponse(
                task.getId(),
                task.getProjectId(),
                task.getTitle(),
                task.isCompleted(),
                task.isDeleted());
    }
}
