package com.example.demo.service.page;

import org.springframework.stereotype.Service;

import com.example.demo.entity.TaskPage;
import com.example.demo.repository.page.TaskPageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskPageServiceImpl implements TaskPageService {

    private final TaskPageRepository repository;

    @Override
    public TaskPage getOrCreatePage(int taskId) {
        log.debug("Fetching task page for task {}", taskId);
        TaskPage p = repository.findByTaskId(taskId);
        if (p == null) {
            p = new TaskPage();
            p.setTaskId(taskId);
            p.setContent("");
            repository.insertPage(p);
            p = repository.findByTaskId(taskId);
        }
        return p;
    }

    @Override
    public void updatePage(TaskPage page) {
        log.debug("Updating task page id {}", page.getId());
        repository.updatePage(page);
    }

    @Override
    public void deleteByTaskId(int taskId) {
        log.debug("Deleting task page for task {}", taskId);
        repository.deleteByTaskId(taskId);
    }
}
