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
                continue;
            }
            LocalDateTime deadline;
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
                        category = "今日";
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
            long minutes = Duration.between(now, deadline).toMinutes();
            if (minutes < 0)
                minutes = 0;
            long rounded = (minutes / 5) * 5;
            long days = rounded / (60 * 24);
            long hours = (rounded % (60 * 24)) / 60;
            long mins = rounded % 60;
            t.setTimeUntilDue(String.format("%d日%d時間%d分", days, hours, mins));
        }
        return list;
    }

    @Override
    public void addTask(Task task) {
        log.debug("Adding task {}", task.getTitle());
        repository.insertTask(task);
    }

    @Override
    public void updateTask(Task task) {
        log.debug("Updating task id {}", task.getId());
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
