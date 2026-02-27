package com.brayden.todolistapi.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequestDTO {
    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 5000)
    private String description;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer priorityLevel;

    @Pattern(regexp = "PENDING|IN_PROGRESS|COMPLETED")
    private String status;

    private LocalDate dueDate;

    private Boolean isCompleted;
}
