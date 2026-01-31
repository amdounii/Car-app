package com.car.car_app.repositories;

import com.car.car_app.entities.Favoris;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavorisRepository extends JpaRepository<Favoris, Long> {

    // ✅ Tous les favoris d’un utilisateur
    List<Favoris> findByUtilisateurId(Long utilisateurId);

    // ✅ Tous les favoris liés à une voiture (optionnel)
    List<Favoris> findByVoitureId(Long voitureId);

    // ✅ Trouver un favori précis (utile pour éviter doublon)
    Optional<Favoris> findByUtilisateurIdAndVoitureId(Long utilisateurId, Long voitureId);

    // ✅ Vérifier si déjà en favoris
    boolean existsByUtilisateurIdAndVoitureId(Long utilisateurId, Long voitureId);

    // ✅ Supprimer un favori (toggle favoris)
    void deleteByUtilisateurIdAndVoitureId(Long utilisateurId, Long voitureId);
}
