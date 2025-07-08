package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entity.AwarenessPage;
import com.example.demo.entity.AwarenessRecord;
import com.example.demo.service.page.AwarenessPageService;
import com.example.demo.service.awareness.AwarenessRecordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AwarenessPageController {

    private final AwarenessPageService service;
    private final AwarenessRecordService recordService;

    @GetMapping("/{username}/task-top/awareness-page/{recordId}")
    public String showAwarenessPage(@PathVariable String username, @PathVariable int recordId,
            Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying awareness page for record {}", recordId);
        AwarenessPage page = service.getOrCreatePage(recordId);
        AwarenessRecord record = recordService.getAllRecords().stream()
                .filter(r -> r.getId() == recordId)
                .findFirst()
                .orElse(null);
        model.addAttribute("page", page);
        model.addAttribute("record", record);
        model.addAttribute("username", username);
        return "awareness-page";
    }

    @PostMapping("/awareness-page-update")
    public ResponseEntity<Void> updateAwarenessPage(@RequestBody AwarenessPage page) {
        log.debug("Updating awareness page id {}", page.getId());
        service.updatePage(page);
        return ResponseEntity.ok().build();
    }
}
