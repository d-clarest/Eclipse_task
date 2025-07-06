package com.example.demo.service.awareness;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.AwarenessRecord;
import com.example.demo.repository.awareness.AwarenessRecordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwarenessRecordServiceImpl implements AwarenessRecordService {

    private final AwarenessRecordRepository repository;

    @Override
    public List<AwarenessRecord> getAllRecords() {
        log.debug("Fetching all awareness records");
        return repository.findAll();
    }
}
