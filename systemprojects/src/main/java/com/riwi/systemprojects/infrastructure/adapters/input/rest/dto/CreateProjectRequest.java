package com.riwi.systemprojects.infrastructure.adapters.input.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating a project.
 */
public class CreateProjectRequest {

    @NotBlank(message = "Project name is required")
    private String name;

    // Constructors
    public CreateProjectRequest() {
    }

    public CreateProjectRequest(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
