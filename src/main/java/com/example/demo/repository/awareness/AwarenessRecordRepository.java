package com.example.demo.repository.awareness;

import java.util.List;

import com.example.demo.entity.AwarenessRecord;

public interface AwarenessRecordRepository {
    List<AwarenessRecord> findAll();
}
