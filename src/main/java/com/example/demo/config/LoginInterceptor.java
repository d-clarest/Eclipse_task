package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String loginUser = session != null ? (String) session.getAttribute("loginUser") : null;

        String path = request.getRequestURI();
        // Extract the username from the path: e.g. /{username}/...
        String[] parts = path.split("/");
        String username = parts.length > 1 ? parts[1] : null;

        if (loginUser != null && loginUser.equals(username)) {
            return true;
        }
        response.sendRedirect("/log-in");
        return false;
    }
}
