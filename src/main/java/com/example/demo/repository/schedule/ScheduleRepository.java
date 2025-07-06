package com.example.demo.repository.schedule;

import java.util.List;

import com.example.demo.entity.Schedule;

public interface ScheduleRepository {
    List<Schedule> findAll();

    void updateCompletedDay(Schedule schedule);

    void updateSchedule(Schedule schedule);

    void insertSchedule(Schedule schedule);

    void deleteById(int id);

    int sumCompletedPoints();
}
