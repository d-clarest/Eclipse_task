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
        String sql = "SELECT id, title, risk, expected_result, strategy, actual_result, improvement_plan, challenge_level, challenge_date FROM challenges";
        return jdbcTemplate.query(sql, new RowMapper<Challenge>() {
            @Override
            public Challenge mapRow(ResultSet rs, int rowNum) throws SQLException {
                Challenge c = new Challenge();
                c.setId(rs.getInt("id"));
                c.setTitle(rs.getString("title"));
                c.setRisk(rs.getString("risk"));
                c.setExpectedResult(rs.getString("expected_result"));
                c.setStrategy(rs.getString("strategy"));
                c.setActualResult(rs.getString("actual_result"));
                c.setImprovementPlan(rs.getString("improvement_plan"));
                c.setChallengeLevel(rs.getInt("challenge_level"));
                java.sql.Date date = rs.getDate("challenge_date");
                if (date != null) {
                    c.setChallengeDate(date.toLocalDate());
                }
                return c;
            }
        });
    }

    @Override
    public void insertChallenge(Challenge challenge) {
        String sql = "INSERT INTO challenges (title, risk, expected_result, strategy, actual_result, improvement_plan, challenge_level, challenge_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        java.sql.Date date = challenge.getChallengeDate() != null ? java.sql.Date.valueOf(challenge.getChallengeDate()) : null;
        jdbcTemplate.update(sql,
                challenge.getTitle(),
                challenge.getRisk(),
                challenge.getExpectedResult(),
                challenge.getStrategy(),
                challenge.getActualResult(),
                challenge.getImprovementPlan(),
                challenge.getChallengeLevel(),
                date);
    }

    @Override
    public void updateChallenge(Challenge challenge) {
        String sql = "UPDATE challenges SET title = ?, risk = ?, expected_result = ?, strategy = ?, actual_result = ?, improvement_plan = ?, challenge_level = ?, challenge_date = ? WHERE id = ?";
        java.sql.Date date = challenge.getChallengeDate() != null ? java.sql.Date.valueOf(challenge.getChallengeDate()) : null;
        jdbcTemplate.update(sql,
                challenge.getTitle(),
                challenge.getRisk(),
                challenge.getExpectedResult(),
                challenge.getStrategy(),
                challenge.getActualResult(),
                challenge.getImprovementPlan(),
                challenge.getChallengeLevel(),
                date,
                challenge.getId());
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM challenges WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public int sumCompletedPoints() {
        String sql = "SELECT COALESCE(SUM(CASE actual_result "
                + "WHEN '成功' THEN challenge_level * 2 "
                + "WHEN '失敗' THEN challenge_level "
                + "ELSE 0 END), 0) "
                + "FROM challenges WHERE challenge_date IS NOT NULL";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }
}
