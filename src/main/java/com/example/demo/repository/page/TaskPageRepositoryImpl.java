package com.example.demo.repository.page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TaskPage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskPageRepositoryImpl implements TaskPageRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public TaskPage findByTaskId(int taskId) {
        String sql = "SELECT id, task_id, content FROM task_pages WHERE task_id = ?";
        List<TaskPage> list = jdbcTemplate.query(sql, new RowMapper<TaskPage>() {
            @Override
            public TaskPage mapRow(ResultSet rs, int rowNum) throws SQLException {
                TaskPage p = new TaskPage();
                p.setId(rs.getInt("id"));
                p.setTaskId(rs.getInt("task_id"));
                p.setContent(rs.getString("content"));
                return p;
            }
        }, taskId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void insertPage(TaskPage page) {
        String sql = "INSERT INTO task_pages (task_id, content) VALUES (?, ?)";
        jdbcTemplate.update(sql, page.getTaskId(), page.getContent());
    }

    @Override
    public void updatePage(TaskPage page) {
        String sql = "UPDATE task_pages SET content = ? WHERE id = ?";
        jdbcTemplate.update(sql, page.getContent(), page.getId());
    }

    @Override
    public void deleteByTaskId(int taskId) {
        String sql = "DELETE FROM task_pages WHERE task_id = ?";
        jdbcTemplate.update(sql, taskId);
    }
}
