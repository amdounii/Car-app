package com.car.car_app.repositories;

import com.car.car_app.entities.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoitureRepository extends JpaRepository<Voiture, Long> {

    // 1) Récupérer les voitures d’un vendeur
    List<Voiture> findByVendeurId(Long vendeurId);

    // 2) Filtrer par statut (DISPONIBLE / VENDU)
    List<Voiture> findByStatut(Voiture.StatutVoiture statut);

    // 3) Recherche par marque (ex: "audi")
    List<Voiture> findByMarqueContainingIgnoreCase(String marque);

    // 4) Recherche par modèle (ex: "a3")
    List<Voiture> findByModeleContainingIgnoreCase(String modele);

    // 5) Recherche marque + modèle
    List<Voiture> findByMarqueContainingIgnoreCaseAndModeleContainingIgnoreCase(String marque, String modele);
}
