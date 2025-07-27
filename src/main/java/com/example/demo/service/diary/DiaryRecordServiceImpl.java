package com.example.demo.service.diary;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DiaryRecord;
import com.example.demo.repository.diary.DiaryRecordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryRecordServiceImpl implements DiaryRecordService {

    private final DiaryRecordRepository repository;

    @Override
    public List<DiaryRecord> getAllRecords() {
        log.debug("Fetching all diary records");
        return repository.findAll();
    }

    @Override
    public void addRecord(DiaryRecord record) {
        log.debug("Adding diary record");
        repository.insertRecord(record);
    }

    @Override
    public void updateRecord(DiaryRecord record) {
        log.debug("Updating diary record id {}", record.getId());
        repository.updateRecord(record);
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting diary record id {}", id);
        repository.deleteById(id);
    }
}
