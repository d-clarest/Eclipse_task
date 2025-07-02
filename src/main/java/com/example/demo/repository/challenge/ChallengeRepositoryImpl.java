package com.example.demo.repository.challenge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Challenge;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Challenge> findAll() {
        String sql = "SELECT id, title FROM challenges";
        return jdbcTemplate.query(sql, new RowMapper<Challenge>() {
            @Override
            public Challenge mapRow(ResultSet rs, int rowNum) throws SQLException {
                Challenge c = new Challenge();
                c.setId(rs.getInt("id"));
                c.setTitle(rs.getString("title"));
                return c;
            }
        });
    }

    @Override
    public void insertChallenge(Challenge challenge) {
        String sql = "INSERT INTO challenges (title) VALUES (?)";
        jdbcTemplate.update(sql, challenge.getTitle());
    }
}
