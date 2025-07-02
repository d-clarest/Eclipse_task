package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.ScheduleService;
import com.example.demo.service.challenge.ChallengeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
	
        private final ScheduleService scheduleService;
        private final ChallengeService challengeService;
	@GetMapping("/total-point")
    @ResponseBody
    public Integer getTotalPoint() {
       log.debug("Fetching total completed points");
        int schedulePoints = scheduleService.getTotalCompletedPoints();
        int challengePoints = challengeService.getTotalCompletedPoints();
        return schedulePoints + challengePoints;//@ResponseBodyによって，htmlではなく，interger型のデータ（ポイント）をreturn
    }

}
