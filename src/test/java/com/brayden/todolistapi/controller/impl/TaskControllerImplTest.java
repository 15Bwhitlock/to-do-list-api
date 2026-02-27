package com.brayden.todolistapi.controller.impl;

import com.brayden.todolistapi.dto.TaskRequestDTO;
import com.brayden.todolistapi.dto.TaskResponseDTO;
import com.brayden.todolistapi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerImplTest {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskControllerImpl taskController;

    @Test
    void createTask_delegatesToService() {
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("Task 1");
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId("id-1");
        when(taskService.createTask(request)).thenReturn(response);

        TaskResponseDTO result = taskController.createTask(request);

        assertEquals("id-1", result.getId());
        verify(taskService).createTask(request);
    }

    @Test
    void findAllTasks_delegatesToService() {
        when(taskService.findAllTasks()).thenReturn(List.of(new TaskResponseDTO()));

        List<TaskResponseDTO> result = taskController.findAllTasks();

        assertEquals(1, result.size());
        verify(taskService).findAllTasks();
    }

    @Test
    void findTaskById_delegatesToService() {
        when(taskService.findTaskById("id-1")).thenReturn(Optional.of(new TaskResponseDTO()));

        Optional<TaskResponseDTO> result = taskController.findTaskById("id-1");

        assertTrue(result.isPresent());
        verify(taskService).findTaskById("id-1");
    }

    @Test
    void searchEndpoints_delegateToService() {
        when(taskService.findTasksByTitle("task")).thenReturn(List.of(new TaskResponseDTO()));
        when(taskService.findTasksByStatus("PENDING")).thenReturn(List.of(new TaskResponseDTO()));
        when(taskService.findTasksByPriorityLevel(3)).thenReturn(List.of(new TaskResponseDTO()));
        when(taskService.findTasksByDueDate(LocalDate.of(2026, 3, 1))).thenReturn(List.of(new TaskResponseDTO()));

        assertEquals(1, taskController.findTasksByTitle("task").size());
        assertEquals(1, taskController.findTasksByStatus("PENDING").size());
        assertEquals(1, taskController.findTasksByPriorityLevel(3).size());
        assertEquals(1, taskController.findTasksByDueDate(LocalDate.of(2026, 3, 1)).size());
    }

    @Test
    void updateAndDelete_delegateToService() {
        TaskRequestDTO request = new TaskRequestDTO();
        TaskResponseDTO response = new TaskResponseDTO();
        when(taskService.updateTask("id-1", request)).thenReturn(Optional.of(response));
        when(taskService.deleteTask("id-1")).thenReturn(true);

        Optional<TaskResponseDTO> updateResult = taskController.updateTask("id-1", request);
        boolean deleteResult = taskController.deleteTask("id-1");

        assertTrue(updateResult.isPresent());
        assertTrue(deleteResult);
        verify(taskService).updateTask("id-1", request);
        verify(taskService).deleteTask("id-1");
    }
}
