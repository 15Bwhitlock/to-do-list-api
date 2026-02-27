package com.brayden.todolistapi.mapper;

import com.brayden.todolistapi.dto.TaskRequestDTO;
import com.brayden.todolistapi.dto.TaskResponseDTO;
import com.brayden.todolistapi.model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MapperTaskTest {

    @Test
    void dtoRequestToModel_mapsExpectedFields() {
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("Task 1");
        request.setDescription("Desc");
        request.setPriorityLevel(2);
        request.setStatus("PENDING");
        request.setDueDate(LocalDate.of(2026, 3, 1));
        request.setIsCompleted(false);

        Task model = MapperTask.dtoRequestToModel(request);

        assertEquals("Task 1", model.getTitle());
        assertEquals(2, model.getPriorityLevel());
        assertEquals("PENDING", model.getStatus());
        assertEquals(LocalDate.of(2026, 3, 1), model.getDueDate());
    }

    @Test
    void modelToResponseDto_mapsExpectedFields() {
        Task task = new Task();
        task.setId("id-1");
        task.setTitle("Task 1");
        task.setDescription("Desc");
        task.setPriorityLevel(4);
        task.setStatus("IN_PROGRESS");

        TaskResponseDTO response = MapperTask.modelToResponseDto(task);

        assertNotNull(response);
        assertEquals("id-1", response.getId());
        assertEquals("Task 1", response.getTitle());
        assertEquals(4, response.getPriorityLevel());
        assertEquals("IN_PROGRESS", response.getStatus());
    }
}
