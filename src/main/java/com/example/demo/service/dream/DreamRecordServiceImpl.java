package com.example.demo.service.dream;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DreamRecord;
import com.example.demo.repository.dream.DreamRecordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DreamRecordServiceImpl implements DreamRecordService {

    private final DreamRecordRepository repository;

    @Override
    public List<DreamRecord> getAllRecords() {
        log.debug("Fetching all dream records");
        return repository.findAll();
    }

    @Override
    public void addRecord(DreamRecord record) {
        log.debug("Adding dream record");
        repository.insertRecord(record);
    }

    @Override
    public void updateRecord(DreamRecord record) {
        log.debug("Updating dream record id {}", record.getId());
        repository.updateRecord(record);
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting dream record id {}", id);
        repository.deleteById(id);
    }
}
