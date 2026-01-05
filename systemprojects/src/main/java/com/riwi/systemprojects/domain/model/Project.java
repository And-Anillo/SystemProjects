package com.riwi.systemprojects.domain.model;

import java.util.UUID;

/**
 * Domain entity representing a Project.
 * This class is framework-agnostic and contains only business logic.
 */
public class Project {
    private UUID id;
    private UUID ownerId;
    private String name;
    private ProjectStatus status;
    private boolean deleted;

    public Project() {
        this.status = ProjectStatus.DRAFT;
        this.deleted = false;
    }

    public Project(UUID id, UUID ownerId, String name, ProjectStatus status, boolean deleted) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.status = status;
        this.deleted = deleted;
    }

    // Business logic: Check if project can be activated
    public boolean canBeActivated() {
        return this.status == ProjectStatus.DRAFT && !this.deleted;
    }

    // Business logic: Activate project
    public void activate() {
        if (!canBeActivated()) {
            throw new IllegalStateException("Project cannot be activated");
        }
        this.status = ProjectStatus.ACTIVE;
    }

    // Business logic: Soft delete
    public void softDelete() {
        this.deleted = true;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
