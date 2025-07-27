package com.example.demo.repository.diary;

import java.util.List;
import com.example.demo.entity.DiaryRecord;

public interface DiaryRecordRepository {
    List<DiaryRecord> findAll();
    void insertRecord(DiaryRecord record);
    void updateRecord(DiaryRecord record);
    void deleteById(int id);
}
