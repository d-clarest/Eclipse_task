package com.example.demo.repository.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
        String sql = "SELECT id, title, category, due_date, result, detail, level, created_at, updated_at, completed_at FROM tasks";
        return jdbcTemplate.query(sql, new RowMapper<Task>() {
            @Override
            public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                Task t = new Task();
                t.setId(rs.getInt("id"));
                t.setTitle(rs.getString("title"));
                t.setCategory(rs.getString("category"));
                java.sql.Date due = rs.getDate("due_date");
                if (due != null) {
                    t.setDueDate(due.toLocalDate());
                }
                t.setResult(rs.getString("result"));
                t.setDetail(rs.getString("detail"));
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
}
