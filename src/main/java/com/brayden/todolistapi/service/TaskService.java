package com.brayden.todolistapi.service;

import com.brayden.todolistapi.dto.TaskRequestDTO;
import com.brayden.todolistapi.dto.TaskResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO task);

    List<TaskResponseDTO> findAllTasks();

    List<TaskResponseDTO> findAllTasks(int page, int size, String sortBy, String direction);

    Optional<TaskResponseDTO> findTaskById(String id);

    List<TaskResponseDTO> findTasksByTitle(String title);

    List<TaskResponseDTO> findTasksByStatus(String status);

    List<TaskResponseDTO> findTasksByPriorityLevel(Integer level);

    List<TaskResponseDTO> findTasksByDueDate(LocalDate date);

    Optional<TaskResponseDTO> updateTask(String id, TaskRequestDTO task);

    boolean deleteTask(String id);
}
