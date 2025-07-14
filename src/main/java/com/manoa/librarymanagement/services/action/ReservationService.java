package com.manoa.librarymanagement.services.action;


import com.manoa.librarymanagement.models.livre.Livre;
import com.manoa.librarymanagement.models.statut.StatutReservation;
import com.manoa.librarymanagement.models.utilisateur.Adherent;
import com.manoa.librarymanagement.models.utilisateur.Profil;
import com.manoa.librarymanagement.repositories.action.AbonnementRepository;
import com.manoa.librarymanagement.repositories.action.PenaliteRepository;
import com.manoa.librarymanagement.repositories.action.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PenaliteRepository penaliteRepository;
    private final AbonnementRepository abonnementRepository;

    public boolean peutReserver(Adherent adherent, Livre livre) {
        Profil profil = adherent.getProfil();

        // Vérifier la pénalité active
        boolean penalise = penaliteRepository.findAll().stream()
                .anyMatch(p -> p.getAdherent().equals(adherent) &&
                        p.getDateFin().isAfter(LocalDate.now()));

        if (penalise) return false;

        // Vérifier l'abonnement actif
        boolean abonne = abonnementRepository.findAll().stream()
                .anyMatch(a -> a.getAdherent().equals(adherent) &&
                        a.getDateDebut().isBefore(LocalDate.now()) &&
                        a.getDateFin().isAfter(LocalDate.now()));

        if (!abonne) return false;

        // Vérifier l'âge minimum
        int age = Period.between(adherent.getDateNaissance(), LocalDate.now()).getYears();
        if (age < livre.getAgeMinimum()) return false;

        // Vérifier quota de réservation
        long count = reservationRepository.findAll().stream()
                .filter(r -> r.getAdherent().equals(adherent) &&
                        r.getStatut().getStatut() == StatutReservation.DescriptionStatut.EN_ATTENTE)
                .count();

        return count < profil.getQuotaReservation();
    }
}