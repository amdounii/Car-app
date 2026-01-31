package com.car.car_app.services;

import com.car.car_app.entities.Voiture;
import com.car.car_app.repositories.VoitureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VoitureService {

    private final VoitureRepository voitureRepository;
    private final EstimationSystemeService estimationService;

    public VoitureService(VoitureRepository voitureRepository,
                          EstimationSystemeService estimationService) {
        this.voitureRepository = voitureRepository;
        this.estimationService = estimationService;
    }

    // =========================
    // CREATE
    // =========================
    public Voiture ajouterVoiture(Voiture voiture) {

        validateVoiture(voiture);

        if (voiture.getStatut() == null) {
            voiture.setStatut(Voiture.StatutVoiture.DISPONIBLE);
        }

        // ✅ Vérification estimation AVANT save (règles basées sur marque/année/km/prix)
        estimationService.verifierPrixVendeur(voiture);

        // save
        Voiture saved = voitureRepository.save(voiture);

        // ✅ générer/mettre à jour estimation après save (on a l'id)
        estimationService.genererEstimationPourVoiture(saved.getId());

        return saved;
    }

    // =========================
    // READ
    // =========================
    @Transactional(readOnly = true)
    public List<Voiture> getAllVoitures() {
        return voitureRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Voiture getVoitureById(Long id) {
        return voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture introuvable avec id=" + id));
    }

    // =========================
    // UPDATE
    // =========================
    public Voiture modifierVoiture(Long id, Voiture data) {

        if (data == null) {
            throw new IllegalArgumentException("Données voiture null");
        }

        Voiture existing = getVoitureById(id);

        // champs simples
        if (data.getMarque() != null) existing.setMarque(data.getMarque());
        if (data.getModele() != null) existing.setModele(data.getModele());

        existing.setAnnee(data.getAnnee());
        existing.setKilometrage(data.getKilometrage());
        existing.setPrixVendeur(data.getPrixVendeur());
        existing.setDescription(data.getDescription());

        // statut
        if (data.getStatut() != null) {
            existing.setStatut(data.getStatut());
        }

        // images
        if (data.getImages() != null) {
            existing.setImages(data.getImages());
        }

        // vendeurId
        if (data.getVendeurId() != null) {
            existing.setVendeurId(data.getVendeurId());
        }

        // validation
        validateVoiture(existing);

        if (existing.getStatut() == null) {
            existing.setStatut(Voiture.StatutVoiture.DISPONIBLE);
        }

        // ✅ Vérification estimation avant save
        estimationService.verifierPrixVendeur(existing);

        // save
        Voiture saved = voitureRepository.save(existing);

        // ✅ générer/mettre à jour estimation
        estimationService.genererEstimationPourVoiture(saved.getId());

        return saved;
    }

    // =========================
    // DELETE
    // =========================
    public void supprimerVoiture(Long id) {
        if (id == null) throw new IllegalArgumentException("id est obligatoire");
        if (!voitureRepository.existsById(id)) {
            throw new RuntimeException("Suppression impossible, voiture introuvable id=" + id);
        }
        voitureRepository.deleteById(id);
    }

    // =========================
    // FILTERS
    // =========================
    @Transactional(readOnly = true)
    public List<Voiture> getVoituresByVendeur(Long vendeurId) {
        if (vendeurId == null) throw new IllegalArgumentException("vendeurId est obligatoire");
        return voitureRepository.findByVendeurId(vendeurId);
    }

    @Transactional(readOnly = true)
    public List<Voiture> getVoituresByStatut(Voiture.StatutVoiture statut) {
        if (statut == null) throw new IllegalArgumentException("statut est obligatoire");
        return voitureRepository.findByStatut(statut);
    }

    @Transactional(readOnly = true)
    public List<Voiture> rechercher(String marque, String modele) {

        boolean hasMarque = (marque != null && !marque.isBlank());
        boolean hasModele = (modele != null && !modele.isBlank());

        if (hasMarque && hasModele) {
            return voitureRepository.findByMarqueContainingIgnoreCaseAndModeleContainingIgnoreCase(
                    marque.trim(), modele.trim()
            );
        }
        if (hasMarque) return voitureRepository.findByMarqueContainingIgnoreCase(marque.trim());
        if (hasModele) return voitureRepository.findByModeleContainingIgnoreCase(modele.trim());

        return voitureRepository.findAll();
    }

    // =========================
    // VALIDATION
    // =========================
    private void validateVoiture(Voiture voiture) {

        if (voiture == null) throw new IllegalArgumentException("La voiture est null");

        if (voiture.getMarque() == null || voiture.getMarque().isBlank()) {
            throw new IllegalArgumentException("La marque est obligatoire");
        }

        if (voiture.getModele() == null || voiture.getModele().isBlank()) {
            throw new IllegalArgumentException("Le modèle est obligatoire");
        }

        if (voiture.getPrixVendeur() != null && voiture.getPrixVendeur() <= 0) {
            throw new IllegalArgumentException("Le prix vendeur doit être > 0");
        }

        if (voiture.getImages() != null && voiture.getImages().size() > 10) {
            throw new IllegalArgumentException("Maximum 10 images autorisées");
        }
    }
}
