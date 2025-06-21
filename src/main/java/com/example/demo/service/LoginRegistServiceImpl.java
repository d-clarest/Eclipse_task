package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.LoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginRegistServiceImpl implements LoginRegistService {
	
	private final LoginRepository repository;

	@Override
	public void userRegist(User user) {
		repository.add(user);
	}

}
