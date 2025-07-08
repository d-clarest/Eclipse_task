package com.example.demo.service.task;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        if (category == null) {
            return today.plusDays(1).atStartOfDay();
        }
        switch (category) {
        case "今日":
            return today.plusDays(1).atStartOfDay();
        case "明日":
            return today.plusDays(2).atStartOfDay();
        case "今週":
            return sundayThisWeek.plusDays(1).atStartOfDay();
        case "来週":
            return sundayThisWeek.plusWeeks(1).plusDays(1).atStartOfDay();
        case "再来週":
            return sundayThisWeek.plusWeeks(2).plusDays(1).atStartOfDay();
        default:
            return today.plusDays(1).atStartOfDay();
        }
    }

    @Override
    public List<Task> getAllTasks() {
        log.debug("Fetching all tasks");
        List<Task> list = repository.findAll();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = now.toLocalDate();
        LocalDate sundayThisWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        for (Task t : list) {
            if (t.getCompletedAt() != null) {
                t.setTimeUntilDue(null);
                t.setDeadline(null);
                continue;
            }

            LocalDateTime deadline = t.getDeadline();
            if (deadline == null) {
                String category = t.getCategory();
                if (category == null) {
                    deadline = today.plusDays(1).atStartOfDay();
                } else {
                    switch (category) {
                    case "今日":
                        deadline = today.plusDays(1).atStartOfDay();
                        break;
                    case "明日":
                        LocalDateTime startOfTomorrow = today.plusDays(1).atStartOfDay();
                        if (now.isAfter(startOfTomorrow)) {
                            t.setCategory("今日");
                            repository.updateTask(t);
                            deadline = today.plusDays(1).atStartOfDay();
                            break;
                        }
                        deadline = today.plusDays(2).atStartOfDay();
                        break;
                    case "今週":
                        deadline = sundayThisWeek.plusDays(1).atStartOfDay();
                        break;
                    case "来週":
                        deadline = sundayThisWeek.plusWeeks(1).plusDays(1).atStartOfDay();
                        break;
                    case "再来週":
                        deadline = sundayThisWeek.plusWeeks(2).plusDays(1).atStartOfDay();
                        break;
                    default:
                        deadline = today.plusDays(1).atStartOfDay();
                    }
                }
                t.setDeadline(deadline);
            }

            long minutes = Duration.between(now, deadline).toMinutes();
            if (minutes < 0)
                minutes = 0;
            long rounded = (minutes / 5) * 5;
            long days = rounded / (60 * 24);
            long hours = (rounded % (60 * 24)) / 60;
            long mins = rounded % 60;
            t.setTimeUntilDue(String.format("%d日%d時間%d分", days, hours, mins));

            String newCategory;
            if (minutes <= 60 * 24) {
                newCategory = "今日";
            } else if (minutes <= 60 * 24 * 2) {
                newCategory = "明日";
            } else if (minutes <= 60 * 24 * 7) {
                newCategory = "今週";
            } else if (minutes <= 60 * 24 * 14) {
                newCategory = "来週";
            } else if (minutes <= 60 * 24 * 21) {
                newCategory = "再来週";
            } else {
                newCategory = t.getCategory();
            }

            if (newCategory != null && !newCategory.equals(t.getCategory())) {
                t.setCategory(newCategory);
                repository.updateTask(t);
            }
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
        if (task.getCompletedAt() == null) {
            LocalDateTime deadline = calculateDeadline(task.getCategory());
            task.setDeadline(deadline);
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
