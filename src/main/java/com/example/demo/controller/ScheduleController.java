package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Schedule;
import com.example.demo.service.schedule.ScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
	
    private final ScheduleService scheduleService;
    
    @PostMapping("/schedule-complete")
    public ResponseEntity<Void> updateCompletedDay(@RequestBody Schedule schedule) {
       log.debug("Updating schedule completed day {} {}", schedule.getTitle(), schedule.getCompletedDay());
        scheduleService.updateCompletedDay(schedule);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/schedule-update")
    public ResponseEntity<Void> updateSchedule(@RequestBody Schedule schedule) {
        log.debug("Updating schedule {} on {}", schedule.getTitle(), schedule.getScheduleDate());
        scheduleService.updateSchedule(schedule);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-add")
    public ResponseEntity<Void> addSchedule(@RequestBody Schedule schedule) {
        log.debug("Adding schedule {} on {}", schedule.getTitle(), schedule.getScheduleDate());
        scheduleService.addSchedule(schedule);//repository.insertSchedule(schedule)
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-delete")
    public ResponseEntity<Void> deleteSchedule(@RequestBody Schedule schedule) {
        log.debug("Deleting schedule id {}", schedule.getId());
        scheduleService.deleteScheduleById(schedule.getId());//idで削除
        return ResponseEntity.ok().build();
    }

    @GetMapping("/completed-schedules")
    @ResponseBody
    //このアノテーションにより、メソッドの戻り値（ここでは List<Schedule>）が自動的にJSONなどのHTTPレスポンスボディに変換されて返す
    //@RequestParam でjavascriptからGETリクエストを受け取っている。 fetch(`/completed-schedules?year=${year}&month=${month}`)
    public java.util.List<Schedule> getCompletedSchedules(@RequestParam int year, @RequestParam int month) {
        log.debug("Fetching completed schedules for {}/{}", year, month);
        return scheduleService.getAllSchedules().stream()//全てのスケジュールをList<Schedule>で取得し、stream()することでfilterをつなげられる
                .filter(s -> s.getCompletedDay() != null)//完了日がnullでない、つまり完了済みのやつでフィルタ
                .filter(s -> s.getScheduleDate().getYear() == year && s.getScheduleDate().getMonthValue() == month)//現在表示しているカレンダーの年月と一致してるのでフィルタ
                .toList();//Stream<Schedule>の処理結果をList<Schedule>に変換
    }

}
