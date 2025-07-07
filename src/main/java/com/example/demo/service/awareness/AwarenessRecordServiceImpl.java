package com.example.demo.service.awareness;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.AwarenessRecord;
import com.example.demo.repository.awareness.AwarenessRecordRepository;
import com.example.demo.service.page.AwarenessPageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwarenessRecordServiceImpl implements AwarenessRecordService {

    private final AwarenessRecordRepository repository;
    private final AwarenessPageService pageService;

    @Override
    public List<AwarenessRecord> getAllRecords() {
        log.debug("Fetching all awareness records");
        return repository.findAll();
    }

    @Override
    public void addRecord(AwarenessRecord record) {
        log.debug("Adding awareness record");
        repository.insertRecord(record);
    }

    @Override
    public void updateRecord(AwarenessRecord record) {
        log.debug("Updating awareness record id {}", record.getId());
        repository.updateRecord(record);
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting awareness record id {}", id);
        pageService.deleteByRecordId(id);
        repository.deleteById(id);
    }

    @Override
    public int getTotalAwarenessLevel() {
        log.debug("Fetching total awareness levels");
        return repository.sumAwarenessLevels();
    }
}
