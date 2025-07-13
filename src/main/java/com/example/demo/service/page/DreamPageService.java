package com.example.demo.service.page;

import com.example.demo.entity.DreamPage;

public interface DreamPageService {
    DreamPage getOrCreatePage(int dreamId);
    void updatePage(DreamPage page);
    void deleteByDreamId(int dreamId);
}
