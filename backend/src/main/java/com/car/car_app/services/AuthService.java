package com.car.car_app.services;

import com.car.car_app.entities.LoginRequest;
import com.car.car_app.entities.LoginResponse;
import com.car.car_app.entities.Utilisateur;
import com.car.car_app.repositories.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;

    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    // ✅ INSCRIPTION
    public Utilisateur register(String nom, String email, String motDePasse, String telephone, String role) {

        // validations simples
        if (isBlank(nom)) throw new IllegalArgumentException("Le nom est obligatoire.");
        if (isBlank(email)) throw new IllegalArgumentException("L'email est obligatoire.");
        if (isBlank(motDePasse)) throw new IllegalArgumentException("Le mot de passe est obligatoire.");
        if (isBlank(telephone)) throw new IllegalArgumentException("Le téléphone est obligatoire.");
        if (isBlank(role)) throw new IllegalArgumentException("Le rôle est obligatoire.");

        String emailNorm = email.trim().toLowerCase();

        // email unique
        if (utilisateurRepository.existsByEmail(emailNorm)) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        // role
        Utilisateur.RoleUtilisateur roleEnum;
        try {
            roleEnum = Utilisateur.RoleUtilisateur.valueOf(role.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Rôle invalide. Choisis: VENDEUR / ACHETEUR / ADMIN.");
        }

        Utilisateur u = Utilisateur.builder()
                .nom(nom.trim())
                .email(emailNorm)
                .motDePasse(motDePasse) // ✅ direct (sans hash)
                .telephone(telephone.trim())
                .role(roleEnum)
                .build();

        return utilisateurRepository.save(u);
    }

    // ✅ LOGIN (ton code mais + validations + normalisation)
    public LoginResponse login(LoginRequest request, HttpSession session) {

        if (request == null) throw new IllegalArgumentException("Données manquantes.");
        if (isBlank(request.getEmail())) throw new IllegalArgumentException("Email obligatoire.");
        if (isBlank(request.getMotDePasse())) throw new IllegalArgumentException("Mot de passe obligatoire.");
        if (isBlank(request.getRole())) throw new IllegalArgumentException("Rôle obligatoire.");

        String emailNorm = request.getEmail().trim().toLowerCase();

        Utilisateur user = utilisateurRepository.findByEmail(emailNorm)
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect."));

        // ✅ comparaison directe
        if (!request.getMotDePasse().equals(user.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect.");
        }

        // ✅ vérifier rôle choisi
        String roleDB = user.getRole().name();           // VENDEUR / ACHETEUR / ADMIN
        String roleChosen = request.getRole().trim().toUpperCase();

        if (!roleDB.equals(roleChosen)) {
            throw new RuntimeException("Rôle incorrect pour cet utilisateur.");
        }

        // ✅ session
        session.setAttribute("userId", user.getId());
        session.setAttribute("role", roleDB);

        return new LoginResponse(user.getId(), roleDB);
    }

    // ✅ helper
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
