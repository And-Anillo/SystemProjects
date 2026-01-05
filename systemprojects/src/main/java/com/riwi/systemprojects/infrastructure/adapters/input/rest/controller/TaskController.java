package com.riwi.systemprojects.infrastructure.adapters.input.rest.controller;

import com.riwi.systemprojects.domain.ports.in.CompleteTaskUseCase;
import com.riwi.systemprojects.domain.ports.in.CreateTaskUseCase;
import com.riwi.systemprojects.domain.ports.in.GetTasksUseCase;
import com.riwi.systemprojects.domain.model.Task;
import com.riwi.systemprojects.infrastructure.adapters.input.rest.dto.CreateTaskRequest;
import com.riwi.systemprojects.infrastructure.adapters.input.rest.dto.TaskResponse;
import com.riwi.systemprojects.infrastructure.adapters.input.rest.mapper.TaskDtoMapper;
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
 * REST Controller for task endpoints.
 * No business logic here - delegates to use cases.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Tasks", description = "Endpoints for managing tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final GetTasksUseCase getTasksUseCase;

    public TaskController(
            CreateTaskUseCase createTaskUseCase,
            CompleteTaskUseCase completeTaskUseCase,
            GetTasksUseCase getTasksUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.completeTaskUseCase = completeTaskUseCase;
        this.getTasksUseCase = getTasksUseCase;
    }

    @PostMapping("/projects/{projectId}/tasks")
    @Operation(summary = "Create a new task", description = "Creates a new task for a project")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable UUID projectId,
            @Valid @RequestBody CreateTaskRequest request) {
        Task task = createTaskUseCase.execute(projectId, request.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskDtoMapper.toResponse(task));
    }

    @GetMapping("/projects/{projectId}/tasks")
    @Operation(summary = "Get tasks by project", description = "Returns all tasks for a specific project")
    public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable UUID projectId) {
        List<Task> tasks = getTasksUseCase.getByProjectId(projectId);
        List<TaskResponse> response = tasks.stream()
                .map(TaskDtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/tasks/{id}/complete")
    @Operation(summary = "Complete a task", description = "Marks a task as completed")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable UUID id) {
        Task task = completeTaskUseCase.execute(id);
        return ResponseEntity.ok(TaskDtoMapper.toResponse(task));
    }
}
