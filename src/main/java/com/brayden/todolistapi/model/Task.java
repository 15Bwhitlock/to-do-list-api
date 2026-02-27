package com.brayden.todolistapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "priority_level", nullable = false)
    private Integer priorityLevel;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist // only runs when a new task is made
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now; // set the createdAt timeStamp to be just before it is added to the table so that the time is correct
        updatedAt = now; // same as createdAt
        if (isCompleted == null) {
            isCompleted = false; // sets all new tasks as not completed
        }
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate // runs on the task whenever a update is make on a task
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
