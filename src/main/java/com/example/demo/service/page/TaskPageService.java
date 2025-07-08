package com.example.demo.service.page;

import com.example.demo.entity.TaskPage;

public interface TaskPageService {
    TaskPage getOrCreatePage(int taskId);
    void updatePage(TaskPage page);
    void deleteByTaskId(int taskId);
}
