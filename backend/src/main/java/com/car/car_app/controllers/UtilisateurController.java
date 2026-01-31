package com.car.car_app.controllers;

import com.car.car_app.entities.Utilisateur;
import com.car.car_app.services.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(
  origins = "http://localhost:4200",
  allowCredentials = "true"
)
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public ResponseEntity<Utilisateur> ajouter(@Valid @RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.ajouterUtilisateur(utilisateur));
    }

    // =========================
    // READ
    // =========================
    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAll() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getById(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getUtilisateurById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Utilisateur> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(utilisateurService.getUtilisateurByEmail(email));
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> modifier(@PathVariable Long id,
                                               @Valid @RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.modifierUtilisateur(id, utilisateur));
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
    
    // =========================
// PUBLIC (vendeur visible pour consulter voiture)
// =========================
@GetMapping("/public/{id}")
public ResponseEntity<?> getPublicUtilisateur(@PathVariable Long id) {

    Utilisateur u = utilisateurService.getUtilisateurById(id);
    u.setMotDePasse(null);
    return ResponseEntity.ok(u);
}

}
