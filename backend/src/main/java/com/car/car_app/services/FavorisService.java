package com.car.car_app.services;

import com.car.car_app.entities.Favoris;
import com.car.car_app.repositories.FavorisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavorisService {

    private final FavorisRepository favorisRepository;

    public FavorisService(FavorisRepository favorisRepository) {
        this.favorisRepository = favorisRepository;
    }

    // ✅ Ajouter un favori
    public Favoris ajouterFavori(Favoris favoris) {
        validateFavoris(favoris);

        // éviter doublon (même si DB a unique constraint)
        if (favorisRepository.existsByUtilisateurIdAndVoitureId(favoris.getUtilisateurId(), favoris.getVoitureId())) {
            throw new IllegalArgumentException("Ce favori existe déjà");
        }

        return favorisRepository.save(favoris);
    }

    // ✅ Ajouter par ids (pratique pour Angular)
    public Favoris ajouterFavori(Long utilisateurId, Long voitureId) {
        if (utilisateurId == null) throw new IllegalArgumentException("utilisateurId obligatoire");
        if (voitureId == null) throw new IllegalArgumentException("voitureId obligatoire");

        if (favorisRepository.existsByUtilisateurIdAndVoitureId(utilisateurId, voitureId)) {
            throw new IllegalArgumentException("Ce favori existe déjà");
        }

        Favoris fav = Favoris.builder()
                .utilisateurId(utilisateurId)
                .voitureId(voitureId)
                .build();

        return favorisRepository.save(fav);
    }

    // ✅ Lire tous les favoris
    @Transactional(readOnly = true)
    public List<Favoris> getAllFavoris() {
        return favorisRepository.findAll();
    }

    // ✅ Favoris d’un utilisateur
    @Transactional(readOnly = true)
    public List<Favoris> getFavorisByUtilisateur(Long utilisateurId) {
        if (utilisateurId == null) {
            throw new IllegalArgumentException("utilisateurId obligatoire");
        }
        return favorisRepository.findByUtilisateurId(utilisateurId);
    }

    // ✅ Vérifier si un favori existe (utile bouton ❤️)
    @Transactional(readOnly = true)
    public boolean isFavori(Long utilisateurId, Long voitureId) {
        if (utilisateurId == null) throw new IllegalArgumentException("utilisateurId obligatoire");
        if (voitureId == null) throw new IllegalArgumentException("voitureId obligatoire");
        return favorisRepository.existsByUtilisateurIdAndVoitureId(utilisateurId, voitureId);
    }

    // ✅ Supprimer un favori par ID
    public void supprimerFavori(Long id) {
        if (id == null) throw new IllegalArgumentException("id obligatoire");
        if (!favorisRepository.existsById(id)) {
            throw new RuntimeException("Favori introuvable id=" + id);
        }
        favorisRepository.deleteById(id);
    }

    // ✅ Supprimer un favori par (utilisateurId, voitureId) (pratique)
    public void supprimerFavori(Long utilisateurId, Long voitureId) {
        if (utilisateurId == null) throw new IllegalArgumentException("utilisateurId obligatoire");
        if (voitureId == null) throw new IllegalArgumentException("voitureId obligatoire");

        if (!favorisRepository.existsByUtilisateurIdAndVoitureId(utilisateurId, voitureId)) {
            throw new RuntimeException("Favori introuvable pour utilisateurId=" + utilisateurId + " et voitureId=" + voitureId);
        }

        favorisRepository.deleteByUtilisateurIdAndVoitureId(utilisateurId, voitureId);
    }

    // ✅ Toggle favoris : si existe -> delete, sinon -> add
    public String toggleFavori(Long utilisateurId, Long voitureId) {
        if (utilisateurId == null) throw new IllegalArgumentException("utilisateurId obligatoire");
        if (voitureId == null) throw new IllegalArgumentException("voitureId obligatoire");

        boolean existe = favorisRepository.existsByUtilisateurIdAndVoitureId(utilisateurId, voitureId);

        if (existe) {
            favorisRepository.deleteByUtilisateurIdAndVoitureId(utilisateurId, voitureId);
            return "SUPPRIME";
        } else {
            Favoris fav = Favoris.builder()
                    .utilisateurId(utilisateurId)
                    .voitureId(voitureId)
                    .build();
            favorisRepository.save(fav);
            return "AJOUTE";
        }
    }

    // ✅ validation simple
    private void validateFavoris(Favoris f) {
        if (f == null) throw new IllegalArgumentException("Favoris null");
        if (f.getUtilisateurId() == null) throw new IllegalArgumentException("utilisateurId obligatoire");
        if (f.getVoitureId() == null) throw new IllegalArgumentException("voitureId obligatoire");
    }
}
