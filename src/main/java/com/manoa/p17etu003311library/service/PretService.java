package com.manoa.p17etu003311library.service;


import com.manoa.p17etu003311library.model.*;
import com.manoa.p17etu003311library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.manoa.p17etu003311library.model.Adherent.TypeAdherent.*;

@Service
public class PretService {

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @Autowired
    private PretStatutRepository pretStatutRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private static final double PENALTY_RATE = 5000.0; // 5000Ar per day
    private static final int MAX_PROLONGATIONS = 2;
    private static final int GRACE_PERIOD_DAYS = 7;

    @Transactional
    public Pret createPret(Long adherentId, Long exemplaireId, Pret.TypePret type) {
        Adherent adherent = adherentRepository.findById(adherentId)
                .orElseThrow(() -> new IllegalArgumentException("Adherent not found"));
        Exemplaire exemplaire = exemplaireRepository.findById(exemplaireId)
                .orElseThrow(() -> new IllegalArgumentException("Exemplaire not found"));

        // Check subscription validity
        if (!isSubscriptionValid(adherent)) {
            throw new IllegalStateException("Adherent's subscription is expired");
        }

        // Check Pret quotas
        if (!canBorrow(adherent)) {
            throw new IllegalStateException("Adherent has reached Pret limit");
        }

        // Check if exemplaire is available
        ExemplaireStatut latestStatus = getLatestExemplaireStatus(exemplaire);
        if (latestStatus == null || !latestStatus.getStatut().equals(ExemplaireStatut.StatutExemplaire.DISPONIBLE)) {
            throw new IllegalStateException("Exemplaire is not available");
        }

        // Create Pret
        Pret pret = new Pret();
        pret.setAdherent(adherent);
        pret.setExemplaire(exemplaire);
        pret.setDateDebut(LocalDate.now());
        pret.setType(type);
        pret.setDateFinPrevue(calculateDueDate(adherent, type));
        pret = pretRepository.save(pret);

        // Update exemplaire status
        updateExemplaireStatus(exemplaire, ExemplaireStatut.StatutExemplaire.PRETE);

        // Update pret status
        updatePretStatus(pret, PretStatut.StatutPret.EN_COURS);

        return pret;
    }

