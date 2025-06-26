package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Schedule;
import com.example.demo.form.ScheduleUpdateForm;

public interface ScheduleService {
    List<Schedule> getAllSchedules();

    void updateCompletedDay(Schedule schedule);

    void updateSchedule(ScheduleUpdateForm form);

    void addSchedule(Schedule schedule);

    void deleteScheduleById(int id);

    int getTotalCompletedPoints();
}
