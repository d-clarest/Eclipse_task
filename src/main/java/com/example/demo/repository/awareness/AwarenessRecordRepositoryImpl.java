package com.example.demo.repository.awareness;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AwarenessRecord;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AwarenessRecordRepositoryImpl implements AwarenessRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<AwarenessRecord> findAll() {
        String sql = "SELECT id, awareness, opinion, awareness_level FROM awareness_records ORDER BY id DESC";
        return jdbcTemplate.query(sql, new RowMapper<AwarenessRecord>() {
            @Override
            public AwarenessRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
                AwarenessRecord r = new AwarenessRecord();
                r.setId(rs.getInt("id"));
                r.setAwareness(rs.getString("awareness"));
                r.setOpinion(rs.getString("opinion"));
                r.setAwarenessLevel(rs.getInt("awareness_level"));
                return r;
            }
        });
    }

    @Override
    public void insertRecord(AwarenessRecord record) {
        String sql = "INSERT INTO awareness_records (awareness, opinion, awareness_level) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                record.getAwareness(),
                record.getOpinion(),
                record.getAwarenessLevel());
    }

    @Override
    public void updateRecord(AwarenessRecord record) {
        String sql = "UPDATE awareness_records SET awareness = ?, opinion = ?, awareness_level = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                record.getAwareness(),
                record.getOpinion(),
                record.getAwarenessLevel(),
                record.getId());
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM awareness_records WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public int sumAwarenessLevels() {
        String sql = "SELECT COALESCE(SUM(awareness_level), 0) FROM awareness_records";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }
}
