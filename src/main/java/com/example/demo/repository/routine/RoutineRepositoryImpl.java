package com.example.demo.repository.routine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Routine;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RoutineRepositoryImpl implements RoutineRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Routine> ROW_MAPPER = new RowMapper<Routine>() {
        @Override
        public Routine mapRow(ResultSet rs, int rowNum) throws SQLException {
            Routine r = new Routine();
            r.setId(rs.getInt("id"));
            r.setName(rs.getString("name"));
            r.setType(rs.getString("type"));
            r.setFrequency(rs.getString("frequency"));
            return r;
        }
    };

    @Override
    public List<Routine> findAll() {
        String sql = "SELECT id, name, type, frequency FROM routines ORDER BY id";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public void insertRoutine(Routine routine) {
        String sql = "INSERT INTO routines (name, type, frequency) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, routine.getName(), routine.getType(), routine.getFrequency());
    }

    @Override
    public void updateRoutine(Routine routine) {
        String sql = "UPDATE routines SET name = ?, type = ?, frequency = ? WHERE id = ?";
        jdbcTemplate.update(sql, routine.getName(), routine.getType(), routine.getFrequency(), routine.getId());
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM routines WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
