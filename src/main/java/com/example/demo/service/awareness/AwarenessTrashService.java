package com.example.demo.service.awareness;

import java.util.List;

import com.example.demo.entity.AwarenessPage;
import com.example.demo.entity.AwarenessRecord;
import com.example.demo.dto.DeletedAwarenessRecordInfo;

public interface AwarenessTrashService {
    void addDeletedRecord(AwarenessRecord record, AwarenessPage page);

    List<DeletedAwarenessRecordInfo> getDeletedRecords();

    void restore(int recordId);
}
