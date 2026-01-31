package com.car.car_app.entities;

public class LoginResponse {
    private Long id;
    private String role;

    public LoginResponse() {}

    public LoginResponse(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}
