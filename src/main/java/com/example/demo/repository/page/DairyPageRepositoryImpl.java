package com.example.demo.repository.page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DairyPage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DairyPageRepositoryImpl implements DairyPageRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<DairyPage> mapper = new RowMapper<DairyPage>() {
        @Override
        public DairyPage mapRow(ResultSet rs, int rowNum) throws SQLException {
            DairyPage p = new DairyPage();
            p.setId(rs.getInt("id"));
            p.setDairyId(rs.getInt("dairy_id"));
            p.setContent(rs.getString("content"));
            Timestamp created = rs.getTimestamp("created_at");
            if (created != null) {
                p.setCreatedAt(created.toLocalDateTime());
            }
            Timestamp updated = rs.getTimestamp("updated_at");
            if (updated != null) {
                p.setUpdatedAt(updated.toLocalDateTime());
            }
            return p;
        }
    };

    @Override
    public DairyPage findByDairyId(int dairyId) {
        String sql = "SELECT id, dairy_id, content, created_at, updated_at FROM dairy_pages WHERE dairy_id = ?";
        List<DairyPage> list = jdbcTemplate.query(sql, mapper, dairyId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void insertPage(DairyPage page) {
        String sql = "INSERT INTO dairy_pages (dairy_id, content) VALUES (?, ?)";
        jdbcTemplate.update(sql, page.getDairyId(), page.getContent());
    }

    @Override
    public void updatePage(DairyPage page) {
        String sql = "UPDATE dairy_pages SET content = ? WHERE id = ?";
        jdbcTemplate.update(sql, page.getContent(), page.getId());
    }

    @Override
    public void deleteByDairyId(int dairyId) {
        String sql = "DELETE FROM dairy_pages WHERE dairy_id = ?";
        jdbcTemplate.update(sql, dairyId);
    }
}
