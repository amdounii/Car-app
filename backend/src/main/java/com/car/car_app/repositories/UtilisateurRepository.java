package com.car.car_app.repositories;

import com.car.car_app.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // 1️⃣ Trouver un utilisateur par email (login)
    Optional<Utilisateur> findByEmail(String email);

    // 2️⃣ Vérifier si un email existe déjà (inscription)
    boolean existsByEmail(String email);
}
