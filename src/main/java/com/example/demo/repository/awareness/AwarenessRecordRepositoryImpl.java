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
        String sql = "SELECT id, awareness, opinion, awareness_level FROM awareness_records";
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
}
