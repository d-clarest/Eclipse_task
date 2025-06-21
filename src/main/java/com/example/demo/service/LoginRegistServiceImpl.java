package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.demo.entity.User;
import com.example.demo.repository.LoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginRegistServiceImpl implements LoginRegistService {
	
        private final LoginRepository repository;

        @Override
        public void userRegist(User user) {
                user.setPassword(hashPassword(user.getPassword()));
                repository.add(user);
        }

        @Override
        public boolean login(User user) {
                String hashed = hashPassword(user.getPassword());
                User found = repository.findByUsernameAndPassword(user.getUsername(), hashed);
                return found != null;
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
