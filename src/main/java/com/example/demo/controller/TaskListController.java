package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.demo.form.TaskNameUpdate;
import com.example.demo.form.ScheduleUpdateForm;

import com.example.demo.entity.Task;
import com.example.demo.entity.Schedule;
import com.example.demo.service.TaskService;
import com.example.demo.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TaskListController {

    private final TaskService service;
    private final ScheduleService scheduleService;

    @GetMapping("/task-top")
    public String showTaskTop(Model model) {
        model.addAttribute("tasks", service.getAllTasks());
        model.addAttribute("schedules", scheduleService.getAllSchedules());
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

    @PostMapping("/schedule-complete")
    public ResponseEntity<Void> updateCompletedDay(@RequestBody Schedule schedule) {
        scheduleService.updateCompletedDay(schedule);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-update")
    public ResponseEntity<Void> updateSchedule(@RequestBody ScheduleUpdateForm form) {
        scheduleService.updateSchedule(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-add")
    public ResponseEntity<Void> addSchedule(@RequestBody Schedule schedule) {
        scheduleService.addSchedule(schedule);
        return ResponseEntity.ok().build();
    }
}
