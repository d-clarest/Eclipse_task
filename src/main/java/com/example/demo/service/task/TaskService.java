package com.example.demo.service.task;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskService {
    List<Task> getAllTasks();

    void addTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    int getTotalCompletedLevels();

    Task getTaskById(int id);

    /**
     * 指定タイトルで作成日が指定日に該当するタスクが存在するか判定する。
     *
     * @param title タイトル
     * @param date  作成日
     * @return 存在する場合は true
     */
    boolean existsTaskCreatedOn(String title, java.time.LocalDate date);
}
