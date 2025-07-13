package com.example.demo.repository.page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DreamPage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DreamPageRepositoryImpl implements DreamPageRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public DreamPage findByDreamId(int dreamId) {
        String sql = "SELECT id, dream_id, content FROM dream_pages WHERE dream_id = ?";
        List<DreamPage> list = jdbcTemplate.query(sql, new RowMapper<DreamPage>() {
            @Override
            public DreamPage mapRow(ResultSet rs, int rowNum) throws SQLException {
                DreamPage p = new DreamPage();
                p.setId(rs.getInt("id"));
                p.setDreamId(rs.getInt("dream_id"));
                p.setContent(rs.getString("content"));
                return p;
            }
        }, dreamId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void insertPage(DreamPage page) {
        String sql = "INSERT INTO dream_pages (dream_id, content) VALUES (?, ?)";
        jdbcTemplate.update(sql, page.getDreamId(), page.getContent());
    }

    @Override
    public void updatePage(DreamPage page) {
        String sql = "UPDATE dream_pages SET content = ? WHERE id = ?";
        jdbcTemplate.update(sql, page.getContent(), page.getId());
    }

    @Override
    public void deleteByDreamId(int dreamId) {
        String sql = "DELETE FROM dream_pages WHERE dream_id = ?";
        jdbcTemplate.update(sql, dreamId);
    }
}
