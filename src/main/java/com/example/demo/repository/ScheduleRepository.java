package com.example.demo.repository;

import java.util.List;

import com.example.demo.entity.Schedule;

public interface ScheduleRepository {
    List<Schedule> findAll();

    void updateCompletedDay(Schedule schedule);
}
