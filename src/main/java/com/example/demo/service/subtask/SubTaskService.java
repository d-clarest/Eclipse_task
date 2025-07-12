package com.example.demo.service.subtask;

import java.util.List;

import com.example.demo.entity.SubTask;

public interface SubTaskService {
    /**
     * すべての子タスクを取得する。
     * @return 子タスク一覧
     */
    List<SubTask> getAllSubTasks();
    List<SubTask> getSubTasks(int taskId);
    void addSubTask(SubTask subTask);
    void updateSubTask(SubTask subTask);
    void deleteSubTaskById(int id);
    int countSubTasks(int taskId);
    int countCompletedSubTasks(int taskId);

    /**
     * 親タスクの未完了の子タスクをまとめて完了状態にする。
     *
     * @param taskId 親タスクのID
     * @param completedAt 完了日
     */
    void markAllCompletedByTaskId(int taskId, java.time.LocalDate completedAt);

    /**
     * 親タスクの子タスクをすべて未完了に戻す。
     *
     * @param taskId 親タスクのID
     */
    void markAllUncompletedByTaskId(int taskId);
}
