package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import com.example.demo.service.TaskService;
import com.example.demo.entity.Task;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TaskListController {

    private final TaskService service;

    @GetMapping("/task-top")
    public String showTaskTop(Model model) {
        model.addAttribute("tasks", service.getAllTasks());
        return "task-top";
    }

    @PostMapping("/task-confirm")
    public ResponseEntity<Void> updateConfirmed(@RequestBody Task task) {
        service.updateConfirmed(task.getTaskName(), task.getDueDate(), task.isConfirmed());
        return ResponseEntity.ok().build();
    }
}
