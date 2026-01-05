package com.riwi.systemprojects.presentation.rest.controller;

import com.riwi.systemprojects.application.port.in.ActivateProjectUseCase;
import com.riwi.systemprojects.application.port.in.CreateProjectUseCase;
import com.riwi.systemprojects.application.port.in.GetProjectsUseCase;
import com.riwi.systemprojects.application.port.out.CurrentUserPort;
import com.riwi.systemprojects.domain.model.Project;
import com.riwi.systemprojects.presentation.rest.dto.CreateProjectRequest;
import com.riwi.systemprojects.presentation.rest.dto.ProjectResponse;
import com.riwi.systemprojects.presentation.rest.mapper.ProjectDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for project endpoints.
 * No business logic here - delegates to use cases.
 */
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Endpoints for managing projects")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final ActivateProjectUseCase activateProjectUseCase;
    private final GetProjectsUseCase getProjectsUseCase;
    private final CurrentUserPort currentUserPort;

    public ProjectController(
            CreateProjectUseCase createProjectUseCase,
            ActivateProjectUseCase activateProjectUseCase,
            GetProjectsUseCase getProjectsUseCase,
            CurrentUserPort currentUserPort) {
        this.createProjectUseCase = createProjectUseCase;
        this.activateProjectUseCase = activateProjectUseCase;
        this.getProjectsUseCase = getProjectsUseCase;
        this.currentUserPort = currentUserPort;
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project in DRAFT status")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        UUID currentUserId = currentUserPort.getCurrentUserId();
        Project project = createProjectUseCase.execute(request.getName(), currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectDtoMapper.toResponse(project));
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns all projects owned by the current user")
    public ResponseEntity<List<ProjectResponse>> getProjects() {
        List<Project> projects = getProjectsUseCase.execute();
        List<ProjectResponse> response = projects.stream()
                .map(ProjectDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a project", description = "Activates a project if it has at least one active task")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ProjectResponse> activateProject(@PathVariable UUID id) {
        Project project = activateProjectUseCase.execute(id);
        return ResponseEntity.ok(ProjectDtoMapper.toResponse(project));
    }
}
