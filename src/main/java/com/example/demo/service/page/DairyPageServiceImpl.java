package com.example.demo.service.page;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DairyPage;
import com.example.demo.repository.page.DairyPageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DairyPageServiceImpl implements DairyPageService {

    private final DairyPageRepository repository;

    @Override
    public DairyPage getOrCreatePage(int dairyId) {
        log.debug("Fetching dairy page for diary {}", dairyId);
        DairyPage p = repository.findByDairyId(dairyId);
        if (p == null) {
            p = new DairyPage();
            p.setDairyId(dairyId);
            p.setContent("");
            repository.insertPage(p);
            p = repository.findByDairyId(dairyId);
        }
        return p;
    }

    @Override
    public void updatePage(DairyPage page) {
        log.debug("Updating dairy page id {}", page.getId());
        repository.updatePage(page);
    }

    @Override
    public void deleteByDairyId(int dairyId) {
        log.debug("Deleting dairy page for diary {}", dairyId);
        repository.deleteByDairyId(dairyId);
    }
}
