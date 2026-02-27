package com.brayden.todolistapi.service.impl;

import com.brayden.todolistapi.dto.TaskRequestDTO;
import com.brayden.todolistapi.dto.TaskResponseDTO;
import com.brayden.todolistapi.exception.ApiException;
import com.brayden.todolistapi.model.Task;
import com.brayden.todolistapi.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_savesAndReturnsMappedTask() {
        TaskRequestDTO request = buildRequest();
        Task savedTask = buildTask();
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponseDTO result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(savedTask.getId(), result.getId());
        assertEquals(savedTask.getTitle(), result.getTitle());
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        assertEquals(request.getTitle(), captor.getValue().getTitle());
    }

    @Test
    void createTask_throwsForInvalidStatus() {
        TaskRequestDTO request = buildRequest();
        request.setStatus("BAD_STATUS");

        ApiException ex = assertThrows(ApiException.class, () -> taskService.createTask(request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void findAllTasks_returnsMappedList() {
        when(taskRepository.findAll()).thenReturn(List.of(buildTask()));

        List<TaskResponseDTO> result = taskService.findAllTasks();

        assertEquals(1, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
    }

    @Test
    void findAllTasksPaged_returnsMappedList() {
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(buildTask())));

        List<TaskResponseDTO> result = taskService.findAllTasks(0, 10, "createdAt", "desc");

        assertEquals(1, result.size());
        verify(taskRepository).findAll(any(Pageable.class));
    }

    @Test
    void findAllTasksPaged_throwsForInvalidPaging() {
        ApiException ex = assertThrows(ApiException.class,
                () -> taskService.findAllTasks(-1, 10, "createdAt", "desc"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        verifyNoInteractions(taskRepository);
    }

    @Test
    void findTaskById_returnsEmptyWhenNotFound() {
        when(taskRepository.findById("id-1")).thenReturn(Optional.empty());

        Optional<TaskResponseDTO> result = taskService.findTaskById("id-1");

        assertTrue(result.isEmpty());
    }

    @Test
    void findTaskById_throwsForBlankId() {
        ApiException ex = assertThrows(ApiException.class, () -> taskService.findTaskById(" "));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void findTasksByTitle_throwsForBlankTitle() {
        ApiException ex = assertThrows(ApiException.class, () -> taskService.findTasksByTitle(" "));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void findTasksByPriorityLevel_throwsForOutOfRangeLevel() {
        ApiException ex = assertThrows(ApiException.class, () -> taskService.findTasksByPriorityLevel(10));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void findTasksByDueDate_throwsForNullDate() {
        ApiException ex = assertThrows(ApiException.class, () -> taskService.findTasksByDueDate(null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void updateTask_returnsUpdatedTaskWhenPresent() {
        Task existing = buildTask();
        TaskRequestDTO request = buildRequest();
        request.setTitle("Updated");
        when(taskRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<TaskResponseDTO> result = taskService.updateTask(existing.getId(), request);

        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getTitle());
    }

    @Test
    void deleteTask_returnsFalseWhenNotFound() {
        when(taskRepository.existsById("id-1")).thenReturn(false);

        boolean result = taskService.deleteTask("id-1");

        assertFalse(result);
        verify(taskRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteTask_returnsTrueWhenFound() {
        when(taskRepository.existsById("id-1")).thenReturn(true);

        boolean result = taskService.deleteTask("id-1");

        assertTrue(result);
        verify(taskRepository).deleteById("id-1");
    }

    private TaskRequestDTO buildRequest() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Task 1");
        dto.setDescription("Desc");
        dto.setPriorityLevel(3);
        dto.setStatus("PENDING");
        dto.setDueDate(LocalDate.of(2026, 3, 1));
        dto.setIsCompleted(false);
        return dto;
    }

    private Task buildTask() {
        Task task = new Task();
        task.setId("id-1");
        task.setTitle("Task 1");
        task.setDescription("Desc");
        task.setPriorityLevel(3);
        task.setStatus("PENDING");
        task.setDueDate(LocalDate.of(2026, 3, 1));
        task.setIsCompleted(false);
        return task;
    }
}
