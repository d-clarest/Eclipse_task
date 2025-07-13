package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.DreamPage;
import com.example.demo.entity.DreamRecord;
import com.example.demo.service.page.DreamPageService;
import com.example.demo.service.dream.DreamRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DreamPageController {

    private final DreamPageService service;
    private final DreamRecordService recordService;

    @GetMapping("/{username}/task-top/dream-page/{dreamId}")
    public String showDreamPage(@PathVariable String username, @PathVariable int dreamId,
            Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying dream page for dream {}", dreamId);
        DreamPage page = service.getOrCreatePage(dreamId);
        DreamRecord dream = recordService.getAllRecords().stream()
                .filter(r -> r.getId() == dreamId)
                .findFirst()
                .orElse(null);
        model.addAttribute("page", page);
        model.addAttribute("dream", dream);
        model.addAttribute("username", username);
        return "dream-page";
    }

    @PostMapping("/dream-page-update")
    public ResponseEntity<Void> updateDreamPage(@RequestBody DreamPage page) {
        log.debug("Updating dream page id {}", page.getId());
        service.updatePage(page);
        return ResponseEntity.ok().build();
    }
}
