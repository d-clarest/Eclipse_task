package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.AwarenessRecord;
import com.example.demo.service.awareness.AwarenessRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AwarenessRecordController {

    private final AwarenessRecordService service;

    @PostMapping("/awareness-add")
    public ResponseEntity<Void> addRecord(@RequestBody AwarenessRecord record) {
        log.debug("Adding awareness record");
        service.addRecord(record);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/awareness-update")
    public ResponseEntity<Void> updateRecord(@RequestBody AwarenessRecord record) {
        log.debug("Updating awareness record id {}", record.getId());
        service.updateRecord(record);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/awareness-delete")
    public ResponseEntity<Void> deleteRecord(@RequestBody AwarenessRecord record) {
        log.debug("Deleting awareness record id {}", record.getId());
        service.deleteById(record.getId());
        return ResponseEntity.ok().build();
    }
}
