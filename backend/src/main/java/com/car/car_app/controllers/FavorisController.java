package com.car.car_app.controllers;

import com.car.car_app.entities.Favoris;
import com.car.car_app.services.FavorisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoris")
@CrossOrigin(
  origins = "http://localhost:4200",
  allowCredentials = "true"
)
public class FavorisController {

    private final FavorisService favorisService;

    public FavorisController(FavorisService favorisService) {
        this.favorisService = favorisService;
    }

    // ✅ CREATE (en envoyant l'objet Favoris)
    @PostMapping
    public ResponseEntity<Favoris> ajouter(@Valid @RequestBody Favoris favoris) {
        return ResponseEntity.ok(favorisService.ajouterFavori(favoris));
    }

    // ✅ CREATE (simple) : /api/favoris/add?utilisateurId=1&voitureId=10
    @PostMapping("/add")
    public ResponseEntity<Favoris> ajouterParIds(
            @RequestParam Long utilisateurId,
            @RequestParam Long voitureId
    ) {
        return ResponseEntity.ok(favorisService.ajouterFavori(utilisateurId, voitureId));
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<List<Favoris>> getAll() {
        return ResponseEntity.ok(favorisService.getAllFavoris());
    }

    // ✅ Favoris d’un utilisateur : /api/favoris/utilisateur/3
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<Favoris>> getByUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(favorisService.getFavorisByUtilisateur(utilisateurId));
    }

    // ✅ Vérifier si favori existe : /api/favoris/check?utilisateurId=1&voitureId=10
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFavori(
            @RequestParam Long utilisateurId,
            @RequestParam Long voitureId
    ) {
        return ResponseEntity.ok(favorisService.isFavori(utilisateurId, voitureId));
    }

    // ✅ DELETE par id : /api/favoris/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        favorisService.supprimerFavori(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ DELETE par ids : /api/favoris/remove?utilisateurId=1&voitureId=10
    @DeleteMapping("/remove")
    public ResponseEntity<Void> supprimerParIds(
            @RequestParam Long utilisateurId,
            @RequestParam Long voitureId
    ) {
        favorisService.supprimerFavori(utilisateurId, voitureId);
        return ResponseEntity.noContent().build();
    }

    // ✅ TOGGLE : /api/favoris/toggle?utilisateurId=1&voitureId=10
    @PostMapping("/toggle")
    public ResponseEntity<String> toggle(
            @RequestParam Long utilisateurId,
            @RequestParam Long voitureId
    ) {
        return ResponseEntity.ok(favorisService.toggleFavori(utilisateurId, voitureId));
    }
}
