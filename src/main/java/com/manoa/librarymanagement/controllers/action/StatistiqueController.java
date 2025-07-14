package com.manoa.librarymanagement.controllers.action;


import com.manoa.librarymanagement.repositories.action.PenaliteRepository;
import com.manoa.librarymanagement.repositories.action.PretRepository;
import com.manoa.librarymanagement.repositories.action.ReservationRepository;
import com.manoa.librarymanagement.repositories.utilisateur.AdherentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class StatistiqueController {

    private final AdherentRepository adherentRepository;
    private final ReservationRepository reservationRepository;
    private final PretRepository pretRepository;
    private final PenaliteRepository penaliteRepository;

    @GetMapping
    public String stats(Model model) {
        model.addAttribute("nbAdherents", adherentRepository.count());
        model.addAttribute("nbPrets", pretRepository.count());
        model.addAttribute("nbReservations", reservationRepository.count());
        model.addAttribute("nbPenalites", penaliteRepository.count());
        return "admin/statistiques";
    }
}