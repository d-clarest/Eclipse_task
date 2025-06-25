package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.form.TaskNameUpdate;
import com.example.demo.form.ScheduleUpdateForm;

import com.example.demo.entity.Task;
import com.example.demo.entity.Schedule;
import com.example.demo.service.TaskService;
import com.example.demo.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskListController {

    private final TaskService service;
    private final ScheduleService scheduleService;

    @GetMapping("/{username}/task-top")
    public String showTaskTop(@PathVariable String username, Model model) {
        log.debug("Displaying task top page");
        model.addAttribute("tasks", service.getAllTasks());
        var list = scheduleService.getAllSchedules().stream()
                .filter(s -> s.getCompletedDay() == null)
                .toList();
        model.addAttribute("schedules", list);
        model.addAttribute("username", username);
        return "task-top";
    }

    @GetMapping("/{username}/task-top/task-box")
    public String showTaskBox(@PathVariable String username, Model model) {
        log.debug("Displaying task box page");
        var all = scheduleService.getAllSchedules();
        var completed = all.stream()
                .filter(s -> s.getCompletedDay() != null)
                .toList();
        var upcoming = all.stream()
                .filter(s -> s.getCompletedDay() == null)
                .toList();
        model.addAttribute("completedSchedules", completed);
        model.addAttribute("upcomingSchedules", upcoming);
        model.addAttribute("username", username);
        return "task-box";
    }

    @PostMapping("/task-confirm")
    public ResponseEntity<Void> updateConfirmed(@RequestBody Task task) {
        log.debug("Updating task confirm: {} {}", task.getTaskName(), task.isConfirmed());
        service.updateConfirmed(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/task-due-date")
    public ResponseEntity<Void> updateDueDate(@RequestBody Task task) {
        log.debug("Updating due date for {} to {}", task.getTaskName(), task.getDueDate());
        service.updateDueDate(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/task-name")
    public ResponseEntity<Void> updateTaskName(@RequestBody TaskNameUpdate req) {
        log.debug("Updating task name from {} to {}", req.getOldTaskName(), req.getNewTaskName());
        service.updateTaskName(req.getOldTaskName(), req.getNewTaskName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-complete")
    public ResponseEntity<Void> updateCompletedDay(@RequestBody Schedule schedule) {
        log.debug("Updating schedule completed day {} {}", schedule.getTitle(), schedule.getCompletedDay());
        scheduleService.updateCompletedDay(schedule);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-update")
    public ResponseEntity<Void> updateSchedule(@RequestBody ScheduleUpdateForm form) {
        log.debug("Updating schedule {} on {}", form.getTitle(), form.getScheduleDate());
        scheduleService.updateSchedule(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-add")
    public ResponseEntity<Void> addSchedule(@RequestBody Schedule schedule) {
        log.debug("Adding schedule {} on {}", schedule.getTitle(), schedule.getScheduleDate());
        scheduleService.addSchedule(schedule);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-delete")
    public ResponseEntity<Void> deleteSchedule(@RequestBody Schedule schedule) {
        log.debug("Deleting schedule id {}", schedule.getId());
        scheduleService.deleteScheduleById(schedule.getId());
        return ResponseEntity.ok().build();
    }
}
