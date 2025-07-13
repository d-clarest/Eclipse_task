package com.example.demo.repository.routine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.LocalDate;
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
            java.sql.Date sd = rs.getDate("start_date");
            if (sd != null) {
                r.setStartDate(sd.toLocalDate());
            }
            java.sql.Time tm = rs.getTime("timing");
            if (tm != null) {
                r.setTiming(tm.toLocalTime());
            }
            return r;
        }
    };

    @Override
    public List<Routine> findAll() {
        String sql = "SELECT id, name, type, frequency, start_date, timing FROM routines ORDER BY id";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public void insertRoutine(Routine routine) {
        String sql = "INSERT INTO routines (name, type, frequency, start_date, timing) VALUES (?, ?, ?, ?, ?)";
        java.sql.Date sd = routine.getStartDate() != null ? java.sql.Date.valueOf(routine.getStartDate()) : null;
        java.sql.Time tm = routine.getTiming() != null ? java.sql.Time.valueOf(routine.getTiming()) : null;
        jdbcTemplate.update(sql, routine.getName(), routine.getType(), routine.getFrequency(), sd, tm);
    }

    @Override
    public void updateRoutine(Routine routine) {
        String sql = "UPDATE routines SET name = ?, type = ?, frequency = ?, start_date = ?, timing = ? WHERE id = ?";
        java.sql.Date sd = routine.getStartDate() != null ? java.sql.Date.valueOf(routine.getStartDate()) : null;
        java.sql.Time tm = routine.getTiming() != null ? java.sql.Time.valueOf(routine.getTiming()) : null;
        jdbcTemplate.update(sql, routine.getName(), routine.getType(), routine.getFrequency(), sd, tm, routine.getId());
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM routines WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
