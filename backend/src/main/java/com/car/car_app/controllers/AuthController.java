package com.car.car_app.controllers;

import com.car.car_app.entities.LoginRequest;
import com.car.car_app.entities.LoginResponse;
import com.car.car_app.entities.RegisterRequest;
import com.car.car_app.entities.Utilisateur;
import com.car.car_app.repositories.UtilisateurRepository;
import com.car.car_app.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;
    private final UtilisateurRepository utilisateurRepository;

    public AuthController(AuthService authService, UtilisateurRepository utilisateurRepository) {
        this.authService = authService;
        this.utilisateurRepository = utilisateurRepository;
    }

    // ✅ INSCRIPTION
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest r) {
        try {
            return ResponseEntity.ok(authService.register(
                    r.getNom(), r.getEmail(), r.getMotDePasse(), r.getTelephone(), r.getRole()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            return ResponseEntity.ok(authService.login(request, session));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    // ✅ ME (id + role)
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object role = session.getAttribute("role");
        Object userId = session.getAttribute("userId");

        if (role == null || userId == null) {
            return ResponseEntity.status(401).body("Non connecté");
        }

        return ResponseEntity.ok(
                new LoginResponse(((Number) userId).longValue(), role.toString())
        );
    }

    // ✅ PROFILE (infos complètes de l'utilisateur connecté)
    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpSession session) {
        Object userId = session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body("Non connecté");
        }

        Utilisateur u = utilisateurRepository.findById(((Number) userId).longValue())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // ✅ pour ne pas exposer le mot de passe au front
        u.setMotDePasse(null);

        return ResponseEntity.ok(u);
    }
}
