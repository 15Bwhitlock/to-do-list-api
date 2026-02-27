package com.brayden.todolistapi.controller.impl;

import com.brayden.todolistapi.controller.TaskController;
import com.brayden.todolistapi.dto.TaskRequestDTO;
import com.brayden.todolistapi.dto.TaskResponseDTO;
import com.brayden.todolistapi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Task management APIs")
public class TaskControllerImpl implements TaskController {
    private final TaskService taskService;

    public TaskControllerImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    @PostMapping
    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO task) {
        return taskService.createTask(task);
    }

    @Override
    @GetMapping
    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "Tasks retrieved")
    public List<TaskResponseDTO> findAllTasks() {
        return taskService.findAllTasks();
    }

    @Override
    @GetMapping("/paged")
    @Operation(summary = "Get tasks with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged tasks retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid paging or sort parameters")
    })
    public List<TaskResponseDTO> findAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return taskService.findAllTasks(page, size, sortBy, direction);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "400", description = "Invalid id")
    })
    public Optional<TaskResponseDTO> findTaskById(@PathVariable String id) {
        return taskService.findTaskById(id);
    }

    @Override
    @GetMapping("/search/title")
    @Operation(summary = "Search tasks by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks filtered by title"),
            @ApiResponse(responseCode = "400", description = "Invalid title filter")
    })
    public List<TaskResponseDTO> findTasksByTitle(@RequestParam String title) {
        return taskService.findTasksByTitle(title);
    }

    @Override
    @GetMapping("/search/status")
    @Operation(summary = "Search tasks by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks filtered by status"),
            @ApiResponse(responseCode = "400", description = "Invalid status filter")
    })
    public List<TaskResponseDTO> findTasksByStatus(@RequestParam String status) {
        return taskService.findTasksByStatus(status);
    }

    @Override
    @GetMapping("/search/priority")
    @Operation(summary = "Search tasks by priority level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks filtered by priority"),
            @ApiResponse(responseCode = "400", description = "Invalid priority filter")
    })
    public List<TaskResponseDTO> findTasksByPriorityLevel(@RequestParam Integer level) {
        return taskService.findTasksByPriorityLevel(level);
    }

    @Override
    @GetMapping("/search/due-date")
    @Operation(summary = "Search tasks by due date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks filtered by due date"),
            @ApiResponse(responseCode = "400", description = "Invalid due date filter")
    })
    public List<TaskResponseDTO> findTasksByDueDate(@RequestParam LocalDate date) {
        return taskService.findTasksByDueDate(date);
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Update a task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated"),
            @ApiResponse(responseCode = "400", description = "Invalid id or request")
    })
    public Optional<TaskResponseDTO> updateTask(@PathVariable String id, @Valid @RequestBody TaskRequestDTO task) {
        return taskService.updateTask(id, task);
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid id")
    })
    public boolean deleteTask(@PathVariable String id) {
        return taskService.deleteTask(id);
    }
}
