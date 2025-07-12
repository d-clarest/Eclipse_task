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
