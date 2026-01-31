package com.car.car_app.entities;


import com.car.car_app.converters.StringListConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "voiture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voiture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String marque;

    @Column(nullable = false, length = 50)
    private String modele;
    
    @Min(1900)
    @Max(2100)
    private Integer annee;

    @Min(0)
    private Integer kilometrage;

    @Column(name = "prix_vendeur")
    private Double prixVendeur;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatutVoiture statut;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    @Size(max = 10, message = "Maximum 10 images autorisées")
    private List<String> images;

    @Column(name = "vendeur_id")
    private Long vendeurId;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }

    public enum StatutVoiture {
        DISPONIBLE,VENDUE
    }
}
