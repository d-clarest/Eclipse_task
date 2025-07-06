package com.example.demo.service.task;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.repository.task.TaskRepository;

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
    public void addTask(Task task) {
        log.debug("Adding task {}", task.getTitle());
        repository.insertTask(task);
    }

    @Override
    public void updateTask(Task task) {
        log.debug("Updating task id {}", task.getId());
        repository.updateTask(task);
    }

    @Override
    public void deleteTaskById(int id) {
        log.debug("Deleting task with id {}", id);
        repository.deleteById(id);
    }
}
