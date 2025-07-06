package com.example.demo.repository.task;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskRepository {
    List<Task> findAll();
}
