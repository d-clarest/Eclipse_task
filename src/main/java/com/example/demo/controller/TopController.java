package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.awareness.AwarenessRecordService;
import com.example.demo.service.challenge.ChallengeService;
import com.example.demo.service.schedule.ScheduleService;
import com.example.demo.service.task.TaskService;
import com.example.demo.service.subtask.SubTaskService;
import com.example.demo.service.word.WordRecordService;
import com.example.demo.service.dream.DreamRecordService;
import com.example.demo.service.routine.RoutineService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TopController {

    private final ScheduleService scheduleService;
    private final ChallengeService challengeService;
    private final TaskService taskService;
    private final SubTaskService subTaskService;
    private final AwarenessRecordService awarenessRecordService;
    private final WordRecordService wordRecordService;
    private final DreamRecordService dreamRecordService;
    private final RoutineService routineService;

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
        System.out.println(list);
        model.addAttribute("schedules", list);
        var challengeList = challengeService.getAllChallenges().stream()
                .filter(c -> c.getChallengeDate() == null)
                .toList();
        model.addAttribute("challenges", challengeList);
        var taskList = taskService.getAllTasks().stream()
                        .filter(t -> !(t.getCompletedAt() != null
                                && ("100%".equals(t.getProgressRate())
                                        || t.getProgressRate() == null)))
                .toList();
        model.addAttribute("tasks", taskList);
        var awarenessList = awarenessRecordService.getAllRecords()
                .stream()
                .limit(5)
                .toList();
        model.addAttribute("awarenessRecords", awarenessList);
        var wordList = wordRecordService.getAllRecords()
                .stream()
                .limit(5)
                .toList();
        model.addAttribute("wordRecords", wordList);
        var dreamList = dreamRecordService.getAllRecords()
                .stream()
                .filter(d -> d.getCompletedAt() == null)
                .limit(5)
                .toList();
        model.addAttribute("dreamRecords", dreamList);
        var routineList = routineService.getAllRoutines();
        model.addAttribute("routines", routineList);
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

    @GetMapping("/{username}/task-top/task-box")
    public String showTaskBoxPage(@PathVariable String username, Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying task box page");
        var all = taskService.getAllTasks();
        var completedTasks = all.stream()
                        .filter(t -> t.getCompletedAt() != null)
                        .filter(t -> "100%".equals(t.getProgressRate()) || t.getProgressRate() == null)
                .toList();
        var uncompleted = all.stream()
                        .filter(t -> !(t.getCompletedAt() != null
                                && ("100%".equals(t.getProgressRate())
                                        || t.getProgressRate() == null)))
                .toList();
        model.addAttribute("uncompletedTasks", uncompleted);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("username", username);
        return "task-box";
    }

    @GetMapping("/{username}/task-top/awareness-box")
    public String showAwarenessBox(@PathVariable String username, Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying awareness box page");
        var records = awarenessRecordService.getAllRecords();
        model.addAttribute("awarenessRecords", records);
        model.addAttribute("username", username);
        return "awareness-box";
    }

    @GetMapping("/{username}/task-top/word-box")
    public String showWordBox(@PathVariable String username, Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying word box page");
        var records = wordRecordService.getAllRecords();
        model.addAttribute("wordRecords", records);
        model.addAttribute("username", username);
        return "word-box";
    }

    @GetMapping("/{username}/task-top/dream-box")
    public String showDreamBox(@PathVariable String username, Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying dream box page");
        var all = dreamRecordService.getAllRecords();
        var uncompleted = all.stream()
                .filter(d -> d.getCompletedAt() == null)
                .toList();
        var completed = all.stream()
                .filter(d -> d.getCompletedAt() != null)
                .toList();
        model.addAttribute("uncompletedDreamRecords", uncompleted);
        model.addAttribute("completedDreamRecords", completed);
        model.addAttribute("username", username);
        return "dream-box";
    }

    @GetMapping("/{username}/task-top/sub-task-box")
    public String showSubTaskBox(@PathVariable String username, Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying sub task box page");
        var all = subTaskService.getAllSubTasks();
        var uncompleted = all.stream()
                .filter(st -> st.getCompletedAt() == null)
                .sorted(java.util.Comparator.comparing(
                        com.example.demo.entity.SubTask::getDeadline,
                        java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder())))
                .toList();
        var completed = all.stream()
                .filter(st -> st.getCompletedAt() != null)
                .sorted(java.util.Comparator.comparing(
                        com.example.demo.entity.SubTask::getDeadline,
                        java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder())))
                .toList();
        model.addAttribute("uncompletedSubTasks", uncompleted);
        model.addAttribute("completedSubTasks", completed);
        model.addAttribute("username", username);
        return "sub-task-box";
    }
    //-------------------------------------------------------------------------------------------------
    @GetMapping("/total-point")
    @ResponseBody
    public Double getTotalPoint() {
        log.debug("Fetching total completed points");
        int schedulePoints = scheduleService.getTotalCompletedPoints();
        int challengePoints = challengeService.getTotalCompletedPoints();
        int taskPoints = taskService.getTotalCompletedLevels();
        int awareness = awarenessRecordService.getTotalAwarenessLevel();
        int wordCount = wordRecordService.countRecords();
        return schedulePoints + challengePoints + taskPoints + awareness * 0.5 + wordCount * 0.5;
    }
}
