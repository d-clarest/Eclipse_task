package com.example.demo.repository.task;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskRepository {
    List<Task> findAll();
    Task findById(int id);
    void insertTask(Task task);
    void updateTask(Task task);
    void deleteById(int id);
    int sumCompletedLevels();
}
