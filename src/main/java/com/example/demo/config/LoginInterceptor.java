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
        if (loginUser == null) {
            response.sendRedirect("/log-in");
            return false;
        }
        String[] parts = request.getRequestURI().split("/");
        if (parts.length > 1) {
            String usernameFromPath = parts[1];
            if (!loginUser.equals(usernameFromPath)) {
                response.sendRedirect("/log-in");
                return false;
            }
        }
        return true;
    }
}
