package com.example.demo.repository.awareness;

import java.util.List;

import com.example.demo.entity.AwarenessRecord;

public interface AwarenessRecordRepository {
    List<AwarenessRecord> findAll();

    AwarenessRecord findById(int id);

    void insertRecord(AwarenessRecord record);

    void insertRecordWithId(AwarenessRecord record);

    void updateRecord(AwarenessRecord record);

    void deleteById(int id);

    int sumAwarenessLevels();
}
