package com.brayden.todolistapi.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskResponseDTO {
    private String id;
    private String title;
    private String description;
    private Integer priorityLevel;
    private LocalDate dueDate;
    private Boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
