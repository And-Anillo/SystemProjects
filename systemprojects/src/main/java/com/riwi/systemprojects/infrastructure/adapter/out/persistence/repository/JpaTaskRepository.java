package com.riwi.systemprojects.infrastructure.adapter.out.persistence.repository;

import com.riwi.systemprojects.infrastructure.adapter.out.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for TaskEntity.
 */
@Repository
public interface JpaTaskRepository extends JpaRepository<TaskEntity, UUID> {
    List<TaskEntity> findByProjectId(UUID projectId);

    @Query("SELECT COUNT(t) FROM TaskEntity t WHERE t.projectId = :projectId AND t.deleted = false AND t.completed = false")
    long countActiveTasksByProjectId(@Param("projectId") UUID projectId);
}
