package com.example.demo.service.subtask;

import java.util.List;

import com.example.demo.entity.SubTask;

public interface SubTaskService {
    List<SubTask> getSubTasks(int taskId);
    void addSubTask(SubTask subTask);
    void updateSubTask(SubTask subTask);
    void deleteSubTaskById(int id);
}
