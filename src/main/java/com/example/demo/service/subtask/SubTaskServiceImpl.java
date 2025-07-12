package com.example.demo.service.subtask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.SubTask;
import com.example.demo.repository.subtask.SubTaskRepository;
import com.example.demo.service.task.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubTaskServiceImpl implements SubTaskService {

    private final SubTaskRepository repository;
    private final TaskService taskService;

    @Override
    public List<SubTask> getSubTasks(int taskId) {
        log.debug("Fetching subtasks for task {}", taskId);
        List<SubTask> list = repository.findByTaskId(taskId);
        LocalDateTime parentDeadline = null;
        try {
            parentDeadline = taskService.getTaskById(taskId).getDeadline();
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
        return list;
    }

    @Override
    public void addSubTask(SubTask subTask) {
        log.debug("Adding subtask {}", subTask.getTitle());
        if (subTask.getDeadline() == null) {
            try {
                subTask.setDeadline(taskService.getTaskById(subTask.getTaskId()).getDeadline());
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
                subTask.setDeadline(taskService.getTaskById(subTask.getTaskId()).getDeadline());
            } catch (Exception e) {
                log.debug("Parent task not found for {}", subTask.getTaskId());
            }
        }
        repository.updateSubTask(subTask);
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
}
