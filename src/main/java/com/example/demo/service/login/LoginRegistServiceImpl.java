package com.example.demo.service.login;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.demo.entity.User;
import com.example.demo.repository.login.LoginRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginRegistServiceImpl implements LoginRegistService {
	
        private final LoginRepository repository;

        @Override
        public void userRegist(User user) {
                log.debug("Registering user {}", user.getUsername());
                user.setPassword(hashPassword(user.getPassword()));
                repository.add(user);
        }

        @Override
        public boolean login(User user) {
                log.debug("Logging in user {}", user.getUsername());
                String hashed = hashPassword(user.getPassword());
                User found = repository.findByUsernameAndPassword(user.getUsername(), hashed);
                boolean success = found != null;
                log.debug("Login {} for user {}", success ? "succeeded" : "failed", user.getUsername());
                return success;
        }

        private String hashPassword(String password) {
                try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        byte[] hashed = md.digest(password.getBytes(StandardCharsets.UTF_8));
                        StringBuilder sb = new StringBuilder();
                        for (byte b : hashed) {
                                sb.append(String.format("%02x", b));
                        }
                        return sb.toString();
                } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException("Could not hash password", e);
                }
        }

}
