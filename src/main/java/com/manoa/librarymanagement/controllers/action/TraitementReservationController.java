package com.manoa.librarymanagement.controllers.action;


import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.models.action.Reservation;
import com.manoa.librarymanagement.models.statut.StatutExemplaire;
import com.manoa.librarymanagement.models.statut.StatutReservation;
import com.manoa.librarymanagement.repositories.action.PretRepository;
import com.manoa.librarymanagement.repositories.action.ReservationRepository;
import com.manoa.librarymanagement.repositories.statut.StatutExemplaireRepository;
import com.manoa.librarymanagement.repositories.statut.StatutReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class TraitementReservationController {

    private final ReservationRepository reservationRepository;
    private final PretRepository pretRepository;
    private final StatutExemplaireRepository statutExemplaireRepository;
    private final StatutReservationRepository statutReservationRepository;

    @PostMapping("/convertir/{id}")
    public String convertirReservation(@PathVariable Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();

        Pret pret = Pret.builder()
                .adherent(reservation.getAdherent())
                .exemplaire(reservation.getExemplaire())
                .datePret(LocalDate.now())
                .dateRetourPrevue(LocalDate.now().plusDays(15))
                .typePret(Pret.TypePret.valueOf("EMPORTE"))
                .build();

        pretRepository.save(pret);

        reservation.setDateConversionPret(LocalDate.now());
        reservation.setStatut(statutReservationRepository.findAll().stream()
                .filter(s -> s.getStatut() == StatutReservation.DescriptionStatut.ACCEPTEE)
                .findFirst().orElseThrow());
        reservationRepository.save(reservation);

        StatutExemplaire statut = new StatutExemplaire();
        statut.setDescription(StatutExemplaire.DescriptionStatut.PRETE);
        statut.setDateDebut(LocalDate.now());
        statut.setDateFin(LocalDate.now().plusDays(15));
        statutExemplaireRepository.save(statut);

        reservation.getExemplaire().setStatut(statut);
        return "redirect:/admin/prets";
    }
}