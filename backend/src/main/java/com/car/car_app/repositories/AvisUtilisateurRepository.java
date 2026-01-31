package com.car.car_app.repositories;

import com.car.car_app.entities.AvisUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvisUtilisateurRepository extends JpaRepository<AvisUtilisateur, Long> {

    // ✅ Tous les avis d’une voiture
    List<AvisUtilisateur> findByVoitureId(Long voitureId);

    // ✅ Tous les avis écrits par un utilisateur
    List<AvisUtilisateur> findByAcheteurId(Long acheteurId);

    // ✅ Un avis précis (1 utilisateur → 1 avis par voiture)
    Optional<AvisUtilisateur> findByVoitureIdAndAcheteurId(Long voitureId, Long acheteurId);

    // ✅ Vérifier si l’utilisateur a déjà donné un avis
    boolean existsByVoitureIdAndAcheteurId(Long voitureId, Long acheteurId);
}
