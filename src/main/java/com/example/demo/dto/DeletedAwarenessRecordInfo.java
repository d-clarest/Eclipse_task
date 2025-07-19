package com.example.demo.dto;

import com.example.demo.entity.AwarenessRecord;
import lombok.Data;

@Data
public class DeletedAwarenessRecordInfo {
    private AwarenessRecord record;
    private long expiryEpochMillis;
}
