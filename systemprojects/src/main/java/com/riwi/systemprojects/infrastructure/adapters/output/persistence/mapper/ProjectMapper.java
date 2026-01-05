package com.riwi.systemprojects.infrastructure.adapters.output.persistence.mapper;

import com.riwi.systemprojects.domain.model.Project;
import com.riwi.systemprojects.infrastructure.adapters.output.persistence.entity.ProjectEntity;

/**
 * Mapper between Project domain model and ProjectEntity JPA entity.
 */
public class ProjectMapper {

    public static Project toDomain(ProjectEntity entity) {
        if (entity == null)
            return null;
        return new Project(
                entity.getId(),
                entity.getOwnerId(),
                entity.getName(),
                entity.getStatus(),
                entity.isDeleted());
    }

    public static ProjectEntity toEntity(Project domain) {
        if (domain == null)
            return null;
        return new ProjectEntity(
                domain.getId(),
                domain.getOwnerId(),
                domain.getName(),
                domain.getStatus(),
                domain.isDeleted());
    }
}
