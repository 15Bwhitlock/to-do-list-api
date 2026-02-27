package com.brayden.todolistapi.repository;

import com.brayden.todolistapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByStatusIgnoreCase(String status);

    List<Task> findByPriorityLevel(Integer priorityLevel);

    List<Task> findByDueDate(LocalDate dueDate);
}
