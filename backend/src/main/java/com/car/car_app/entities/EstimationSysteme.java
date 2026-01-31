package com.car.car_app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
  name = "estimation_systeme",
  uniqueConstraints = @UniqueConstraint(columnNames = {"voiture_id"})
)

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstimationSysteme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prix_estime")
    private Double prixEstime;

    @Column(name = "avis_auto", length = 100)
    private String avisAuto;

    @Column(name = "voiture_id")
    private Long voitureId;
}