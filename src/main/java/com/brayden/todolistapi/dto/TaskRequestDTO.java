package com.brayden.todolistapi.dto;

import lombok.Data;

import java.util.Date;

// Switched from @Getter/@Setter to @Data for simpler test setup in the test files.
//@Getter
//@Setter
@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private String priority_level;
    private Date due_date;
    private Boolean is_completed;
}
