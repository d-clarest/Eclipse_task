package com.example.demo.service.awareness;

import java.util.List;

import com.example.demo.entity.AwarenessRecord;

public interface AwarenessRecordService {
    List<AwarenessRecord> getAllRecords();

    void addRecord(AwarenessRecord record);

    void updateRecord(AwarenessRecord record);

    void deleteById(int id);

    int getTotalAwarenessLevel();
}
