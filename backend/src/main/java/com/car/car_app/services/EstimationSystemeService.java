package com.car.car_app.services;

import com.car.car_app.entities.EstimationSysteme;
import com.car.car_app.entities.Voiture;
import com.car.car_app.repositories.EstimationSystemeRepository;
import com.car.car_app.repositories.VoitureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@Transactional
public class EstimationSystemeService {

    private final EstimationSystemeRepository estimationRepository;
    private final VoitureRepository voitureRepository;

    // ✅ marge max autorisée (ex: +20%)
    private static final double MAX_RATIO = 1.20;

    public EstimationSystemeService(EstimationSystemeRepository estimationRepository,
                                    VoitureRepository voitureRepository) {
        this.estimationRepository = estimationRepository;
        this.voitureRepository = voitureRepository;
    }

    // ✅ Refuser prix trop élevé (utilisé en create + update)
    public void verifierPrixVendeur(Voiture v) {
        if (v == null) throw new IllegalArgumentException("Voiture null");
        if (v.getPrixVendeur() == null || v.getPrixVendeur() <= 0) return;

        double prixEstime = calculerPrixEstime(v);
        double maxAutorise = prixEstime * MAX_RATIO;

        if (v.getPrixVendeur() > maxAutorise) {
            throw new IllegalArgumentException(
                    "Prix vendeur trop élevé. Estimation = " + prixEstime +
                    " | Max autorisé = " + (Math.round(maxAutorise * 100.0) / 100.0)
            );
        }
    }

    // ✅ Generate estimation (DB)
    public EstimationSysteme genererEstimationPourVoiture(Long voitureId) {
        if (voitureId == null) throw new IllegalArgumentException("voitureId est obligatoire");

        Voiture voiture = voitureRepository.findById(voitureId)
                .orElseThrow(() -> new IllegalArgumentException("Voiture introuvable id=" + voitureId));

        double prixEstime = calculerPrixEstime(voiture);
        String avisAuto = genererAvisAuto(voiture, prixEstime);

        return estimationRepository.findByVoitureId(voitureId)
                .map(existing -> {
                    existing.setPrixEstime(prixEstime);
                    existing.setAvisAuto(avisAuto);
                    return estimationRepository.save(existing);
                })
                .orElseGet(() -> estimationRepository.save(
                        EstimationSysteme.builder()
                                .voitureId(voitureId)
                                .prixEstime(prixEstime)
                                .avisAuto(avisAuto)
                                .build()
                ));
    }

    // ✅ IMPORTANT : PUBLIC (et ne dépend PAS du prix vendeur)
    public double calculerPrixEstime(Voiture v) {
        double base = 30000.0; // base fixe

        int currentYear = Year.now().getValue();
        int annee = (v.getAnnee() != null) ? v.getAnnee() : currentYear;
        int age = Math.max(0, currentYear - annee);

        double decoteAge = Math.min(0.60, age * 0.05);
        double prixApresAge = base * (1 - decoteAge);

        int km = (v.getKilometrage() != null) ? v.getKilometrage() : 0;
        double decoteKm = Math.min(0.50, (km / 10000.0) * 0.02);
        double prixApresKm = prixApresAge * (1 - decoteKm);

        double facteurMarque = facteurMarque(v.getMarque());

        double finalPrice = prixApresKm * facteurMarque;

        finalPrice = Math.max(finalPrice, 2000.0);
        return Math.round(finalPrice * 100.0) / 100.0;
    }

    private double facteurMarque(String marque) {
        if (marque == null) return 1.0;

        String m = marque.trim().toLowerCase();
        if (m.contains("bmw") || m.contains("mercedes") || m.contains("audi")) return 1.08;
        if (m.contains("toyota") || m.contains("honda")) return 1.05;
        if (m.contains("kia") || m.contains("hyundai")) return 1.02;
        if (m.contains("dacia") || m.contains("fiat")) return 0.95;
        return 1.00;
    }

    private String genererAvisAuto(Voiture v, double prixEstime) {
        int currentYear = Year.now().getValue();
        int annee = (v.getAnnee() != null) ? v.getAnnee() : currentYear;
        int age = Math.max(0, currentYear - annee);

        int km = (v.getKilometrage() != null) ? v.getKilometrage() : 0;

        String etat;
        if (age <= 5 && km < 80000) etat = "Très bon état estimé";
        else if (age <= 10 && km < 160000) etat = "État moyen estimé";
        else etat = "État usé estimé";

        return etat + " | Prix estimé: " + prixEstime;
    }

    @Transactional(readOnly = true)
    public EstimationSysteme getEstimationByVoitureId(Long voitureId) {
        if (voitureId == null) throw new IllegalArgumentException("voitureId est obligatoire");

        return estimationRepository.findByVoitureId(voitureId)
                .orElseThrow(() -> new IllegalArgumentException("Aucune estimation trouvée pour voitureId=" + voitureId));
    }
}
