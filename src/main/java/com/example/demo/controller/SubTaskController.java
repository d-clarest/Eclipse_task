package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.SubTask;
import com.example.demo.service.subtask.SubTaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SubTaskController {

    private final SubTaskService service;

    @PostMapping("/subtask-add")
    public ResponseEntity<Void> addSubTask(@RequestBody SubTask subTask) {
        log.debug("Adding subtask {}", subTask.getTitle());
        service.addSubTask(subTask);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/subtask-update")
    public ResponseEntity<Void> updateSubTask(@RequestBody SubTask subTask) {
        log.debug("Updating subtask id {}", subTask.getId());
        service.updateSubTask(subTask);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/subtask-delete")
    public ResponseEntity<Void> deleteSubTask(@RequestBody SubTask subTask) {
        log.debug("Deleting subtask id {}", subTask.getId());
        service.deleteSubTaskById(subTask.getId());
        return ResponseEntity.ok().build();
    }
}
