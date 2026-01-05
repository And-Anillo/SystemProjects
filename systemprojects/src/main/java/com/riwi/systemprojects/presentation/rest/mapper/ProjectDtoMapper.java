package com.riwi.systemprojects.presentation.rest.mapper;

import com.riwi.systemprojects.domain.model.Project;
import com.riwi.systemprojects.presentation.rest.dto.ProjectResponse;

/**
 * Mapper for converting Project domain model to ProjectResponse DTO.
 */
public class ProjectDtoMapper {

    public static ProjectResponse toResponse(Project project) {
        if (project == null)
            return null;
        return new ProjectResponse(
                project.getId(),
                project.getOwnerId(),
                project.getName(),
                project.getStatus(),
                project.isDeleted());
    }
}
