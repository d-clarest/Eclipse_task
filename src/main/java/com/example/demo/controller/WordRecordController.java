package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.WordRecord;
import com.example.demo.service.word.WordRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WordRecordController {

    private final WordRecordService service;

    @PostMapping("/word-add")
    public ResponseEntity<Void> addRecord(@RequestBody WordRecord record) {
        log.debug("Adding word record");
        service.addRecord(record);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/word-update")
    public ResponseEntity<Void> updateRecord(@RequestBody WordRecord record) {
        log.debug("Updating word record id {}", record.getId());
        service.updateRecord(record);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/word-delete")
    public ResponseEntity<Void> deleteRecord(@RequestBody WordRecord record) {
        log.debug("Deleting word record id {}", record.getId());
        service.deleteById(record.getId());
        return ResponseEntity.ok().build();
    }
}
