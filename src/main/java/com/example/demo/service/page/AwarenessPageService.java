package com.example.demo.service.page;

import com.example.demo.entity.AwarenessPage;

public interface AwarenessPageService {
    AwarenessPage getOrCreatePage(int recordId);
    void updatePage(AwarenessPage page);
}
