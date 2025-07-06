package com.example.demo.service.task;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskService {
    List<Task> getAllTasks();

    void addTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    int getTotalCompletedLevels();
}
