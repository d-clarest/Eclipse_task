package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.service.awareness.AwarenessTrashService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AwarenessTrashController {

    private final AwarenessTrashService trashService;

    @GetMapping("/{username}/task-top/awareness-trash")
    public String showTrash(@PathVariable String username, Model model, HttpSession session) {
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.equals(username)) {
            return "redirect:/log-in";
        }
        log.debug("Displaying awareness trash page");
        model.addAttribute("deletedRecords", trashService.getDeletedRecords());
        model.addAttribute("username", username);
        return "awareness-trash";
    }
}