package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
	
	private final ScheduleService scheduleService;
	@GetMapping("/total-point")
    @ResponseBody
    public Integer getTotalPoint() {
       log.debug("Fetching total completed points");
        return scheduleService.getTotalCompletedPoints();//@ResponseBodyによって，htmlではなく，interger型のデータ（ポイント）をreturn
    }

}
