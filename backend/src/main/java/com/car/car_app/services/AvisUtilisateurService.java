package com.car.car_app.services;

import com.car.car_app.entities.AvisUtilisateur;
import com.car.car_app.repositories.AvisUtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AvisUtilisateurService {

    private final AvisUtilisateurRepository avisRepository;

    public AvisUtilisateurService(AvisUtilisateurRepository avisRepository) {
        this.avisRepository = avisRepository;
    }

    // ----------------------------
    // CREATE
    // ----------------------------
    public AvisUtilisateur ajouterAvis(AvisUtilisateur avis) {

        if (avis == null) {
            throw new IllegalArgumentException("Avis est null");
        }

        if (avis.getVoitureId() == null) {
            throw new IllegalArgumentException("voitureId est obligatoire");
        }

        if (avis.getAcheteurId() == null) {
            throw new IllegalArgumentException("acheteurId est obligatoire");
        }

        if (avis.getNote() < 1 || avis.getNote() > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5");
        }

        // 🔒 règle métier principale
        if (avisRepository.existsByVoitureIdAndAcheteurId(
                avis.getVoitureId(), avis.getAcheteurId())) {
            throw new RuntimeException("Cet utilisateur a déjà donné un avis pour cette voiture");
        }

        return avisRepository.save(avis);
    }

    // ----------------------------
    // READ
    // ----------------------------

    @Transactional(readOnly = true)
    public List<AvisUtilisateur> getAvisByVoiture(Long voitureId) {
        if (voitureId == null) {
            throw new IllegalArgumentException("voitureId est obligatoire");
        }
        return avisRepository.findByVoitureId(voitureId);
    }

    @Transactional(readOnly = true)
    public List<AvisUtilisateur> getAvisByAcheteur(Long acheteurId) {
        if (acheteurId == null) {
            throw new IllegalArgumentException("acheteurId est obligatoire");
        }
        return avisRepository.findByAcheteurId(acheteurId);
    }

    @Transactional(readOnly = true)
    public AvisUtilisateur getAvis(Long voitureId, Long acheteurId) {
        return avisRepository.findByVoitureIdAndAcheteurId(voitureId, acheteurId)
                .orElseThrow(() ->
                        new RuntimeException("Avis introuvable"));
    }

    // ----------------------------
    // UPDATE
    // ----------------------------
    public AvisUtilisateur modifierAvis(Long id, AvisUtilisateur data) {

        AvisUtilisateur existing = avisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avis introuvable avec id=" + id));

        if (data.getCommentaire() != null) {
            existing.setCommentaire(data.getCommentaire());
        }

        if (data.getNote() >= 1 && data.getNote() <= 5) {
            existing.setNote(data.getNote());
        }

        return avisRepository.save(existing);
    }

    // ----------------------------
    // DELETE
    // ----------------------------
    public void supprimerAvis(Long id) {
        if (!avisRepository.existsById(id)) {
            throw new RuntimeException("Suppression impossible, avis introuvable");
        }
        avisRepository.deleteById(id);
    }
}
