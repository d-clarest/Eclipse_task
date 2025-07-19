package com.example.demo.service.awareness;

import java.util.List;

import com.example.demo.entity.AwarenessPage;
import com.example.demo.entity.AwarenessRecord;

public interface AwarenessTrashService {
    void addDeletedRecord(AwarenessRecord record, AwarenessPage page);

    List<AwarenessRecord> getDeletedRecords();

    void restore(int recordId);
}
