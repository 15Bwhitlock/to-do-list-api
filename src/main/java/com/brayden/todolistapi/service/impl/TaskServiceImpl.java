package com.brayden.todolistapi.service.impl;

import com.brayden.todolistapi.dto.TaskRequestDTO;
import com.brayden.todolistapi.dto.TaskResponseDTO;
import com.brayden.todolistapi.exception.ApiException;
import com.brayden.todolistapi.mapper.MapperTask;
import com.brayden.todolistapi.model.Task;
import com.brayden.todolistapi.repository.TaskRepository;
import com.brayden.todolistapi.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "title", "description", "priorityLevel", "status", "dueDate", "isCompleted", "createdAt", "updatedAt"
    );
    private static final Set<String> ALLOWED_STATUS_VALUES = Set.of("PENDING", "IN_PROGRESS", "COMPLETED");

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO task) {
        validateTaskPayload(task);
        validateStatus(task.getStatus());
        Task model = MapperTask.dtoRequestToModel(task);
        if (model.getStatus() != null && !model.getStatus().isBlank()) {
            model.setStatus(model.getStatus().toUpperCase());
        }
        Task savedTask = taskRepository.save(model);
        return MapperTask.modelToResponseDto(savedTask);
    }

    @Override
    public List<TaskResponseDTO> findAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(MapperTask::modelToResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDTO> findAllTasks(int page, int size, String sortBy, String direction) {
        validatePageAndSize(page, size);
        validateSort(sortBy, direction);
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return taskRepository.findAll(PageRequest.of(page, size, sort))
                .stream()
                .map(MapperTask::modelToResponseDto)
                .toList();
    }

    @Override
    public Optional<TaskResponseDTO> findTaskById(String id) {
        validateId(id);
        return taskRepository.findById(id).map(MapperTask::modelToResponseDto);
    }

    @Override
    public List<TaskResponseDTO> findTasksByTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ApiException("Title must not be blank", HttpStatus.BAD_REQUEST);
        }
        return taskRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(MapperTask::modelToResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDTO> findTasksByStatus(String status) {
        validateStatus(status);
        return taskRepository.findByStatusIgnoreCase(status)
                .stream()
                .map(MapperTask::modelToResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDTO> findTasksByPriorityLevel(Integer level) {
        if (level == null || level < 1 || level > 5) {
            throw new ApiException("Priority level must be between 1 and 5", HttpStatus.BAD_REQUEST);
        }
        return taskRepository.findByPriorityLevel(level)
                .stream()
                .map(MapperTask::modelToResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDTO> findTasksByDueDate(LocalDate date) {
        if (date == null) {
            throw new ApiException("Due date is required", HttpStatus.BAD_REQUEST);
        }
        return taskRepository.findByDueDate(date)
                .stream()
                .map(MapperTask::modelToResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public Optional<TaskResponseDTO> updateTask(String id, TaskRequestDTO task) {
        validateId(id);
        validateTaskPayload(task);
        validateStatus(task.getStatus());
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setPriorityLevel(task.getPriorityLevel());
            existingTask.setDueDate(task.getDueDate());
            if (task.getIsCompleted() != null) {
                existingTask.setIsCompleted(task.getIsCompleted());
            }
            if (task.getStatus() != null && !task.getStatus().isBlank()) {
                existingTask.setStatus(task.getStatus().toUpperCase());
            }
            Task updatedTask = taskRepository.save(existingTask);
            return MapperTask.modelToResponseDto(updatedTask);
        });
    }

    @Override
    @Transactional
    public boolean deleteTask(String id) {
        validateId(id);
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }

    private void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new ApiException("Task id must not be blank", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateTaskPayload(TaskRequestDTO task) {
        if (task == null) {
            throw new ApiException("Task payload is required", HttpStatus.BAD_REQUEST);
        }
    }

    private void validatePageAndSize(int page, int size) {
        if (page < 0) {
            throw new ApiException("Page must be greater than or equal to 0", HttpStatus.BAD_REQUEST);
        }
        if (size < 1 || size > 100) {
            throw new ApiException("Size must be between 1 and 100", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateSort(String sortBy, String direction) {
        if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new ApiException("Invalid sort field: " + sortBy, HttpStatus.BAD_REQUEST);
        }
        if (direction == null || (!"asc".equalsIgnoreCase(direction) && !"desc".equalsIgnoreCase(direction))) {
            throw new ApiException("Direction must be 'asc' or 'desc'", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.isBlank()) {
            return;
        }
        if (!ALLOWED_STATUS_VALUES.contains(status.toUpperCase())) {
            throw new ApiException("Status must be one of: PENDING, IN_PROGRESS, COMPLETED", HttpStatus.BAD_REQUEST);
        }
    }
}