    @Transactional
    public Pret returnPret(Long pretId) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new IllegalArgumentException("Pret not found"));

        if (getLatestPretStatus(pret).equals(PretStatut.StatutPret.RETOURNE)) {
            throw new IllegalStateException("Pret already returned");
        }

        pret.setDateRetourEffective(LocalDate.now());
        pret = pretRepository.save(pret);

        // Update pret status
        updatePretStatus(pret, PretStatut.StatutPret.RETOURNE);

        // Update exemplaire status
        updateExemplaireStatus(pret.getExemplaire(), ExemplaireStatut.StatutExemplaire.DISPONIBLE);

        // Check for penalties
        calculatePenalties(pret);

        return pret;
    }

    @Transactional
    public Pret prolongPret(Long pretId) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new IllegalArgumentException("Pret not found"));

        // Check if prolongation is allowed
        long prolongationCount = pretStatutRepository.findAll().stream()
                .filter(status -> status.getPret().getId().equals(pretId))
                .filter(status -> status.getStatut().equals(PretStatut.StatutPret.EN_COURS))
                .count() - 1; // Initial EN_COURS doesn't count
        if (prolongationCount >= MAX_PROLONGATIONS) {
            throw new IllegalStateException("Maximum prolongations reached");
        }

        // Check if there are pending reservations
        if (reservationRepository.findAll().stream()
                .anyMatch(res -> res.getLivre().getId().equals(pret.getExemplaire().getLivre().getId())
                        && res.getStatut().equals(ReservationStatut.StatutReservation.EN_ATTENTE))) {
            throw new IllegalStateException("Prolongation not allowed due to pending reservations");
        }

        pret.setDateFinPrevue(calculateDueDate(pret.getAdherent(), pret.getType()));
        pret = pretRepository.save(pret);

        return pret;
    }

    private LocalDate calculateDueDate(Adherent adherent, Pret.TypePret type) {
        int weeks;
        switch (adherent.getType()) {
            case ETUDIANT:
                weeks = 3;
                break;
            case PROFESSEUR:
                weeks = 6;
                break;
            case PERSONNEL:
                weeks = 2;
                break;
            case ANONYME:
                weeks = 1;
                break;
            default:
                throw new IllegalArgumentException("Unknown adherent type");
        }
        return LocalDate.now().plusWeeks(weeks);
    }

    private boolean isSubscriptionValid(Adherent adherent) {
        LocalDate today = LocalDate.now();
        return !adherent.getDateFinAbonnement().isBefore(today.minusDays(GRACE_PERIOD_DAYS));
    }

    private boolean canBorrow(Adherent adherent) {
        int maxPrets;
        switch (adherent.getType()) {
            case ETUDIANT:
                maxPrets = 5;
                break;
            case PROFESSEUR:
                maxPrets = 10;
                break;
            case PERSONNEL:
                maxPrets = 3;
                break;
            case ANONYME:
                maxPrets = 1;
                break;
            default:
                throw new IllegalArgumentException("Unknown adherent type");
        }

        long activePrets = pretRepository.findAll().stream()
                .filter(pret -> pret.getAdherent().getId().equals(adherent.getId()))
                .filter(pret -> getLatestPretStatus(pret).equals(PretStatut.StatutPret.EN_COURS))
                .count();
        return activePrets < maxPrets;
    }

    private void calculatePenalties(Pret pret) {
        LocalDate dueDate = pret.getDateFinPrevue();
        LocalDate returnDate = pret.getDateRetourEffective();
        if (returnDate != null && returnDate.isAfter(dueDate)) {
            long daysLate = calculateDaysLate(dueDate, returnDate);
            if (daysLate > 0) {
                Penalite penalite = new Penalite();
                penalite.setPret(pret);
                penalite.setMontant(daysLate * PENALTY_RATE);
                penalite.setDateEmission(LocalDate.now());
                penaliteRepository.save(penalite);
                updatePenaliteStatus(penalite, PenaliteStatut.StatutPenalite.IMPAYEE);
            }
        }
    }

    private long calculateDaysLate(LocalDate dueDate, LocalDate returnDate) {
        List<LocalDate> holidays = jourFerieRepository.findAll().stream()
                .map(JourFerie::getDate)
                .toList();
        long days = ChronoUnit.DAYS.between(dueDate, returnDate);
        long holidayCount = holidays.stream()
                .filter(date -> !date.isBefore(dueDate) && !date.isAfter(returnDate))
                .count();
        return days - holidayCount;
    }

    private void updateExemplaireStatus(Exemplaire exemplaire, ExemplaireStatut.StatutExemplaire statut) {
        ExemplaireStatut status = new ExemplaireStatut();
        status.setExemplaire(exemplaire);
        status.setStatut(statut);
        status.setDateChangement(LocalDateTime.now());
        exemplaireStatutRepository.save(status);
    }

    private void updatePretStatus(Pret pret, PretStatut.StatutPret statut) {
        PretStatut status = new PretStatut();
        status.setPret(pret);
        status.setStatut(statut);
        status.setDateChangement(LocalDateTime.now());
        pretStatutRepository.save(status);
    }

    private ExemplaireStatut getLatestExemplaireStatus(Exemplaire exemplaire) {
        return exemplaireStatutRepository.findAll().stream()
                .filter(status -> status.getExemplaire().getId().equals(exemplaire.getId()))
                .max((s1, s2) -> s1.getDateChangement().compareTo(s2.getDateChangement()))
                .orElse(null);
    }

    private PretStatut getLatestPretStatus(Pret pret) {
        return pretStatutRepository.findAll().stream()
                .filter(status -> status.getPret().getId().equals(pret.getId()))
                .max((s1, s2) -> s1.getDateChangement().compareTo(s2.getDateChangement()))
                .orElse(null);
    }

    @Autowired
    private PenaliteRepository penaliteRepository;

    @Autowired
    private PenaliteStatutRepository penaliteStatutRepository;

    private void updatePenaliteStatus(Penalite penalite, PenaliteStatut.StatutPenalite statut) {
        PenaliteStatut status = new PenaliteStatut();
        status.setPenalite(penalite);
        status.setStatut(statut);
        status.setDateChangement(LocalDateTime.now());
        penaliteStatutRepository.save(status);
    }
}