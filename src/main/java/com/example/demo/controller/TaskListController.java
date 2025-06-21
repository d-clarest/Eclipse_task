package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
