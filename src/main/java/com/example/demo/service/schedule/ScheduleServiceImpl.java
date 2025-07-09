package com.example.demo.service.schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Schedule;
import com.example.demo.repository.schedule.ScheduleRepository;

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
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);//truncatedTo(ChronoUnit.MINUTES) により秒以下を切り捨て、例えば「2024-04-20T15:43:27」なら「2024-04-20T15:43:00」
        for (Schedule s : list) {
            //完了日がnullでない場合、つまり完了済みの予定の場合は、開始までの時間は表示しなくてよい。
            if (s.getCompletedDay() != null) {
                s.setTimeUntilStart(null);//開始までの時間がnull
                s.setExpired(false);
                continue;
            }

            //未完了の予定の場合は、以下で完了までの時間を計算
            LocalDateTime start = LocalDateTime.of(s.getScheduleDate(), s.getStartTime());//合成して「2024-04-20T15:30:00」という日時を表す
            long minutes = Duration.between(now, start).toMinutes();//分単位で今から開始までの時間を取得
            boolean expired = minutes <= 0;
            if (minutes < 0) minutes = 0;//期限切れ
            long rounded = (minutes / 5) * 5;//5分単位に変換
            long days = rounded / (60 * 24);//日
            long hours = (rounded % (60 * 24)) / 60;//時間
            long mins = rounded % 60;//分
            s.setTimeUntilStart(String.format("%d日%d時間%d分", days, hours, mins));
            s.setExpired(expired);
        }
        
        return list;
    }

    @Override
    public void updateCompletedDay(Schedule schedule) {
        log.debug("Updating completed day for {} to {}", schedule.getTitle(), schedule.getCompletedDay());
        repository.updateCompletedDay(schedule);
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        log.debug("Updating schedule id {} - {} on {}", schedule.getId(), schedule.getTitle(), schedule.getScheduleDate());
        repository.updateSchedule(schedule);
    }

    @Override
    public void addSchedule(Schedule schedule) {
        log.debug("Adding schedule {} on {}", schedule.getTitle(), schedule.getScheduleDate());
        repository.insertSchedule(schedule);//scheduleService.addSchedule(schedule)
    }

    @Override
    public void deleteScheduleById(int id) {
        log.debug("Deleting schedule with id {}", id);
        repository.deleteById(id);
    }

    @Override
    public int getTotalCompletedPoints() {
        log.debug("Fetching total completed points");
        return repository.sumCompletedPoints();
    }
}
