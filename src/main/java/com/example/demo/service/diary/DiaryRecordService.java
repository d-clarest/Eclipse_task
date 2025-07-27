package com.example.demo.service.diary;

import java.util.List;
import com.example.demo.entity.DiaryRecord;

public interface DiaryRecordService {
    List<DiaryRecord> getAllRecords();
    void addRecord(DiaryRecord record);
    void updateRecord(DiaryRecord record);
    void deleteById(int id);
}
