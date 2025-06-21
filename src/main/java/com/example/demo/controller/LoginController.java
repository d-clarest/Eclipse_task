package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.form.NewRegistForm;
import com.example.demo.service.LoginRegistService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	
	private final LoginRegistService service;
	
	
	
	@GetMapping("/log-in")
	public String showLogInForm() {
		return "log-in";
	}
	
	@PostMapping("/new-regist")
	public String newRsgistForm() {
		return "new-regist";
	}
	
	@PostMapping("/log-in")
	public String reShowLogInForm(NewRegistForm form) {
		User u=new User();
		u.setUsername(form.getNewUsername());
		u.setPassword(form.getNewPassword());
		service.userRegist(u);
		return "log-in";
	}
	
	@PostMapping("/task-top")
	public String showTop() {
		return "task-top";
	}
}

