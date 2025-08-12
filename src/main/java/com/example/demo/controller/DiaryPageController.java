package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.DiaryRecord;
import com.example.demo.service.diary.DiaryRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DiaryPageController {

    private final DiaryRecordService recordService;

    @GetMapping("/{username}/task-top/diary-page/{recordId}")
    public String showDiaryPage(@PathVariable String username, @PathVariable int recordId,
            Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying diary page for record {}", recordId);
        DiaryRecord record = recordService.getAllRecords().stream()
                .filter(r -> r.getId() == recordId)
                .findFirst()
                .orElse(null);
        model.addAttribute("record", record);
        model.addAttribute("username", username);
        return "diary-page";
    }
}
