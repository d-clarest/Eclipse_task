package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    @Override
    public List<Task> getAllTasks() {
        log.debug("Fetching all tasks");
        return repository.findAll();
    }

    @Override
    public void updateConfirmed(Task task) {
        log.debug("Updating confirm for task {} to {}", task.getTaskName(), task.isConfirmed());
        repository.updateConfirmed(task);
    }

    @Override
    public void updateDueDate(Task task) {
        log.debug("Updating due date of {} to {}", task.getTaskName(), task.getDueDate());
        repository.updateDueDate(task);
    }

    @Override
    public void updateTaskName(String oldTaskName, String newTaskName) {
        log.debug("Updating task name from {} to {}", oldTaskName, newTaskName);
        repository.updateTaskName(oldTaskName, newTaskName);
    }
}
