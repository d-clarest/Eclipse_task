package com.example.demo.repository;

import java.util.List;

import com.example.demo.entity.Schedule;
import com.example.demo.form.ScheduleUpdateForm;

public interface ScheduleRepository {
    List<Schedule> findAll();

    void updateCompletedDay(Schedule schedule);

    void updateSchedule(ScheduleUpdateForm form);

    void insertSchedule(Schedule schedule);

    void deleteById(int id);

    int sumCompletedPoints();
}
