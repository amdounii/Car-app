package com.car.car_app.controllers;

import com.car.car_app.entities.EstimationSysteme;
import com.car.car_app.services.EstimationSystemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estimations")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class EstimationSystemeController {

    private final EstimationSystemeService estimationService;

    public EstimationSystemeController(EstimationSystemeService estimationService) {
        this.estimationService = estimationService;
    }

    @PostMapping("/voiture/{voitureId}/generer")
    public ResponseEntity<EstimationSysteme> generer(@PathVariable Long voitureId) {
        return ResponseEntity.ok(estimationService.genererEstimationPourVoiture(voitureId));
    }

    @GetMapping("/voiture/{id}")
    public ResponseEntity<EstimationSysteme> getByVoiture(@PathVariable Long id) {
        return ResponseEntity.ok(estimationService.getEstimationByVoitureId(id));
    }
}
