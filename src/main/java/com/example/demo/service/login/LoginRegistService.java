package com.example.demo.service.login;

import com.example.demo.entity.User;

public interface LoginRegistService {
        void userRegist(User user);

        boolean login(User user);

}
