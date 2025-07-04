package com.manoa.p17etu003311library.service;


import com.manoa.p17etu003311library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private ReservationStatutPretRepository reservationStatutPretRepository;

    private static final int RESERVATION_EXPIRATION_DAYS = 3;

    @Transactional
    public Reservation createReservation(Long adherentId, Long livreId) {
        Adherent adherent = adherentRepository.findById(adherentId)
                .orElseThrow(() -> new IllegalArgumentException("Adherent not found"));
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new IllegalArgumentException("Livre not found"));

        // Check subscription validity
        if (!isSubscriptionValid(adherent)) {
            throw new IllegalStateException("Adherent's subscription is expired");
        }

        // Check if there are available exemplaires
        boolean hasAvailableExemplaire = exemplaireRepository.findAll().stream()
                .filter(ex -> ex.getLivre().getId().equals(livreId))
                .anyMatch(ex -> getLatestExemplaireStatus(ex).getStatut().equals(ExemplaireStatutPret.StatutExemplaire.DISPONIBLE));

        Reservation reservation = new Reservation();
        reservation.setAdherent(adherent);
        reservation.setLivre(livre);
        reservation.setDateDemande(LocalDate.now());
        reservation.setDateExpiration(hasAvailableExemplaire ? LocalDate.now().plusDays(RESERVATION_EXPIRATION_DAYS) : null);
        reservation = reservationRepository.save(reservation);

        // Update reservation status
        updateReservationStatus(reservation, hasAvailableExemplaire ?
                ReservationStatutPret.StatutReservation.HONOREE :
                ReservationStatutPret.StatutReservation.EN_ATTENTE);

        return reservation;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (getLatestReservationStatus(reservation).equals(ReservationStatutPret.StatutReservation.ANNULEE)) {
            throw new IllegalStateException("Reservation already cancelled");
        }

        updateReservationStatus(reservation, ReservationStatutPret.StatutReservation.ANNULEE);
    }

    @Transactional
    public void honorReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (!getLatestReservationStatus(reservation).equals(ReservationStatutPret.StatutReservation.EN_ATTENTE)) {
            throw new IllegalStateException("Reservation is not in waiting state");
        }

        // Check if an exemplaire is available
        Exemplaire availableExemplaire = exemplaireRepository.findAll().stream()
                .filter(ex -> ex.getLivre().getId().equals(reservation.getLivre().getId()))
                .filter(ex -> getLatestExemplaireStatus(ex).getStatut().equals(ExemplaireStatutPret.StatutExemplaire.DISPONIBLE))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No available exemplaire for this livre"));

        reservation.setDateExpiration(LocalDate.now().plusDays(RESERVATION_EXPIRATION_DAYS));
        reservationRepository.save(reservation);

        updateReservationStatus(reservation, ReservationStatutPret.StatutReservation.HONOREE);
        updateExemplaireStatus(availableExemplaire, ExemplaireStatutPret.StatutExemplaire.RESERVE);
    }

    private boolean isSubscriptionValid(Adherent adherent) {
        LocalDate today = LocalDate.now();
        return !adherent.getDateFinAbonnement().isBefore(today.minusDays(7));
    }

    private void updateReservationStatus(Reservation reservation, ReservationStatutPret.StatutReservation statut) {
        ReservationStatutPret status = new ReservationStatutPret();
        status.setReservation(reservation);
        status.setStatut(statut);
        status.setDateChangement(LocalDateTime.now());
        reservationStatutPretRepository.save(status);
    }

    private void updateExemplaireStatus(Exemplaire exemplaire, ExemplaireStatutPret.StatutExemplaire statut) {
        ExemplaireStatutPret status = new ExemplaireStatutPret();
        status.setExemplaire(exemplaire);
        status.setStatut(statut);
        status.setDateChangement(LocalDateTime.now());
        exemplaireStatutPretRepository.save(status);
    }

    private ExemplaireStatutPret getLatestExemplaireStatus(Exemplaire exemplaire) {
        return exemplaireStatutPretRepository.findAll().stream()
                .filter(status -> status.getExemplaire().getId().equals(exemplaire.getId()))
                .max((s1, s2) -> s1.getDateChangement().compareTo(s2.getDateChangement()))
                .orElseThrow(() -> new IllegalStateException("No status history found for exemplaire"));
    }

    private ReservationStatutPret getLatestReservationStatus(Reservation reservation) {
        return reservationStatutPretRepository.findAll().stream()
                .filter(status -> status.getReservation().getId().equals(reservation.getId()))
                .max((s1, s2) -> s1.getDateChangement().compareTo(s2.getDateChangement()))
                .orElseThrow(() -> new IllegalStateException("No status history found for reservation"));
    }

    @Autowired
    private ExemplaireStatutPretRepository exemplaireStatutPretRepository;
}