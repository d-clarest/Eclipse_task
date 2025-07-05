package com.example.demo.repository.login;

import com.example.demo.entity.User;

public interface LoginRepository {
        void add(User user);

        User findByUsernameAndPassword(String username, String password);
}
