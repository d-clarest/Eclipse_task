package com.example.demo.repository.subtask;

import java.util.List;

import com.example.demo.entity.SubTask;

public interface SubTaskRepository {
    /**
     * 全ての子タスクを取得する。
     * @return 子タスク一覧
     */
    List<SubTask> findAll();
    List<SubTask> findByTaskId(int taskId);
    void insertSubTask(SubTask subTask);
    void updateSubTask(SubTask subTask);
    void deleteById(int id);
    /**
     * 指定した親タスクに紐づく子タスクをすべて削除する。
     *
     * @param taskId 親タスクのID
     */
    void deleteByTaskId(int taskId);
    int countByTaskId(int taskId);
    int countCompletedByTaskId(int taskId);

    /**
     * 指定したタスクIDの未完了の子タスクをすべて完了済みにする。
     *
     * @param taskId 親タスクのID
     * @param completedAt 完了日
     */
    void markAllCompletedByTaskId(int taskId, java.time.LocalDate completedAt);

    /**
     * 指定したタスクIDの子タスクをすべて未完了状態に戻す。
     *
     * @param taskId 親タスクのID
     */
    void markAllUncompletedByTaskId(int taskId);
}
