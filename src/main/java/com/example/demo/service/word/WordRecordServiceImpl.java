package com.example.demo.service.word;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.WordRecord;
import com.example.demo.repository.word.WordRecordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordRecordServiceImpl implements WordRecordService {

    private final WordRecordRepository repository;

    @Override
    public List<WordRecord> getAllRecords() {
        log.debug("Fetching all word records");
        return repository.findAll();
    }

    @Override
    public void addRecord(WordRecord record) {
        log.debug("Adding word record {}", record.getWord());
        repository.insertRecord(record);
    }

    @Override
    public void updateRecord(WordRecord record) {
        log.debug("Updating word record id {}", record.getId());
        repository.updateRecord(record);
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting word record id {}", id);
        repository.deleteById(id);
    }

    @Override
    public int countRecords() {
        log.debug("Counting word records");
        return repository.countRecords();
    }
}
