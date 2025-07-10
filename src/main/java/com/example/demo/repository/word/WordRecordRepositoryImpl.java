package com.example.demo.repository.word;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.WordRecord;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WordRecordRepositoryImpl implements WordRecordRepository {

    private final JdbcTemplate jdbcTemplate;
    //mySQLから単語データベースを全レコード取得。idの降順
    @Override
    public List<WordRecord> findAll() {
        String sql = "SELECT id, word, meaning, example, count FROM word_records ORDER BY id DESC";
        return jdbcTemplate.query(sql, new RowMapper<WordRecord>() {
            @Override
            public WordRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
                WordRecord w = new WordRecord();
                w.setId(rs.getInt("id"));
                w.setWord(rs.getString("word"));
                w.setMeaning(rs.getString("meaning"));
                w.setExample(rs.getString("example"));
                w.setCount(rs.getInt("count"));
                return w;
            }
        });
    }

    //新規レコードを追加
    @Override
    public void insertRecord(WordRecord record) {
        String sql = "INSERT INTO word_records (word, meaning, example) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                record.getWord(),
                record.getMeaning(),
                record.getExample());
    }

    //指定のidのレコードを更新
    @Override
    public void updateRecord(WordRecord record) {
        String sql = "UPDATE word_records SET word = ?, meaning = ?, example = ?, count = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                record.getWord(),
                record.getMeaning(),
                record.getExample(),
                record.getCount(),
                record.getId());
    }

    //指定のidのレコードを削除
    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM word_records WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    //単語データベースの全レコード数を取得
    @Override
    public int countRecords() {
        String sql = "SELECT COUNT(*) FROM word_records";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;//nullでないならresult、nullなら0
    }
}
