package com.example.demo.repository.page;

import com.example.demo.entity.AwarenessPage;

public interface AwarenessPageRepository {
    AwarenessPage findByRecordId(int recordId);

    void insertPage(AwarenessPage page);

    void updatePage(AwarenessPage page);

    void deleteByRecordId(int recordId);
}
