package com.example.demo.service.word;

import java.util.List;

import com.example.demo.entity.WordRecord;

public interface WordRecordService {
    List<WordRecord> getAllRecords();
    void addRecord(WordRecord record);
    void updateRecord(WordRecord record);
    void deleteById(int id);
    int countRecords();
}
