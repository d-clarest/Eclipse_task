package com.example.demo.repository.page;

import com.example.demo.entity.DairyPage;

public interface DairyPageRepository {
    DairyPage findByDairyId(int dairyId);
    void insertPage(DairyPage page);
    void updatePage(DairyPage page);
    void deleteByDairyId(int dairyId);
}
