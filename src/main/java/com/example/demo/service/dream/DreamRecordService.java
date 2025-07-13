package com.example.demo.service.dream;

import java.util.List;
import com.example.demo.entity.DreamRecord;

public interface DreamRecordService {
    List<DreamRecord> getAllRecords();
    void addRecord(DreamRecord record);
    void updateRecord(DreamRecord record);
    void deleteById(int id);
}
