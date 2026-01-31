package com.car.car_app.entities;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nom;
    private String email;
    private String telephone;
    private String motDePasse;
    private String role; // "VENDEUR" ou "ACHETEUR"
}
