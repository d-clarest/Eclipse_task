package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.DiaryRecord;
import com.example.demo.service.diary.DiaryRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DiaryRecordController {

    private final DiaryRecordService service;

    @PostMapping("/diary-add")
    public ResponseEntity<Void> addRecord(@RequestBody DiaryRecord record) {
        log.debug("Adding diary record");
        service.addRecord(record);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/diary-update")
    public ResponseEntity<Void> updateRecord(@RequestBody DiaryRecord record) {
        log.debug("Updating diary record id {}", record.getId());
        service.updateRecord(record);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/diary-delete")
    public ResponseEntity<Void> deleteRecord(@RequestBody DiaryRecord record) {
        log.debug("Deleting diary record id {}", record.getId());
        service.deleteById(record.getId());
        return ResponseEntity.ok().build();
    }
}
