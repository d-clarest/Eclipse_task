package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Schedule;
import com.example.demo.service.challenge.ChallengeService;
import com.example.demo.service.schedule.ScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TopController {

    private final ScheduleService scheduleService;
    private final ChallengeService challengeService;

    @GetMapping("/{username}/task-top")
    public String showTaskTop(@PathVariable String username, Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying task top page");
        var list = scheduleService.getAllSchedules().stream()
                .filter(s -> s.getCompletedDay() == null)
                .toList();
        model.addAttribute("schedules", list);
        var challengeList = challengeService.getAllChallenges().stream()
                .filter(c -> c.getChallengeDate() == null)
                .toList();
        model.addAttribute("challenges", challengeList);
        model.addAttribute("username", username);
        return "task-top";
    }
    //--------------------------------------------------------------------------------------------------
    @GetMapping("/{username}/task-top/schedule-box")
    public String showTaskBox(@PathVariable String username, Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying task box page");
        var all = scheduleService.getAllSchedules();
        var completed = all.stream()
                .filter(s -> s.getCompletedDay() != null)
                .toList();
        var upcoming = all.stream()
                .filter(s -> s.getCompletedDay() == null)
                .toList();
        model.addAttribute("completedSchedules", completed);
        model.addAttribute("upcomingSchedules", upcoming);
        model.addAttribute("username", username);
        return "schedule-box";
    }

    @GetMapping("/{username}/task-top/challenge-box")
    public String showChallengeBox(@PathVariable String username, Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying challenge box page");
        var all = challengeService.getAllChallenges();
        var unchallenged = all.stream()
                .filter(c -> c.getChallengeDate() == null)
                .toList();
        var completed = all.stream()
                .filter(c -> c.getChallengeDate() != null)
                .toList();
        model.addAttribute("unchallenged", unchallenged);
        model.addAttribute("completedChallenges", completed);
        model.addAttribute("username", username);
        return "challenge-box";
    }
    //-------------------------------------------------------------------------------------------------
   @GetMapping("/total-point")
   @ResponseBody
   public Integer getTotalPoint() {
       log.debug("Fetching total completed points");
       return scheduleService.getTotalCompletedPoints();//@ResponseBodyによって，htmlではなく，interger型のデータ（ポイント）をreturn
   }
}
