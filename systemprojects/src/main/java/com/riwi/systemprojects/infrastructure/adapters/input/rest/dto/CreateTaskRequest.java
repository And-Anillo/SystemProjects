package com.riwi.systemprojects.infrastructure.adapters.input.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating a task.
 */
public class CreateTaskRequest {

    @NotBlank(message = "Task title is required")
    private String title;

    // Constructors
    public CreateTaskRequest() {
    }

    public CreateTaskRequest(String title) {
        this.title = title;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
