package com.example.demo.service.page;

import org.springframework.stereotype.Service;

import com.example.demo.entity.AwarenessPage;
import com.example.demo.repository.page.AwarenessPageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwarenessPageServiceImpl implements AwarenessPageService {

    private final AwarenessPageRepository repository;

    @Override
    public AwarenessPage getOrCreatePage(int recordId) {
        log.debug("Fetching page for record {}", recordId);
        AwarenessPage p = repository.findByRecordId(recordId);
        if (p == null) {
            p = new AwarenessPage();
            p.setRecordId(recordId);
            p.setContent("");
            repository.insertPage(p);
            p = repository.findByRecordId(recordId);
        }
        return p;
    }

    @Override
    public void updatePage(AwarenessPage page) {
        log.debug("Updating page id {}", page.getId());
        repository.updatePage(page);
    }

    @Override
    public void deleteByRecordId(int recordId) {
        log.debug("Deleting page for record {}", recordId);
        repository.deleteByRecordId(recordId);
    }
}
