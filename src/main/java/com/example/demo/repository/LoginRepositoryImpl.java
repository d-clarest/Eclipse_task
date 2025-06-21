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

        @Override
        public User findByUsernameAndPassword(String username, String password) {
            String sql = "SELECT username, password FROM users WHERE username = ? AND password = ?";
            return jdbcTemplate.query(sql, rs -> {
                if (rs.next()) {
                    User u = new User();
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    return u;
                }
                return null;
            }, username, password);
        }
}
