package com.car.car_app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "avis_utilisateur",
uniqueConstraints = @UniqueConstraint(columnNames = {"voiture_id", "acheteur_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvisUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String commentaire;
    
    @Min(1)
    @Max(5)
    private int note;

    @Column(name = "voiture_id", nullable = false)
    private Long voitureId;

    @Column(name = "acheteur_id", nullable = false)
    private Long acheteurId;

}