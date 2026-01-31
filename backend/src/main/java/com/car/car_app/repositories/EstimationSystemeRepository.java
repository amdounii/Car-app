package com.car.car_app.repositories;

import com.car.car_app.entities.EstimationSysteme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstimationSystemeRepository extends JpaRepository<EstimationSysteme, Long> {

    Optional<EstimationSysteme> findByVoitureId(Long voitureId);

   
    boolean existsByVoitureId(Long voitureId);

    // ✅ Supprimer l’estimation liée à une voiture (si besoin)
    void deleteByVoitureId(Long voitureId);
}
