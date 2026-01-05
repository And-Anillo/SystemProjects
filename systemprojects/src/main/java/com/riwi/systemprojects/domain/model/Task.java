package com.riwi.systemprojects.domain.model;

import java.util.UUID;

/**
 * Domain entity representing a Task.
 * This class is framework-agnostic and contains only business logic.
 */
public class Task {
    private UUID id;
    private UUID projectId;
    private String title;
    private boolean completed;
    private boolean deleted;

    public Task() {
        this.completed = false;
        this.deleted = false;
    }

    public Task(UUID id, UUID projectId, String title, boolean completed, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.completed = completed;
        this.deleted = deleted;
    }

    // Business logic: Check if task can be completed
    public boolean canBeCompleted() {
        return !this.completed && !this.deleted;
    }

    // Business logic: Complete task
    public void complete() {
        if (!canBeCompleted()) {
            throw new IllegalStateException("Task cannot be completed");
        }
        this.completed = true;
    }

    // Business logic: Soft delete
    public void softDelete() {
        this.deleted = true;
    }

    // Business logic: Check if task is active (not deleted and not completed)
    public boolean isActive() {
        return !this.deleted && !this.completed;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
