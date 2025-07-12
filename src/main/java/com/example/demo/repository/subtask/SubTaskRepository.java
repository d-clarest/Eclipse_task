package com.example.demo.repository.subtask;

import java.util.List;

import com.example.demo.entity.SubTask;

public interface SubTaskRepository {
    List<SubTask> findByTaskId(int taskId);
    void insertSubTask(SubTask subTask);
    void updateSubTask(SubTask subTask);
    void deleteById(int id);
    int countByTaskId(int taskId);
    int countCompletedByTaskId(int taskId);
}
