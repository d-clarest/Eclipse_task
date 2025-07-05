package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

import com.example.demo.entity.User;
import com.example.demo.form.LogInForm;
import com.example.demo.form.NewRegistForm;
import com.example.demo.service.login.LoginRegistService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

        private final LoginRegistService loginService;
	
        @GetMapping("/log-in")
        public String showLogInForm() {
                log.debug("Displaying login form");
                return "log-in";
        }
	
        @GetMapping("/new-regist")
        public String newRsgistForm() {
                log.debug("Displaying registration form");
                return "new-regist";
        }
	
    @PostMapping("/log-in")
    public String reShowLogInForm(NewRegistForm form) {
            log.debug("Registering new user: {}", form.getNewUsername());
            User u=new User();
            u.setUsername(form.getNewUsername());
            u.setPassword(form.getNewPassword());
            loginService.userRegist(u);
            log.debug("Registration successful, returning to login form");
            return "log-in";
    }
	
    @PostMapping("/task-top")
    public String showTop(LogInForm form, Model model, HttpSession session) {
        User u = new User();
        u.setUsername(form.getUsername());
        u.setPassword(form.getPassword());
        log.debug("Attempting login for user: {}", form.getUsername());
        if (loginService.login(u)) {
            log.debug("Login success for user: {}", form.getUsername());
            session.setAttribute("loginUser", form.getUsername());
            return "redirect:/" + form.getUsername() + "/task-top";
        } else {
            log.debug("Login failed for user: {}", form.getUsername());
            model.addAttribute("errorMessage", "ログイン失敗");
            return "log-in";
        }
    }
}

