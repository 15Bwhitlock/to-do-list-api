package com.brayden.todolistapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "priorityLevel",
        "status",
        "dueDate",
        "isCompleted",
        "createdAt",
        "updatedAt"
})// this will make the values show in this order
public class TaskResponseDTO {
    private String id;
    private String title;
    private String description;
    private Integer priorityLevel;
    private String status;
    private LocalDate dueDate;
    private Boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
