package com.example.demo.service.diary;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entity.DiaryRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiaryRecordScheduler {

    private final DiaryRecordService service;

    @Scheduled(cron = "0 0 0 * * *")
    public void addDailyRecord() {
        DiaryRecord record = new DiaryRecord();
        record.setRecordDate(LocalDate.now());
        record.setContent("");
        record.setDetail("");
        service.addRecord(record);
        log.debug("Added daily diary record for {}", record.getRecordDate());
    }
}
