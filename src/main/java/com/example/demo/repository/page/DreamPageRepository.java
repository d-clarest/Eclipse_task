package com.example.demo.repository.page;

import com.example.demo.entity.DreamPage;

public interface DreamPageRepository {
    DreamPage findByDreamId(int dreamId);
    void insertPage(DreamPage page);
    void updatePage(DreamPage page);
    void deleteByDreamId(int dreamId);
}
