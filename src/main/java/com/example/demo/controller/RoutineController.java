package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.Routine;
import com.example.demo.service.routine.RoutineService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RoutineController {

    private final RoutineService service;

    @PostMapping("/routine-add")
    public ResponseEntity<Void> addRoutine(@RequestBody Routine routine) {
        log.debug("Adding routine");
        service.addRoutine(routine);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/routine-update")
    public ResponseEntity<Void> updateRoutine(@RequestBody Routine routine) {
        log.debug("Updating routine id {}", routine.getId());
        service.updateRoutine(routine);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/routine-delete")
    public ResponseEntity<Void> deleteRoutine(@RequestBody Routine routine) {
        log.debug("Deleting routine id {}", routine.getId());
        service.deleteById(routine.getId());
        return ResponseEntity.ok().build();
    }
}
