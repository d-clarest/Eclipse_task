package com.example.demo.service;

import java.util.List;
import java.time.LocalDate;

import com.example.demo.entity.Task;

public interface TaskService {
    List<Task> getAllTasks();

    void updateConfirmed(String taskName, LocalDate dueDate, boolean confirmed);
}
