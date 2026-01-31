package com.car.car_app.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "favoris",
    uniqueConstraints = @UniqueConstraint(columnNames = {"utilisateur_id", "voiture_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favoris {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "utilisateur_id", nullable = false)
    private Long utilisateurId;

    @Column(name = "voiture_id", nullable = false)
    private Long voitureId;

    @Column(name = "date_ajout", updatable = false)
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        if (this.dateAjout == null) {
            this.dateAjout = LocalDateTime.now();
        }
    }
}
