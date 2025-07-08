package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskPage;
import com.example.demo.service.page.TaskPageService;
import com.example.demo.service.task.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskPageController {

    private final TaskPageService service;
    private final TaskService taskService;

    @GetMapping("/{username}/task-top/task-page/{taskId}")
    public String showTaskPage(@PathVariable String username, @PathVariable int taskId,
            Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying task page for task {}", taskId);
        TaskPage page = service.getOrCreatePage(taskId);
        Task task = taskService.getAllTasks().stream()
                .filter(t -> t.getId() == taskId)
                .findFirst()
                .orElse(null);
        model.addAttribute("page", page);
        model.addAttribute("task", task);
        model.addAttribute("username", username);
        return "task-page";
    }

    @PostMapping("/task-page-update")
    public ResponseEntity<Void> updateTaskPage(@RequestBody TaskPage page) {
        log.debug("Updating task page id {}", page.getId());
        service.updatePage(page);
        return ResponseEntity.ok().build();
    }
}
