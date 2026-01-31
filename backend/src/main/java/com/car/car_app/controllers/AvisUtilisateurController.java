package com.car.car_app.controllers;

import com.car.car_app.entities.AvisUtilisateur;
import com.car.car_app.services.AvisUtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avis")
@CrossOrigin(
  origins = "http://localhost:4200",
  allowCredentials = "true"
)
public class AvisUtilisateurController {

    private final AvisUtilisateurService avisService;

    public AvisUtilisateurController(AvisUtilisateurService avisService) {
        this.avisService = avisService;
    }

    // ----------------------------
    // CREATE
    // ----------------------------
    @PostMapping
    public ResponseEntity<AvisUtilisateur> ajouter(@Valid @RequestBody AvisUtilisateur avis) {
        return ResponseEntity.ok(avisService.ajouterAvis(avis));
    }

    // ----------------------------
    // READ
    // ----------------------------

    // Tous les avis d’une voiture
    // GET /api/avis/voiture/5
    @GetMapping("/voiture/{voitureId}")
    public ResponseEntity<List<AvisUtilisateur>> getByVoiture(@PathVariable Long voitureId) {
        return ResponseEntity.ok(avisService.getAvisByVoiture(voitureId));
    }

    // Tous les avis d’un utilisateur
    // GET /api/avis/acheteur/3
    @GetMapping("/acheteur/{acheteurId}")
    public ResponseEntity<List<AvisUtilisateur>> getByAcheteur(@PathVariable Long acheteurId) {
        return ResponseEntity.ok(avisService.getAvisByAcheteur(acheteurId));
    }

    // Avis précis (voiture + acheteur)
    // GET /api/avis?voitureId=5&acheteurId=3
    @GetMapping
    public ResponseEntity<AvisUtilisateur> getOne(
            @RequestParam Long voitureId,
            @RequestParam Long acheteurId) {
        return ResponseEntity.ok(avisService.getAvis(voitureId, acheteurId));
    }

    // ----------------------------
    // UPDATE
    // ----------------------------
    @PutMapping("/{id}")
    public ResponseEntity<AvisUtilisateur> modifier(
            @PathVariable Long id,
            @Valid @RequestBody AvisUtilisateur avis) {
        return ResponseEntity.ok(avisService.modifierAvis(id, avis));
    }

    // ----------------------------
    // DELETE
    // ----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        avisService.supprimerAvis(id);
        return ResponseEntity.noContent().build();
    }
}
