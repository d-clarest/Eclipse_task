package com.example.demo.repository.task;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskRepository {
    List<Task> findAll();
    Task findById(int id);
    void insertTask(Task task);
    void updateTask(Task task);
    void deleteById(int id);
    int sumCompletedLevels();

    /**
     * 指定したタイトルで、作成日が指定日に該当するタスクが存在するかを判定する。
     *
     * @param title タイトル
     * @param date  作成日
     * @return 存在する場合は true
     */
    boolean existsByTitleAndDate(String title, java.time.LocalDate date);
}
