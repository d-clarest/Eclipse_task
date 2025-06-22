package com.example.demo.repository;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskRepository {
    List<Task> findAll();

    void updateConfirmed(String taskName, boolean confirmed);
}
