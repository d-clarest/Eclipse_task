package com.example.demo.repository.page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AwarenessPage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AwarenessPageRepositoryImpl implements AwarenessPageRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public AwarenessPage findByRecordId(int recordId) {
        String sql = "SELECT id, record_id, content FROM awareness_pages WHERE record_id = ?";
        List<AwarenessPage> list = jdbcTemplate.query(sql, new RowMapper<AwarenessPage>() {
            @Override
            public AwarenessPage mapRow(ResultSet rs, int rowNum) throws SQLException {
                AwarenessPage p = new AwarenessPage();
                p.setId(rs.getInt("id"));
                p.setRecordId(rs.getInt("record_id"));
                p.setContent(rs.getString("content"));
                return p;
            }
        }, recordId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void insertPage(AwarenessPage page) {
        String sql = "INSERT INTO awareness_pages (record_id, content) VALUES (?, ?)";
        jdbcTemplate.update(sql, page.getRecordId(), page.getContent());
    }

    @Override
    public void updatePage(AwarenessPage page) {
        String sql = "UPDATE awareness_pages SET content = ? WHERE id = ?";
        jdbcTemplate.update(sql, page.getContent(), page.getId());
    }

    @Override
    public void deleteByRecordId(int recordId) {
        String sql = "DELETE FROM awareness_pages WHERE record_id = ?";
        jdbcTemplate.update(sql, recordId);
    }
}
