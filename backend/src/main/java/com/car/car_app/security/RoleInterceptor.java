package com.car.car_app.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

public class RoleInterceptor implements HandlerInterceptor {

    private final Set<String> allowedRoles;

    public RoleInterceptor(Set<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // ✅ IMPORTANT : autoriser le preflight CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        HttpSession session = request.getSession(false);

        // ✅ pas connecté
        if (session == null || session.getAttribute("role") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }

        String role = session.getAttribute("role").toString();

        // ✅ pas autorisé
        if (!allowedRoles.contains(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            return false;
        }

        return true; // ✅ ok
    }
}
