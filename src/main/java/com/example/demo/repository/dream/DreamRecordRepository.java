package com.example.demo.repository.dream;

import java.util.List;
import com.example.demo.entity.DreamRecord;

public interface DreamRecordRepository {
    List<DreamRecord> findAll();
    void insertRecord(DreamRecord record);
    void updateRecord(DreamRecord record);
    void deleteById(int id);
}
