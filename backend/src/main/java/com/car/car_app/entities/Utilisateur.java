package com.car.car_app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse;

    @Column(length = 20)
    private String telephone;


    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RoleUtilisateur role;


    public enum RoleUtilisateur {
        VENDEUR, ACHETEUR,ADMIN
        }
}