package com.example.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
        String sql = "SELECT task_name, due_date, confirmed FROM tasks";
        return jdbcTemplate.query(sql, new RowMapper<Task>() {
            @Override
            public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                Task t = new Task();
                t.setTaskName(rs.getString("task_name"));
                LocalDate date = rs.getDate("due_date").toLocalDate();
                t.setDueDate(date);
                t.setConfirmed(rs.getBoolean("confirmed"));
                return t;
            }
        });
    }

    @Override
    public void updateConfirmed(Task task) {
        String sql = "UPDATE tasks SET confirmed = ? WHERE task_name = ? AND due_date = ?";
        jdbcTemplate.update(sql, task.isConfirmed(), task.getTaskName(), java.sql.Date.valueOf(task.getDueDate()));
    }

    @Override
    public void updateDueDate(Task task) {
        String sql = "UPDATE tasks SET due_date = ? WHERE task_name = ?";
        jdbcTemplate.update(sql, java.sql.Date.valueOf(task.getDueDate()), task.getTaskName());
    }

    @Override
    public void updateTaskName(String oldTaskName, String newTaskName) {
        String sql = "UPDATE tasks SET task_name = ? WHERE task_name = ?";
        jdbcTemplate.update(sql, newTaskName, oldTaskName);
    }
}
