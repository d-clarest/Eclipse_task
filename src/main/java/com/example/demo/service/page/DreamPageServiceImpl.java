package com.example.demo.service.page;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DreamPage;
import com.example.demo.repository.page.DreamPageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DreamPageServiceImpl implements DreamPageService {

    private final DreamPageRepository repository;

    @Override
    public DreamPage getOrCreatePage(int dreamId) {
        log.debug("Fetching dream page for dream {}", dreamId);
        DreamPage p = repository.findByDreamId(dreamId);
        if (p == null) {
            p = new DreamPage();
            p.setDreamId(dreamId);
            p.setContent("");
            repository.insertPage(p);
            p = repository.findByDreamId(dreamId);
        }
        return p;
    }

    @Override
    public void updatePage(DreamPage page) {
        log.debug("Updating dream page id {}", page.getId());
        repository.updatePage(page);
    }

    @Override
    public void deleteByDreamId(int dreamId) {
        log.debug("Deleting dream page for dream {}", dreamId);
        repository.deleteByDreamId(dreamId);
    }
}
