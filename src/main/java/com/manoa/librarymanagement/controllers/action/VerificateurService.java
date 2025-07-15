package com.manoa.librarymanagement.controllers.action;


import com.manoa.librarymanagement.models.action.Penalite;
import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.models.utilisateur.Profil;
import com.manoa.librarymanagement.repositories.action.JourFerieRepository;
import com.manoa.librarymanagement.repositories.action.PenaliteRepository;
import com.manoa.librarymanagement.repositories.action.PretRepository;
import com.manoa.librarymanagement.repositories.utilisateur.ProfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificateurService {

    private final JourFerieRepository jourFerieRepository;
    private final PenaliteRepository penaliteRepository;
    private final ProfilRepository profilRepository;
    private final PretRepository pretRepository;

    public boolean estJourFerie(LocalDate date) {
        return jourFerieRepository.findAll().stream()
                .anyMatch(j -> j.getDate().equals(date));
    }

    public boolean peutFaireOperationCeJour(LocalDate date) {
        return !estJourFerie(date);
    }

    public void verifierEtPenaliserRetards() {
        List<Pret> prets = pretRepository.findAll();
        for (Pret p : prets) {
            if (p.getDateRetourEffective() != null &&
                    p.getDateRetourEffective().isAfter(p.getDateRetourPrevue())) {

                boolean deja = penaliteRepository.findAll().stream()
                        .anyMatch(pe -> pe.getAdherent().equals(p.getAdherent()) &&
                                pe.getDateDebut().equals(p.getDateRetourEffective()));

                if (!deja) {
                    Profil profil = p.getAdherent().getProfil();
                    Penalite penalite = Penalite.builder()
                            .adherent(p.getAdherent())
                            .dateDebut(p.getDateRetourEffective())
                            .dateFin(p.getDateRetourEffective().plusDays(profil.getJoursPenalite()))
                            .motif("Retour en retard du livre")
                            .reglee(false)
                            .build();
                    penaliteRepository.save(penalite);
                }
            }
        }
    }
}