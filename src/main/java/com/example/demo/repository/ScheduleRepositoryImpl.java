package com.example.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Schedule;
import com.example.demo.form.ScheduleUpdateForm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Schedule> findAll() {
        String sql = "SELECT add_flag, title, day_of_week, schedule_date, start_time, end_time, location, detail, feedback,point, completed_day FROM schedules";
        return jdbcTemplate.query(sql, new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                Schedule s = new Schedule();
                s.setAddFlag(rs.getBoolean("add_flag"));
                s.setTitle(rs.getString("title"));
                s.setDayOfWeek(rs.getString("day_of_week"));
                LocalDate date = rs.getDate("schedule_date").toLocalDate();
                s.setScheduleDate(date);
                LocalTime st = rs.getTime("start_time").toLocalTime();
                s.setStartTime(st);
                LocalTime et = rs.getTime("end_time").toLocalTime();
                s.setEndTime(et);
                s.setLocation(rs.getString("location"));
                s.setDetail(rs.getString("detail"));
                s.setFeedback(rs.getString("feedback"));
                s.setPoint(rs.getInt("point"));
                java.sql.Date comp = rs.getDate("completed_day");
                if (comp != null) {
                    s.setCompletedDay(comp.toLocalDate());
                }
                return s;
            }
        });
    }

    @Override
    public void updateCompletedDay(Schedule schedule) {
        String sql = "UPDATE schedules SET completed_day = ? WHERE title = ? AND schedule_date = ?";
        java.sql.Date day = schedule.getCompletedDay() != null ? java.sql.Date.valueOf(schedule.getCompletedDay()) : null;
        jdbcTemplate.update(sql, day, schedule.getTitle(), java.sql.Date.valueOf(schedule.getScheduleDate()));
    }

    @Override
    public void updateSchedule(ScheduleUpdateForm form) {
        String sql = "UPDATE schedules SET add_flag = ?, title = ?, day_of_week = ?, schedule_date = ?, start_time = ?, end_time = ?, location = ?, detail = ?, feedback = ?, point = ?, completed_day = ? WHERE title = ? AND schedule_date = ?";
        java.sql.Date schedDate = java.sql.Date.valueOf(form.getScheduleDate());
        java.sql.Time start = java.sql.Time.valueOf(form.getStartTime());
        java.sql.Time end = java.sql.Time.valueOf(form.getEndTime());
        java.sql.Date completed = form.getCompletedDay() != null ? java.sql.Date.valueOf(form.getCompletedDay()) : null;
        jdbcTemplate.update(sql,
                form.isAddFlag(),
                form.getTitle(),
                form.getDayOfWeek(),
                schedDate,
                start,
                end,
                form.getLocation(),
                form.getDetail(),
                form.getFeedback(),
                form.getPoint(),
                completed,
                form.getOldTitle(),
                java.sql.Date.valueOf(form.getOldScheduleDate()));
    }

    @Override
    public void insertSchedule(Schedule schedule) {
        String sql = "INSERT INTO schedules (add_flag, title, day_of_week, schedule_date, start_time, end_time, location, detail, feedback, point, completed_day) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        java.sql.Date schedDate = java.sql.Date.valueOf(schedule.getScheduleDate());
        java.sql.Time start = java.sql.Time.valueOf(schedule.getStartTime());
        java.sql.Time end = java.sql.Time.valueOf(schedule.getEndTime());
        java.sql.Date completed = schedule.getCompletedDay() != null ? java.sql.Date.valueOf(schedule.getCompletedDay()) : null;
        jdbcTemplate.update(sql,
                schedule.isAddFlag(),
                schedule.getTitle(),
                schedule.getDayOfWeek(),
                schedDate,
                start,
                end,
                schedule.getLocation(),
                schedule.getDetail(),
                schedule.getFeedback(),
                schedule.getPoint(),
                completed);
    }
}
