package com.car.car_app.entities;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String motDePasse;
    private String role; 
}