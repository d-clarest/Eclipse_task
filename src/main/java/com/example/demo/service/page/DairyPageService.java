package com.example.demo.service.page;

import com.example.demo.entity.DairyPage;

public interface DairyPageService {
    DairyPage getOrCreatePage(int dairyId);
    void updatePage(DairyPage page);
    void deleteByDairyId(int dairyId);
}
