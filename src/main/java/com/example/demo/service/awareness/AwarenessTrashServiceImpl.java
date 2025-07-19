package com.example.demo.service.awareness;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.ZoneId;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.entity.AwarenessPage;
import com.example.demo.entity.AwarenessRecord;
import com.example.demo.repository.awareness.AwarenessRecordRepository;
import com.example.demo.repository.page.AwarenessPageRepository;
import com.example.demo.dto.DeletedAwarenessRecordInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwarenessTrashServiceImpl implements AwarenessTrashService {

    private final AwarenessRecordRepository recordRepository;
    private final AwarenessPageRepository pageRepository;

    private static class TrashItem {
        AwarenessRecord record;
        AwarenessPage page;
        LocalDateTime deletedAt;
    }

    private final Map<Integer, TrashItem> trash = new ConcurrentHashMap<>();

    @Override
    public void addDeletedRecord(AwarenessRecord record, AwarenessPage page) {
        TrashItem item = new TrashItem();
        item.record = record;
        item.page = page;
        item.deletedAt = LocalDateTime.now();
        trash.put(record.getId(), item);
    }

    @Override
    public List<DeletedAwarenessRecordInfo> getDeletedRecords() {
        removeExpired();
        List<DeletedAwarenessRecordInfo> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (TrashItem item : trash.values()) {
            DeletedAwarenessRecordInfo info = new DeletedAwarenessRecordInfo();
            info.setRecord(item.record);
            long expiry = item.deletedAt.plusMinutes(10)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            info.setExpiryEpochMillis(expiry);
            list.add(info);
        }
        return list;
    }

    @Override
    public void restore(int recordId) {
        TrashItem item = trash.remove(recordId);
        if (item != null) {
            log.debug("Restoring awareness record id {}", recordId);
            // Insert the record first so that the page can reference it
            recordRepository.insertRecordWithId(item.record);
            if (item.page != null) {
                pageRepository.insertPageWithId(item.page);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void removeExpired() {
        LocalDateTime now = LocalDateTime.now();
        trash.entrySet().removeIf(e -> Duration.between(e.getValue().deletedAt, now).toMinutes() >= 10);
    }
}
