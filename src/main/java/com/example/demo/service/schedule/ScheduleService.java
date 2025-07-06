package com.example.demo.service.schedule;

import java.util.List;

import com.example.demo.entity.Schedule;

public interface ScheduleService {
    List<Schedule> getAllSchedules();

    void updateCompletedDay(Schedule schedule);

    void updateSchedule(Schedule schedule);

    void addSchedule(Schedule schedule);

    void deleteScheduleById(int id);

    int getTotalCompletedPoints();
}
