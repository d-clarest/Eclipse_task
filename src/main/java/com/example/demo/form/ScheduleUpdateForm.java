package com.example.demo.form;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ScheduleUpdateForm {
    private int id;
    private String oldTitle;
    private LocalDate oldScheduleDate;

    private boolean addFlag;
    private String title;
    private String dayOfWeek;
    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String detail;
    private String feedback;
    private int point;
    private LocalDate completedDay;
}
