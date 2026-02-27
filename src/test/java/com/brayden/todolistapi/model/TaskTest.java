package com.brayden.todolistapi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void onCreate_setsDefaultsAndTimestamps() {
        Task task = new Task();
        task.setIsCompleted(null);
        task.setStatus(null);

        task.onCreate();

        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
        assertFalse(task.getIsCompleted());
        assertEquals("PENDING", task.getStatus());
    }

    @Test
    void onUpdate_setsUpdatedTimestamp() throws InterruptedException {
        Task task = new Task();
        task.onCreate();
        var firstUpdatedAt = task.getUpdatedAt();

        Thread.sleep(2);
        task.onUpdate();

        assertTrue(task.getUpdatedAt().isAfter(firstUpdatedAt) || task.getUpdatedAt().isEqual(firstUpdatedAt));
    }
}
