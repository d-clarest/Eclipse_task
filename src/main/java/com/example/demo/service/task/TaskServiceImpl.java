package com.example.demo.service.task;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.repository.task.TaskRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    /**
     * カテゴリから締切日時を計算する。
     */
    private LocalDateTime calculateDeadline(String category) {
        LocalDate today = LocalDate.now();
        LocalDate sundayThisWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalTime cutOff = LocalTime.of(23, 59);
        if (category == null) {
            return today.plusDays(1).atTime(cutOff);
        }
        switch (category) {
        case "今日":
            return today.atTime(cutOff);
        case "明日":
            return today.plusDays(1).atTime(cutOff);
        case "今週":
            return sundayThisWeek.atTime(cutOff);
        case "来週":
            return sundayThisWeek.plusWeeks(1).atTime(cutOff);
        case "再来週":
            return sundayThisWeek.plusWeeks(2).atTime(cutOff);
        default:
            return today.atTime(cutOff);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        log.debug("Fetching all tasks");
        List<Task> list = repository.findAll();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        for (Task t : list) {
            if (t.getCompletedAt() != null) {
                t.setTimeUntilDue(null);
                continue;
            }

            LocalDateTime deadline = t.getDeadline();
            if (deadline == null) {
                // When deadline is missing (表示が "-"), treat task as expired
                t.setExpired(true);
                t.setTimeUntilDue("-");
                continue;
            }

            long minutes = Duration.between(now, deadline).toMinutes();
            boolean expired = minutes <= 0;
            if (minutes < 0)
                minutes = 0;
            long rounded = (minutes / 5) * 5;
            long days = rounded / (60 * 24);
            long hours = (rounded % (60 * 24)) / 60;
            long mins = rounded % 60;
            t.setTimeUntilDue(String.format("%d日%d時間%d分", days, hours, mins));
            t.setExpired(expired);

            // Keep existing deadline without changing category based on it
            t.setDeadline(deadline);
        }
        return list;
    }

    @Override
    public void addTask(Task task) {
        log.debug("Adding task {}", task.getTitle());
        LocalDateTime deadline = calculateDeadline(task.getCategory());
        task.setDeadline(deadline);
        repository.insertTask(task);
    }

    @Override
    public void updateTask(Task task) {
        log.debug("Updating task id {}", task.getId());
        Task current = repository.findById(task.getId());
        boolean categoryChanged = (current.getCategory() == null && task.getCategory() != null)
                || (current.getCategory() != null && !current.getCategory().equals(task.getCategory()));
        if (task.getCompletedAt() == null && categoryChanged) {
            LocalDateTime deadline = calculateDeadline(task.getCategory());
            task.setDeadline(deadline);
        } else {
            task.setDeadline(current.getDeadline());
        }
        repository.updateTask(task);
    }

    @Override
    public void deleteTaskById(int id) {
        log.debug("Deleting task with id {}", id);
        repository.deleteById(id);
    }

    @Override
    public int getTotalCompletedLevels() {
        log.debug("Fetching total completed task levels");
        return repository.sumCompletedLevels();
    }
}
