package com.example.demo.service.awareness;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.AwarenessRecord;
import com.example.demo.repository.awareness.AwarenessRecordRepository;
import com.example.demo.service.page.AwarenessPageService;
import com.example.demo.service.awareness.AwarenessTrashService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwarenessRecordServiceImpl implements AwarenessRecordService {

    private final AwarenessRecordRepository repository;
    private final AwarenessPageService pageService;
    private final AwarenessTrashService trashService;

    @Override
    public List<AwarenessRecord> getAllRecords() {
        log.debug("Fetching all awareness records");
        return repository.findAll();
    }

    @Override
    public void addRecord(AwarenessRecord record) {
        log.debug("Adding awareness record");
        repository.insertRecord(record);
    }

    @Override
    public void updateRecord(AwarenessRecord record) {
        log.debug("Updating awareness record id {}", record.getId());
        repository.updateRecord(record);
    }

    @Override
    public void deleteById(int id) {
        log.debug("Deleting awareness record id {}", id);
        AwarenessRecord record = repository.findById(id);
        var page = pageService.getPage(id);
        if (page != null) {
            trashService.addDeletedRecord(record, page);
        } else {
            trashService.addDeletedRecord(record, null);
        }
        pageService.deleteByRecordId(id);
        repository.deleteById(id);
    }

    @Override
    public void restoreById(int id) {
        log.debug("Restoring awareness record id {}", id);
        trashService.restore(id);
    }

    @Override
    public AwarenessRecord getRecordById(int id) {
        log.debug("Fetching awareness record id {}", id);
        return repository.findById(id);
    }

    @Override
    public int getTotalAwarenessLevel() {
        log.debug("Fetching total awareness levels");
        return repository.sumAwarenessLevels();
    }
}
