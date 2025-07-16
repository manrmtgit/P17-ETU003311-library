package com.manoa.librarymanagement.controllers.action;


import com.manoa.librarymanagement.models.action.Reservation;
import com.manoa.librarymanagement.models.livre.Exemplaire;
import com.manoa.librarymanagement.models.livre.Livre;
import com.manoa.librarymanagement.models.statut.StatutReservation;
import com.manoa.librarymanagement.models.utilisateur.Adherent;
import com.manoa.librarymanagement.models.utilisateur.Utilisateur;
import com.manoa.librarymanagement.repositories.action.AbonnementRepository;
import com.manoa.librarymanagement.repositories.action.PenaliteRepository;
import com.manoa.librarymanagement.repositories.action.ReservationRepository;
import com.manoa.librarymanagement.repositories.livre.ExemplaireRepository;
import com.manoa.librarymanagement.repositories.statut.StatutReservationRepository;
import com.manoa.librarymanagement.repositories.utilisateur.UtilisateurRepository;
import com.manoa.librarymanagement.services.action.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ExemplaireRepository exemplaireRepository;
    private final StatutReservationRepository statutRepository;
    private final ReservationRepository reservationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ReservationService reservationService;
    private final AbonnementRepository abonnementRepository;
    private final PenaliteRepository penaliteRepository;

    @GetMapping("/ajouter/{exemplaireId}")
    public String reserver(@PathVariable Long exemplaireId,
                           @AuthenticationPrincipal User user,
                           Model model) {

        Optional<Utilisateur> u = utilisateurRepository.findByUsername(user.getUsername());
        if (u.isEmpty()) return "redirect:/home";

        Adherent adherent = u.get().getAdherent();
        Exemplaire exemplaire = exemplaireRepository.findById(exemplaireId).orElseThrow();
        Livre livre = exemplaire.getLivre();

        if (!reservationService.peutReserver(adherent, livre)) {
            model.addAttribute("erreur",
                    "Conditions non remplies pour réserver ce livre.");
            return "redirect:/user/dashboard";
        }

        StatutReservation statut = statutRepository.findAll().stream()
                .filter(s -> s.getStatut() == StatutReservation.DescriptionStatut.EN_ATTENTE)
                .findFirst()
                .orElseThrow();

        Reservation r = Reservation.builder()
                .adherent(adherent)
                .exemplaire(exemplaire)
                .statut(statut)
                .dateReservation(LocalDate.now())
                .build();

        reservationRepository.save(r);
        return "redirect:/user/dashboard";
    }


    @GetMapping("/user/dashboard")
    public String userDashboard(@AuthenticationPrincipal User user, Model model) {
        Optional<Utilisateur> u = utilisateurRepository.findByUsername(user.getUsername());
        if (u.isEmpty()) return "redirect:/home";

        Adherent adherent = u.get().getAdherent();
        List<Reservation> reservations = reservationRepository.findAll();

        long count = reservations.stream()
                .filter(r -> r.getAdherent().equals(adherent) && r.getStatut().getStatut() == StatutReservation.DescriptionStatut.EN_ATTENTE)
                .count();

        model.addAttribute("username", user.getUsername());
        model.addAttribute("quotaPret", adherent.getProfil().getQuotaPret());
        model.addAttribute("quotaReservation", adherent.getProfil().getQuotaReservation());
        model.addAttribute("reservationsActives", count);

        boolean abonne = abonnementRepository.findAll().stream()
                .anyMatch(a -> a.getAdherent().equals(adherent) &&
                        a.getDateDebut().isBefore(LocalDate.now()) &&
                        a.getDateFin().isAfter(LocalDate.now()));

        boolean penalise = penaliteRepository.findAll().stream()
                .anyMatch(p -> p.getAdherent().equals(adherent) && p.getDateFin().isAfter(LocalDate.now()));

        model.addAttribute("statutAbonnement", abonne ? "Actif" : "Inactif");
        model.addAttribute("statutPenalite", penalise ? "Pénalisé" : "Aucun");
        model.addAttribute("exemplaires", exemplaireRepository.findAll());

        return "dashboard/user-dashboard";
    }
}

