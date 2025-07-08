package com.example.demo.repository.page;

import com.example.demo.entity.TaskPage;

public interface TaskPageRepository {
    TaskPage findByTaskId(int taskId);

    void insertPage(TaskPage page);

    void updatePage(TaskPage page);

    void deleteByTaskId(int taskId);
}
