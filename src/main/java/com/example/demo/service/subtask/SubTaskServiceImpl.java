package com.example.demo.service.subtask;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.example.demo.entity.SubTask;
import com.example.demo.entity.Task;
import com.example.demo.repository.subtask.SubTaskRepository;
import com.example.demo.repository.task.TaskRepository;
import com.example.demo.service.task.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubTaskServiceImpl implements SubTaskService {

    private final SubTaskRepository repository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Override
    public List<SubTask> getAllSubTasks() {
        log.debug("Fetching all subtasks");
        return repository.findAll().stream().map(st -> {
            LocalDateTime parentDeadline = null;
            try {
                parentDeadline = taskRepository.findById(st.getTaskId()).getDeadline();
            } catch (Exception e) {
                log.debug("Parent task not found for {}", st.getTaskId());
            }
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            if (st.getDeadline() == null) {
                st.setDeadline(parentDeadline);
            }
            if (st.getCompletedAt() != null) {
                st.setTimeUntilDue(null);
            } else {
                LocalDateTime deadline = st.getDeadline();
                if (deadline == null) {
                    st.setExpired(true);
                    st.setTimeUntilDue("-");
                } else {
                    long minutes = Duration.between(now, deadline).toMinutes();
                    boolean expired = minutes <= 0;
                    if (minutes < 0) minutes = 0;
                    long days = minutes / (60 * 24);
                    long hours = (minutes % (60 * 24)) / 60;
                    long mins = minutes % 60;
                    st.setTimeUntilDue(String.format("%d日%d時間%d分", days, hours, mins));
                    st.setExpired(expired);
                }
            }
            return st;
        }).sorted(Comparator.comparing(SubTask::getDeadline,
                Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    @Override
    public List<SubTask> getSubTasks(int taskId) {
        log.debug("Fetching subtasks for task {}", taskId);
        List<SubTask> list = repository.findByTaskId(taskId);
        LocalDateTime parentDeadline = null;
        try {
            parentDeadline = taskRepository.findById(taskId).getDeadline();
        } catch (Exception e) {
            log.debug("Parent task not found for {}", taskId);
        }
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        for (SubTask st : list) {
            if (st.getDeadline() == null) {
                st.setDeadline(parentDeadline);
            }
            if (st.getCompletedAt() != null) {
                st.setTimeUntilDue(null);
                continue;
            }
            LocalDateTime deadline = st.getDeadline();
            if (deadline == null) {
                st.setExpired(true);
                st.setTimeUntilDue("-");
                continue;
            }
            long minutes = Duration.between(now, deadline).toMinutes();
            boolean expired = minutes <= 0;
            if (minutes < 0) minutes = 0;
            long days = minutes / (60 * 24);
            long hours = (minutes % (60 * 24)) / 60;
            long mins = minutes % 60;
            st.setTimeUntilDue(String.format("%d日%d時間%d分", days, hours, mins));
            st.setExpired(expired);
        }
        return list.stream()
                .sorted(Comparator.comparing(SubTask::getDeadline,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        log.debug("Adding subtask {}", subTask.getTitle());
        if (subTask.getDeadline() == null) {
            try {
                subTask.setDeadline(taskRepository.findById(subTask.getTaskId()).getDeadline());
            } catch (Exception e) {
                log.debug("Parent task not found for {}", subTask.getTaskId());
            }
        }
        repository.insertSubTask(subTask);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        log.debug("Updating subtask id {}", subTask.getId());
        if (subTask.getDeadline() == null) {
            try {
                subTask.setDeadline(taskRepository.findById(subTask.getTaskId()).getDeadline());
            } catch (Exception e) {
                log.debug("Parent task not found for {}", subTask.getTaskId());
            }
        }
        repository.updateSubTask(subTask);

        int total = repository.countByTaskId(subTask.getTaskId());
        int completed = repository.countCompletedByTaskId(subTask.getTaskId());
        if (total > 0) {
            Task parent = taskRepository.findById(subTask.getTaskId());
            if (completed == total) {
                // all subtasks done -> ensure parent marked completed
                if (parent.getCompletedAt() == null) {
                    parent.setCompletedAt(LocalDate.now());
                    taskRepository.updateTask(parent); // avoid resetting subtasks
                }
            } else {
                // some subtasks not done -> ensure parent marked uncompleted
                if (parent.getCompletedAt() != null) {
                    parent.setCompletedAt(null);
                    taskRepository.updateTask(parent); // avoid resetting subtasks
                }
            }
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        log.debug("Deleting subtask id {}", id);
        repository.deleteById(id);
    }

    @Override
    public int countSubTasks(int taskId) {
        return repository.countByTaskId(taskId);
    }

    @Override
    public int countCompletedSubTasks(int taskId) {
        return repository.countCompletedByTaskId(taskId);
    }

    @Override
    public void markAllCompletedByTaskId(int taskId, java.time.LocalDate completedAt) {
        log.debug("Marking all subtasks of task {} completed", taskId);
        repository.markAllCompletedByTaskId(taskId, completedAt);
    }

    @Override
    public void markAllUncompletedByTaskId(int taskId) {
        log.debug("Marking all subtasks of task {} uncompleted", taskId);
        repository.markAllUncompletedByTaskId(taskId);
    }
}
