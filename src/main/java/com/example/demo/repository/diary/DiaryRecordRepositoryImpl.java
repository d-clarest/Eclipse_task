package com.example.demo.repository.diary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DiaryRecord;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DiaryRecordRepositoryImpl implements DiaryRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<DiaryRecord> findAll() {
        String sql = "SELECT id, record_date, content FROM diary_records ORDER BY id DESC";
        return jdbcTemplate.query(sql, new RowMapper<DiaryRecord>() {
            @Override
            public DiaryRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
                DiaryRecord d = new DiaryRecord();
                d.setId(rs.getInt("id"));
                d.setRecordDate(rs.getDate("record_date").toLocalDate());
                d.setContent(rs.getString("content"));
                return d;
            }
        });
    }

    @Override
    public void insertRecord(DiaryRecord record) {
        String sql = "INSERT INTO diary_records (record_date, content) VALUES (?, ?)";
        LocalDate date = record.getRecordDate();
        java.sql.Date sqlDate = date != null ? java.sql.Date.valueOf(date) : null;
        jdbcTemplate.update(sql, sqlDate, record.getContent());
    }

    @Override
    public void updateRecord(DiaryRecord record) {
        String sql = "UPDATE diary_records SET record_date = ?, content = ? WHERE id = ?";
        java.sql.Date sqlDate = record.getRecordDate() != null ? java.sql.Date.valueOf(record.getRecordDate()) : null;
        jdbcTemplate.update(sql, sqlDate, record.getContent(), record.getId());
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM diary_records WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
