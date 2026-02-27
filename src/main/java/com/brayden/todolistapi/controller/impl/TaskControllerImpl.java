package com.brayden.todolistapi.controller.impl;

import com.brayden.todolistapi.controller.TaskController;
import com.brayden.todolistapi.dto.TaskRequestDTO;
import com.brayden.todolistapi.dto.TaskResponseDTO;
import com.brayden.todolistapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskControllerImpl implements TaskController {
    private final TaskService taskService;

    public TaskControllerImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    @PostMapping
    public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO task) {
        return taskService.createTask(task);
    }

    @Override
    @GetMapping
    public List<TaskResponseDTO> findAllTasks() {
        return taskService.findAllTasks();
    }

    @Override
    @GetMapping("/paged")
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
    public Optional<TaskResponseDTO> findTaskById(@PathVariable String id) {
        return taskService.findTaskById(id);
    }

    @Override
    @GetMapping("/search/title")
    public List<TaskResponseDTO> findTasksByTitle(@RequestParam String title) {
        return taskService.findTasksByTitle(title);
    }

    @Override
    @GetMapping("/search/status")
    public List<TaskResponseDTO> findTasksByStatus(@RequestParam String status) {
        return taskService.findTasksByStatus(status);
    }

    @Override
    @GetMapping("/search/priority")
    public List<TaskResponseDTO> findTasksByPriorityLevel(@RequestParam Integer level) {
        return taskService.findTasksByPriorityLevel(level);
    }

    @Override
    @GetMapping("/search/due-date")
    public List<TaskResponseDTO> findTasksByDueDate(@RequestParam LocalDate date) {
        return taskService.findTasksByDueDate(date);
    }

    @Override
    @PutMapping("/{id}")
    public Optional<TaskResponseDTO> updateTask(@PathVariable String id, @Valid @RequestBody TaskRequestDTO task) {
        return taskService.updateTask(id, task);
    }

    @Override
    @DeleteMapping("/{id}")
    public boolean deleteTask(@PathVariable String id) {
        return taskService.deleteTask(id);
    }
}
