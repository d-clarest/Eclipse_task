package com.example.demo.repository.dream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DreamRecord;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DreamRecordRepositoryImpl implements DreamRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<DreamRecord> findAll() {
        String sql = "SELECT id, dream, completed_at FROM dream_records ORDER BY id DESC";
        return jdbcTemplate.query(sql, new RowMapper<DreamRecord>() {
            @Override
            public DreamRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
                DreamRecord r = new DreamRecord();
                r.setId(rs.getInt("id"));
                r.setDream(rs.getString("dream"));
                java.sql.Date comp = rs.getDate("completed_at");
                if (comp != null) {
                    r.setCompletedAt(comp.toLocalDate());
                }
                return r;
            }
        });
    }

    @Override
    public void insertRecord(DreamRecord record) {
        String sql = "INSERT INTO dream_records (dream, completed_at) VALUES (?, ?)";
        java.sql.Date comp = record.getCompletedAt() != null ? java.sql.Date.valueOf(record.getCompletedAt()) : null;
        jdbcTemplate.update(sql, record.getDream(), comp);
    }

    @Override
    public void updateRecord(DreamRecord record) {
        String sql = "UPDATE dream_records SET dream = ?, completed_at = ? WHERE id = ?";
        java.sql.Date comp = record.getCompletedAt() != null ? java.sql.Date.valueOf(record.getCompletedAt()) : null;
        jdbcTemplate.update(sql, record.getDream(), comp, record.getId());
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM dream_records WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
