package com.car.car_app.services;

import com.car.car_app.entities.Utilisateur;
import com.car.car_app.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    // =========================
    // CREATE (inscription simple)
    // =========================
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {

        validateUtilisateur(utilisateur);

        // email unique
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        // normaliser email
        utilisateur.setEmail(utilisateur.getEmail().trim().toLowerCase());

        return utilisateurRepository.save(utilisateur);
    }

    // =========================
    // READ
    // =========================
    @Transactional(readOnly = true)
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable id=" + id));
    }

    @Transactional(readOnly = true)
    public Utilisateur getUtilisateurByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email obligatoire");
        }

        return utilisateurRepository
                .findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable email=" + email));
    }

    // =========================
    // UPDATE (profil simple)
    // =========================
    public Utilisateur modifierUtilisateur(Long id, Utilisateur data) {

        if (data == null) {
            throw new IllegalArgumentException("Données utilisateur null");
        }

        Utilisateur existing = getUtilisateurById(id);

        existing.setNom(data.getNom());
        existing.setTelephone(data.getTelephone());
        existing.setRole(data.getRole());

        validateUtilisateur(existing);

        return utilisateurRepository.save(existing);
    }

    // =========================
    // DELETE
    // =========================
    public void supprimerUtilisateur(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id est obligatoire");
        }
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Suppression impossible, utilisateur introuvable id=" + id);
        }
        utilisateurRepository.deleteById(id);
    }

    // =========================
    // VALIDATION MÉTIER
    // =========================
    private void validateUtilisateur(Utilisateur u) {

        if (u == null) {
            throw new IllegalArgumentException("Utilisateur null");
        }

        if (u.getNom() == null || u.getNom().isBlank()) {
            throw new IllegalArgumentException("Nom obligatoire");
        }

        if (u.getEmail() == null || u.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email obligatoire");
        }

        if (u.getMotDePasse() == null || u.getMotDePasse().isBlank()) {
            throw new IllegalArgumentException("Mot de passe obligatoire");
        }

        if (u.getRole() == null) {
            throw new IllegalArgumentException("Rôle obligatoire");
        }
    }
}
