package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.demo.form.TaskNameUpdate;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;

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
        service.updateConfirmed(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/task-due-date")
    public ResponseEntity<Void> updateDueDate(@RequestBody Task task) {
        service.updateDueDate(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/task-name")
    public ResponseEntity<Void> updateTaskName(@RequestBody TaskNameUpdate req) {
        service.updateTaskName(req.getOldTaskName(), req.getNewTaskName());
        return ResponseEntity.ok().build();
    }
}
