package com.manoa.librarymanagement.controllers.utilisateur;

import com.manoa.librarymanagement.models.utilisateur.Adherent;
import com.manoa.librarymanagement.models.utilisateur.Profil;
import com.manoa.librarymanagement.models.action.Abonnement;
import com.manoa.librarymanagement.repositories.utilisateur.AdherentRepository;
import com.manoa.librarymanagement.repositories.utilisateur.ProfilRepository;
import com.manoa.librarymanagement.repositories.action.AbonnementRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Controller
@RequestMapping("/admin/adherents")
@RequiredArgsConstructor
public class AdherentController {

    private final AdherentRepository adherentRepository;
    private final ProfilRepository profilRepository;
    private final AbonnementRepository abonnementRepository;

    @GetMapping
    public String lister(Model model) {
        model.addAttribute("adherents", adherentRepository.findAll());
        return "admin/adherents";
    }

    @GetMapping("/ajouter")
    public String formAjout(Model model) {
        model.addAttribute("adherent", new Adherent());
        model.addAttribute("profils", profilRepository.findAll());
        return "admin/adherent-ajout";
    }

    @PostMapping("/ajouter")
    public String ajouter(@ModelAttribute Adherent adherent, @RequestParam Long profilId,
                          @RequestParam String dateDebut, @RequestParam String dateFin) {
        Profil profil = profilRepository.findById(profilId).orElseThrow();
        adherent.setProfil(profil);
        adherentRepository.save(adherent);

        Abonnement abonnement = Abonnement.builder()
                .adherent(adherent)
                .dateDebut(LocalDate.parse(dateDebut))
                .dateFin(LocalDate.parse(dateFin))
                .build();
        abonnementRepository.save(abonnement);

        return "redirect:/admin/adherents";
    }

    @GetMapping("/{id}")
    public String voir(@PathVariable Long id, Model model) {
        Adherent adherent = adherentRepository.findById(id).orElseThrow();
        model.addAttribute("adherent", adherent);
        model.addAttribute("abonnements", adherent.getAbonnements());
        return "admin/adherent-detail";
    }
}