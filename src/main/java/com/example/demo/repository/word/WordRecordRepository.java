package com.example.demo.repository.word;

import java.util.List;

import com.example.demo.entity.WordRecord;

public interface WordRecordRepository {
    List<WordRecord> findAll();
    void insertRecord(WordRecord record);
    void updateRecord(WordRecord record);
    void deleteById(int id);
    int countRecords();
}
