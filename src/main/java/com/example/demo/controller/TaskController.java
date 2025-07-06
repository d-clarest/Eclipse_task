package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.Task;
import com.example.demo.service.task.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/task-add")
    public ResponseEntity<Void> addTask(@RequestBody Task task) {
        log.debug("Adding task {}", task.getTitle());
        taskService.addTask(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/task-update")
    public ResponseEntity<Void> updateTask(@RequestBody Task task) {
        log.debug("Updating task id {}", task.getId());
        taskService.updateTask(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/task-delete")
    public ResponseEntity<Void> deleteTask(@RequestBody Task task) {
        log.debug("Deleting task id {}", task.getId());
        taskService.deleteTaskById(task.getId());
        return ResponseEntity.ok().build();
    }
}
