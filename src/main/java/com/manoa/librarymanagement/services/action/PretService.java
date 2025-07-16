package com.manoa.librarymanagement.services.action;

import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.models.action.Penalite;
import com.manoa.librarymanagement.models.action.Prolongement;
import com.manoa.librarymanagement.models.livre.Exemplaire;
import com.manoa.librarymanagement.models.utilisateur.Adherent;
import com.manoa.librarymanagement.models.utilisateur.Profil;
import com.manoa.librarymanagement.repositories.action.*;
import com.manoa.librarymanagement.repositories.livre.ExemplaireRepository;
import com.manoa.librarymanagement.repositories.utilisateur.AdherentRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Data
@Service
@RequiredArgsConstructor
public class PretService {

    private final PretRepository pretRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final AdherentRepository adherentRepository;
    private final AbonnementRepository abonnementRepository;
    private final PenaliteRepository penaliteRepository;
    private final JourFerieRepository jourFerieRepository;
    private final ProlongementRepository prolongementRepository;

    // Vérifie si un adhérent peut emprunter un exemplaire à une date donnée
    public boolean peutPreter(Adherent adherent, Exemplaire exemplaire, LocalDate datePret, LocalDate dateRetourPrevue) {
        // Abonnement actif
        boolean abonne = abonnementRepository.existsByAdherentIdAndDateDebutBeforeAndDateFinAfter(
                adherent.getId(), datePret, dateRetourPrevue);
        if (!abonne) return false;

        // Pas de pénalité active
        boolean penalise = penaliteRepository.existsByAdherentIdAndDateFinAfter(adherent.getId(), datePret);
        if (penalise) return false;

        // Quota de prêt non dépassé
        int quotaPret = adherent.getProfil().getQuotaPret();
        long nbPrets = pretRepository.countByAdherentIdAndDateRetourEffectiveIsNull(adherent.getId());
        if (nbPrets >= quotaPret) return false;

        // Âge requis
        int age = Period.between(adherent.getDateNaissance(), datePret).getYears();
        int ageMin = exemplaire.getLivre().getAgeMinimum();
        if (age < ageMin) return false;

        // Jours fériés
        if (jourFerieRepository.existsByDate(datePret) || jourFerieRepository.existsByDate(dateRetourPrevue))
            return false;

        // Disponibilité exemplaire
        return exemplaire.getDisponible() != null && exemplaire.getDisponible();
    }

    // Effectue le prêt si toutes les conditions sont remplies
    public void preterLivre(Adherent adherent, Exemplaire exemplaire,
                            LocalDate datePret, LocalDate dateRetourPrevue, Pret.TypePret typePret) {
        if (!peutPreter(adherent, exemplaire, datePret, dateRetourPrevue)) {
            throw new IllegalStateException("Conditions de prêt non remplies");
        }
        Pret pret = Pret.builder()
                .adherent(adherent)
                .exemplaire(exemplaire)
                .datePret(datePret)
                .dateRetourPrevue(dateRetourPrevue)
                .typePret(typePret)
                .build();
        pretRepository.save(pret);

        exemplaire.setDisponible(false);
        exemplaireRepository.save(exemplaire);

    }

    // Vérifie si le retour est possible (pas un jour férié)
    public boolean peutRendre(Pret pret, LocalDate dateRetour) {
        return !jourFerieRepository.existsByDate(dateRetour);
    }

    // Effectue le retour et applique une pénalité si nécessaire
    public void rendreLivre(Pret pret, LocalDate dateRetour) {
        if (!peutRendre(pret, dateRetour)) {
            throw new IllegalStateException("Impossible de rendre le livre un jour férié");
        }
        pret.setDateRetourEffective(dateRetour);
        pretRepository.save(pret);

        Exemplaire exemplaire = pret.getExemplaire();
        exemplaire.setDisponible(true);
        exemplaireRepository.save(exemplaire);

        // Appliquer pénalité si retard
        if (dateRetour.isAfter(pret.getDateRetourPrevue())) {
            appliquerPenalite(pret);
        }
    }

    // Applique une pénalité à l’adhérent pour un prêt en retard
    public void appliquerPenalite(Pret pret) {
        Adherent adherent = pret.getAdherent();
        Profil profil = adherent.getProfil();
        LocalDate debut = pret.getDateRetourEffective();
        LocalDate fin = debut.plusDays(profil.getJoursPenalite());
        Penalite penalite = Penalite.builder()
                .adherent(adherent)
                .dateDebut(debut)
                .dateFin(fin)
                .motif("Retour en retard du livre")
                .reglee(false)
                .build();
        penaliteRepository.save(penalite);
    }

    // Vérifie si un prêt peut être prolongé (un seul prolongement)
    public boolean peutProlonger(Pret pret) {
        return pret.getProlongement() == null;
    }

    // Prolonge un prêt si possible
    public void prolongerPret(Pret pret, LocalDate nouvelleDateRetour) {
        if (!peutProlonger(pret)) {
            throw new IllegalStateException("Déjà prolongé");
        }
        if (jourFerieRepository.existsByDate(nouvelleDateRetour)) {
            throw new IllegalStateException("Impossible de prolonger jusqu'à un jour férié");
        }
        Prolongement prolongement = Prolongement.builder()
                .pret(pret)
                .dateProlongement(LocalDate.now())
                .nouvelleDateRetour(nouvelleDateRetour)
                .build();
        prolongementRepository.save(prolongement);

        pret.setDateRetourPrevue(nouvelleDateRetour);
        pretRepository.save(pret);
    }

    // Vérifie si l’adhérent a atteint son quota de prêt
    public boolean quotaAtteint(Adherent adherent) {
        int quotaPret = adherent.getProfil().getQuotaPret();
        long nbPrets = pretRepository.countByAdherentIdAndDateRetourEffectiveIsNull(adherent.getId());
        return nbPrets >= quotaPret;
    }
}