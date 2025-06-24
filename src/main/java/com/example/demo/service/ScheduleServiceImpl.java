package com.example.demo.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Schedule;
import com.example.demo.form.ScheduleUpdateForm;
import com.example.demo.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository repository;

    @Override
    public List<Schedule> getAllSchedules() {
        log.debug("Fetching all schedules");
        List<Schedule> list = repository.findAll();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        for (Schedule s : list) {
            LocalDateTime start = LocalDateTime.of(s.getScheduleDate(), s.getStartTime());
            long minutes = Duration.between(now, start).toMinutes();
            if (minutes < 0) minutes = 0;
            long rounded = (minutes / 5) * 5;

            long days = rounded / (60 * 24);
            long hours = (rounded % (60 * 24)) / 60;
            long mins = rounded % 60;

            s.setTimeUntilStart(String.format("%d日%d時間%d分", days, hours, mins));
        }
        return list;
    }

    @Override
    public void updateCompletedDay(Schedule schedule) {
        log.debug("Updating completed day for {} to {}", schedule.getTitle(), schedule.getCompletedDay());
        repository.updateCompletedDay(schedule);
    }

    @Override
    public void updateSchedule(ScheduleUpdateForm form) {
        log.debug("Updating schedule id {} - {} on {}", form.getId(), form.getTitle(), form.getScheduleDate());
        repository.updateSchedule(form);
    }

    @Override
    public void addSchedule(Schedule schedule) {
        log.debug("Adding schedule {} on {}", schedule.getTitle(), schedule.getScheduleDate());
        repository.insertSchedule(schedule);
    }

    @Override
    public void deleteScheduleById(int id) {
        log.debug("Deleting schedule with id {}", id);
        repository.deleteById(id);
    }
}
