package com.example.demo.service.routine;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Routine;
import com.example.demo.entity.Schedule;
import com.example.demo.entity.Task;
import com.example.demo.entity.Challenge;
import com.example.demo.service.schedule.ScheduleService;
import com.example.demo.service.task.TaskService;
import com.example.demo.service.challenge.ChallengeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoutineScheduler {

    private final RoutineService routineService;
    private final ScheduleService scheduleService;
    private final TaskService taskService;
    private final ChallengeService challengeService;

    @Scheduled(cron = "0 * * * * *")
    public void addDailySchedules() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = LocalDate.now();
        List<Routine> routines = routineService.getAllRoutines();
        for (Routine r : routines) {
            if ("予定".equals(r.getType()) && "毎日".equals(r.getFrequency()) && r.getTiming() != null) {
                LocalTime timing = r.getTiming().truncatedTo(ChronoUnit.MINUTES);
                if (now.equals(timing)) {
                    boolean exists = scheduleService.getAllSchedules().stream()
                            .anyMatch(s -> s.getTitle().equals(r.getName()) && today.equals(s.getScheduleDate()));
                    if (!exists) {
                        Schedule s = new Schedule();
                        s.setAddFlag(true);
                        s.setTitle(r.getName());
                        s.setDayOfWeek(getJapaneseDayOfWeek(today.getDayOfWeek()));
                        s.setScheduleDate(today);
                        s.setStartTime(r.getTiming());
                        s.setEndTime(r.getTiming().plusHours(1));
                        s.setLocation("");
                        s.setDetail("");
                        s.setFeedback("");
                        s.setPoint(1);
                        s.setCompletedDay(null);
                        scheduleService.addSchedule(s);
                        log.debug("Added schedule for routine {}", r.getName());
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void addWeeklySchedules() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = LocalDate.now();
        List<Routine> routines = routineService.getAllRoutines();
        for (Routine r : routines) {
            if ("予定".equals(r.getType()) && "毎週".equals(r.getFrequency())
                    && r.getTiming() != null && r.getStartDate() != null) {
                LocalTime timing = r.getTiming().truncatedTo(ChronoUnit.MINUTES);
                LocalDate start = r.getStartDate();
                long days = ChronoUnit.DAYS.between(start, today);
                if (days >= 0 && days % 7 == 0 && now.equals(timing)) {
                    boolean exists = scheduleService.getAllSchedules().stream()
                            .anyMatch(s -> s.getTitle().equals(r.getName()) && today.equals(s.getScheduleDate()));
                    if (!exists) {
                        Schedule s = new Schedule();
                        s.setAddFlag(true);
                        s.setTitle(r.getName());
                        s.setDayOfWeek(getJapaneseDayOfWeek(today.getDayOfWeek()));
                        s.setScheduleDate(today);
                        s.setStartTime(r.getTiming());
                        s.setEndTime(r.getTiming().plusHours(1));
                        s.setLocation("");
                        s.setDetail("");
                        s.setFeedback("");
                        s.setPoint(1);
                        s.setCompletedDay(null);
                        scheduleService.addSchedule(s);
                        log.debug("Added schedule for routine {}", r.getName());
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void addMonthlySchedules() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = LocalDate.now();
        List<Routine> routines = routineService.getAllRoutines();
        for (Routine r : routines) {
            if ("予定".equals(r.getType()) && "毎月".equals(r.getFrequency())
                    && r.getTiming() != null && r.getStartDate() != null) {
                LocalTime timing = r.getTiming().truncatedTo(ChronoUnit.MINUTES);
                LocalDate start = r.getStartDate();
                long months = ChronoUnit.MONTHS.between(start.withDayOfMonth(1), today.withDayOfMonth(1));
                if (months >= 0 && today.getDayOfMonth() == start.getDayOfMonth() && now.equals(timing)) {
                    boolean exists = scheduleService.getAllSchedules().stream()
                            .anyMatch(s -> s.getTitle().equals(r.getName()) && today.equals(s.getScheduleDate()));
                    if (!exists) {
                        Schedule s = new Schedule();
                        s.setAddFlag(true);
                        s.setTitle(r.getName());
                        s.setDayOfWeek(getJapaneseDayOfWeek(today.getDayOfWeek()));
                        s.setScheduleDate(today);
                        s.setStartTime(r.getTiming());
                        s.setEndTime(r.getTiming().plusHours(1));
                        s.setLocation("");
                        s.setDetail("");
                        s.setFeedback("");
                        s.setPoint(1);
                        s.setCompletedDay(null);
                        scheduleService.addSchedule(s);
                        log.debug("Added schedule for routine {}", r.getName());
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void addDailyTasks() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = LocalDate.now();
        List<Routine> routines = routineService.getAllRoutines();
        for (Routine r : routines) {
            if ("タスク".equals(r.getType()) && "毎日".equals(r.getFrequency()) && r.getTiming() != null) {
                LocalTime timing = r.getTiming().truncatedTo(ChronoUnit.MINUTES);
                if (now.equals(timing)) {
                    boolean exists = taskService.getAllTasks().stream()
                            .anyMatch(t -> t.getTitle().equals(r.getName()) &&
                                    t.getCreatedAt() != null &&
                                    today.equals(t.getCreatedAt().toLocalDate()));
                    if (!exists) {
                        Task t = new Task();
                        t.setTitle(r.getName());
                        t.setCategory("今日");
                        t.setLevel(1);
                        t.setCompletedAt(null);
                        taskService.addTask(t);
                        log.debug("Added task for routine {}", r.getName());
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void addWeeklyTasks() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = LocalDate.now();
        List<Routine> routines = routineService.getAllRoutines();
        for (Routine r : routines) {
            if ("タスク".equals(r.getType()) && "毎週".equals(r.getFrequency())
                    && r.getTiming() != null && r.getStartDate() != null) {
                LocalTime timing = r.getTiming().truncatedTo(ChronoUnit.MINUTES);
                LocalDate start = r.getStartDate();
                long days = ChronoUnit.DAYS.between(start, today);
                if (days >= 0 && days % 7 == 0 && now.equals(timing)) {
                    boolean exists = taskService.getAllTasks().stream()
                            .anyMatch(t -> t.getTitle().equals(r.getName()) &&
                                    t.getCreatedAt() != null &&
                                    today.equals(t.getCreatedAt().toLocalDate()));
                    if (!exists) {
                        Task t = new Task();
                        t.setTitle(r.getName());
                        t.setCategory("今日");
                        t.setLevel(1);
                        t.setCompletedAt(null);
                        taskService.addTask(t);
                        log.debug("Added task for routine {}", r.getName());
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void addMonthlyTasks() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = LocalDate.now();
        List<Routine> routines = routineService.getAllRoutines();
        for (Routine r : routines) {
            if ("タスク".equals(r.getType()) && "毎月".equals(r.getFrequency())
                    && r.getTiming() != null && r.getStartDate() != null) {
                LocalTime timing = r.getTiming().truncatedTo(ChronoUnit.MINUTES);
                LocalDate start = r.getStartDate();
                long months = ChronoUnit.MONTHS.between(start.withDayOfMonth(1), today.withDayOfMonth(1));
                if (months >= 0 && today.getDayOfMonth() == start.getDayOfMonth() && now.equals(timing)) {
                    boolean exists = taskService.getAllTasks().stream()
                            .anyMatch(t -> t.getTitle().equals(r.getName()) &&
                                    t.getCreatedAt() != null &&
                                    today.equals(t.getCreatedAt().toLocalDate()));
                    if (!exists) {
                        Task t = new Task();
                        t.setTitle(r.getName());
                        t.setCategory("今日");
                        t.setLevel(1);
                        t.setCompletedAt(null);
                        taskService.addTask(t);
                        log.debug("Added task for routine {}", r.getName());
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void addDailyChallenges() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<Routine> routines = routineService.getAllRoutines();
        for (Routine r : routines) {
            if ("挑戦".equals(r.getType()) && "毎日".equals(r.getFrequency()) && r.getTiming() != null) {
                LocalTime timing = r.getTiming().truncatedTo(ChronoUnit.MINUTES);
                if (now.equals(timing)) {
                    boolean exists = challengeService.getAllChallenges().stream()
                            .anyMatch(c -> c.getTitle().equals(r.getName()) && c.getChallengeDate() == null);
                    if (!exists) {
                        Challenge c = new Challenge();
                        c.setTitle(r.getName());
                        c.setChallengeLevel(1);
                        challengeService.addChallenge(c);
                        log.debug("Added challenge for routine {}", r.getName());
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void addWeeklyChallenges() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = LocalDate.now();
        List<Routine> routines = routineService.getAllRoutines();
        for (Routine r : routines) {
            if ("挑戦".equals(r.getType()) && "毎週".equals(r.getFrequency())
                    && r.getTiming() != null && r.getStartDate() != null) {
                LocalTime timing = r.getTiming().truncatedTo(ChronoUnit.MINUTES);
                LocalDate start = r.getStartDate();
                long days = ChronoUnit.DAYS.between(start, today);
                if (days >= 0 && days % 7 == 0 && now.equals(timing)) {
                    boolean exists = challengeService.getAllChallenges().stream()
                            .anyMatch(c -> c.getTitle().equals(r.getName()) && c.getChallengeDate() == null);
                    if (!exists) {
                        Challenge c = new Challenge();
                        c.setTitle(r.getName());
                        c.setChallengeLevel(1);
                        challengeService.addChallenge(c);
                        log.debug("Added challenge for routine {}", r.getName());
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void addMonthlyChallenges() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = LocalDate.now();
        List<Routine> routines = routineService.getAllRoutines();
        for (Routine r : routines) {
            if ("挑戦".equals(r.getType()) && "毎月".equals(r.getFrequency())
                    && r.getTiming() != null && r.getStartDate() != null) {
                LocalTime timing = r.getTiming().truncatedTo(ChronoUnit.MINUTES);
                LocalDate start = r.getStartDate();
                long months = ChronoUnit.MONTHS.between(start.withDayOfMonth(1), today.withDayOfMonth(1));
                if (months >= 0 && today.getDayOfMonth() == start.getDayOfMonth() && now.equals(timing)) {
                    boolean exists = challengeService.getAllChallenges().stream()
                            .anyMatch(c -> c.getTitle().equals(r.getName()) && c.getChallengeDate() == null);
                    if (!exists) {
                        Challenge c = new Challenge();
                        c.setTitle(r.getName());
                        c.setChallengeLevel(1);
                        challengeService.addChallenge(c);
                        log.debug("Added challenge for routine {}", r.getName());
                    }
                }
            }
        }
    }

    private String getJapaneseDayOfWeek(DayOfWeek day) {
        return day.getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
    }
}
