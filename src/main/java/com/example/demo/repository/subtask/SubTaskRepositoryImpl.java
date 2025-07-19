package com.example.demo.repository.subtask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.SubTask;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubTaskRepositoryImpl implements SubTaskRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<SubTask> ROW_MAPPER = new RowMapper<SubTask>() {
        @Override
        public SubTask mapRow(ResultSet rs, int rowNum) throws SQLException {
            SubTask s = new SubTask();
            s.setId(rs.getInt("id"));
            s.setTaskId(rs.getInt("task_id"));
            s.setTitle(rs.getString("title"));
            java.sql.Timestamp deadline = rs.getTimestamp("deadline");
            if (deadline != null) {
                s.setDeadline(deadline.toLocalDateTime());
            }
            java.sql.Date completed = rs.getDate("completed_at");
            if (completed != null) {
                s.setCompletedAt(completed.toLocalDate());
            }
            return s;
        }
    };

    @Override
    public List<SubTask> findAll() {
        String sql = "SELECT id, task_id, title, deadline, completed_at FROM sub_tasks ORDER BY id";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public List<SubTask> findByTaskId(int taskId) {
        String sql = "SELECT id, task_id, title, deadline, completed_at FROM sub_tasks WHERE task_id = ? ORDER BY id";
        return jdbcTemplate.query(sql, ROW_MAPPER, taskId);
    }

    @Override
    public void insertSubTask(SubTask subTask) {
        String sql = "INSERT INTO sub_tasks (task_id, title, deadline, completed_at) VALUES (?, ?, ?, ?)";
        java.sql.Timestamp deadline = subTask.getDeadline() != null ? java.sql.Timestamp.valueOf(subTask.getDeadline()) : null;
        java.sql.Date completed = subTask.getCompletedAt() != null ? java.sql.Date.valueOf(subTask.getCompletedAt()) : null;
        jdbcTemplate.update(sql, subTask.getTaskId(), subTask.getTitle(), deadline, completed);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        String sql = "UPDATE sub_tasks SET title = ?, deadline = ?, completed_at = ? WHERE id = ?";
        java.sql.Timestamp deadline = subTask.getDeadline() != null ? java.sql.Timestamp.valueOf(subTask.getDeadline()) : null;
        java.sql.Date completed = subTask.getCompletedAt() != null ? java.sql.Date.valueOf(subTask.getCompletedAt()) : null;
        jdbcTemplate.update(sql, subTask.getTitle(), deadline, completed, subTask.getId());
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM sub_tasks WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteByTaskId(int taskId) {
        String sql = "DELETE FROM sub_tasks WHERE task_id = ?";
        jdbcTemplate.update(sql, taskId);
    }

    @Override
    public int countByTaskId(int taskId) {
        String sql = "SELECT COUNT(*) FROM sub_tasks WHERE task_id = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, taskId);
        return result != null ? result : 0;
    }

    @Override
    public int countCompletedByTaskId(int taskId) {
        String sql = "SELECT COUNT(*) FROM sub_tasks WHERE task_id = ? AND completed_at IS NOT NULL";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, taskId);
        return result != null ? result : 0;
    }

    @Override
    public void markAllCompletedByTaskId(int taskId, java.time.LocalDate completedAt) {
        String sql = "UPDATE sub_tasks SET completed_at = ? WHERE task_id = ? AND completed_at IS NULL";
        java.sql.Date completed = completedAt != null ? java.sql.Date.valueOf(completedAt) : null;
        jdbcTemplate.update(sql, completed, taskId);
    }

    @Override
    public void markAllUncompletedByTaskId(int taskId) {
        String sql = "UPDATE sub_tasks SET completed_at = NULL WHERE task_id = ?";
        jdbcTemplate.update(sql, taskId);
    }
}
