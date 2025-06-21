package com.example.demo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoginRepositoryImpl implements LoginRepository {
	
	private final JdbcTemplate jdbcTemplate;

	@Override
	public void add(User user) {
	    String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
	    jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
	}
}
