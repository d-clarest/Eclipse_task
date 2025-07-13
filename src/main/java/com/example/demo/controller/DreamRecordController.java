package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.DreamRecord;
import com.example.demo.service.dream.DreamRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DreamRecordController {

    private final DreamRecordService service;

    @PostMapping("/dream-add")
    public ResponseEntity<Void> addRecord(@RequestBody DreamRecord record) {
        log.debug("Adding dream record");
        service.addRecord(record);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dream-update")
    public ResponseEntity<Void> updateRecord(@RequestBody DreamRecord record) {
        log.debug("Updating dream record id {}", record.getId());
        service.updateRecord(record);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dream-delete")
    public ResponseEntity<Void> deleteRecord(@RequestBody DreamRecord record) {
        log.debug("Deleting dream record id {}", record.getId());
        service.deleteById(record.getId());
        return ResponseEntity.ok().build();
    }
}
