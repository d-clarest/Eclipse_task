package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskService {
    List<Task> getAllTasks();

    void updateConfirmed(String taskName, boolean confirmed);
}
