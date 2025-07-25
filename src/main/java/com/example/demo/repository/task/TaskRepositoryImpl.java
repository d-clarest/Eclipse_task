package com.example.demo.repository.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Task;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Task> findAll() {
        String sql = "SELECT id, title, category, deadline, level, created_at, updated_at, completed_at "
                + "FROM tasks ORDER BY CASE category "
                + "WHEN '今日' THEN 1 "
                + "WHEN '明日' THEN 2 "
                + "WHEN '今週' THEN 3 "
                + "WHEN '来週' THEN 4 "
                + "WHEN '再来週' THEN 5 "
                + "ELSE 6 END";
        return jdbcTemplate.query(sql, new RowMapper<Task>() {
            @Override
            public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                Task t = new Task();
                t.setId(rs.getInt("id"));
                t.setTitle(rs.getString("title"));
                t.setCategory(rs.getString("category"));
                java.sql.Timestamp deadline = rs.getTimestamp("deadline");
                if (deadline != null) {
                    t.setDeadline(deadline.toLocalDateTime());
                }
                t.setLevel(rs.getInt("level"));
                java.sql.Timestamp created = rs.getTimestamp("created_at");
                if (created != null) {
                    t.setCreatedAt(created.toLocalDateTime());
                }
                java.sql.Timestamp updated = rs.getTimestamp("updated_at");
                if (updated != null) {
                    t.setUpdatedAt(updated.toLocalDateTime());
                }
                java.sql.Date completed = rs.getDate("completed_at");
                if (completed != null) {
                    t.setCompletedAt(completed.toLocalDate());
                }
                return t;
            }
        });
    }

    @Override
    public Task findById(int id) {
        String sql = "SELECT id, title, category, deadline, level, created_at, updated_at, completed_at "
                + "FROM tasks WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new RowMapper<Task>() {
            @Override
            public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                Task t = new Task();
                t.setId(rs.getInt("id"));
                t.setTitle(rs.getString("title"));
                t.setCategory(rs.getString("category"));
                java.sql.Timestamp deadline = rs.getTimestamp("deadline");
                if (deadline != null) {
                    t.setDeadline(deadline.toLocalDateTime());
                }
                t.setLevel(rs.getInt("level"));
                java.sql.Timestamp created = rs.getTimestamp("created_at");
                if (created != null) {
                    t.setCreatedAt(created.toLocalDateTime());
                }
                java.sql.Timestamp updated = rs.getTimestamp("updated_at");
                if (updated != null) {
                    t.setUpdatedAt(updated.toLocalDateTime());
                }
                java.sql.Date completed = rs.getDate("completed_at");
                if (completed != null) {
                    t.setCompletedAt(completed.toLocalDate());
                }
                return t;
            }
        }, id);
    }

    @Override
    public void insertTask(Task task) {
        String sql = "INSERT INTO tasks (title, category, deadline, level, created_at, updated_at, completed_at) VALUES (?, ?, ?, ?, NOW(), NOW(), ?)";
        java.sql.Timestamp deadline = task.getDeadline() != null ? java.sql.Timestamp.valueOf(task.getDeadline()) : null;
        java.sql.Date completed = task.getCompletedAt() != null ? java.sql.Date.valueOf(task.getCompletedAt()) : null;
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getCategory(),
                deadline,
                task.getLevel(),
                completed);
    }

    @Override
    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, category = ?, deadline = ?, level = ?, completed_at = ?, updated_at = NOW() WHERE id = ?";
        java.sql.Timestamp deadline = task.getDeadline() != null ? java.sql.Timestamp.valueOf(task.getDeadline()) : null;
        java.sql.Date completed = task.getCompletedAt() != null ? java.sql.Date.valueOf(task.getCompletedAt()) : null;
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getCategory(),
                deadline,
                task.getLevel(),
                completed,
                task.getId());
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public int sumCompletedLevels() {
        String sql = "SELECT COALESCE(SUM(level), 0) FROM tasks WHERE completed_at IS NOT NULL";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }

    @Override
    public boolean existsByTitleAndDate(String title, java.time.LocalDate date) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE title = ? AND DATE(created_at) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, title, java.sql.Date.valueOf(date));
        return count != null && count > 0;
    }
}
