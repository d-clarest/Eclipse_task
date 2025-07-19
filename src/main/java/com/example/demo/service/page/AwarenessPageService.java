package com.example.demo.service.page;

import com.example.demo.entity.AwarenessPage;

public interface AwarenessPageService {
    AwarenessPage getOrCreatePage(int recordId);
    AwarenessPage getPage(int recordId);
    void updatePage(AwarenessPage page);
    void insertPageWithId(AwarenessPage page);
    void deleteByRecordId(int recordId);
}
