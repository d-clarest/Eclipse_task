package com.example.demo.service;

import java.util.List;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    @Override
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @Override
    public void updateConfirmed(String taskName, LocalDate dueDate, boolean confirmed) {
        repository.updateConfirmed(taskName, dueDate, confirmed);
    }
}
