package com.car.car_app.controllers;

import com.car.car_app.entities.Voiture;
import com.car.car_app.services.VoitureService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voitures")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class VoitureController {

    private final VoitureService voitureService;

    public VoitureController(VoitureService voitureService) {
        this.voitureService = voitureService;
    }
    @PostMapping
public ResponseEntity<?> ajouter(@Valid @RequestBody Voiture voiture) {
    try {
        return ResponseEntity.ok(voitureService.ajouterVoiture(voiture));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

@PutMapping("/{id}")
public ResponseEntity<?> modifier(@PathVariable Long id, @Valid @RequestBody Voiture voiture) {
    try {
        return ResponseEntity.ok(voitureService.modifierVoiture(id, voiture));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @GetMapping
    public ResponseEntity<List<Voiture>> getAll() {
        return ResponseEntity.ok(voitureService.getAllVoitures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voiture> getById(@PathVariable Long id) {
        return ResponseEntity.ok(voitureService.getVoitureById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        voitureService.supprimerVoiture(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vendeur/{vendeurId}")
    public ResponseEntity<List<Voiture>> getByVendeur(@PathVariable Long vendeurId) {
        return ResponseEntity.ok(voitureService.getVoituresByVendeur(vendeurId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Voiture>> getByStatut(@PathVariable Voiture.StatutVoiture statut) {
        return ResponseEntity.ok(voitureService.getVoituresByStatut(statut));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Voiture>> search(
            @RequestParam(required = false) String marque,
            @RequestParam(required = false) String modele
    ) {
        return ResponseEntity.ok(voitureService.rechercher(marque, modele));
    }
}
