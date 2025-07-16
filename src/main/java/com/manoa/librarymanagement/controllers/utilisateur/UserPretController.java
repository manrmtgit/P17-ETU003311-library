package com.manoa.librarymanagement.controllers.utilisateur;

import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.models.livre.Exemplaire;
import com.manoa.librarymanagement.models.utilisateur.Adherent;
import com.manoa.librarymanagement.models.utilisateur.Utilisateur;
import com.manoa.librarymanagement.repositories.action.PretRepository;
import com.manoa.librarymanagement.repositories.livre.ExemplaireRepository;
import com.manoa.librarymanagement.repositories.utilisateur.AdherentRepository;
import com.manoa.librarymanagement.repositories.utilisateur.UtilisateurRepository;
import com.manoa.librarymanagement.services.action.PretService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user/prets")
@RequiredArgsConstructor
public class UserPretController {

    private final PretRepository pretRepository;
    private final PretService pretService;
    private final AdherentRepository adherentRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final UtilisateurRepository utilisateurRepository;


    @GetMapping
    public String mesPrets(Model model, Principal principal) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(principal.getName())
                .orElse(null);
        if (utilisateur == null || utilisateur.getAdherent() == null) {
            model.addAttribute("error", "Aucun adhérent associé à cet utilisateur.");
            return "user/mes-prets";
        }
        Adherent adherent = utilisateur.getAdherent();
        List<Pret> prets = pretRepository.findByAdherentId(adherent.getId());
        model.addAttribute("prets", prets);
        return "user/mes-prets";
    }

    @GetMapping("/demander-pret")
    public String montrerPagePret(Model model) {
        List<Exemplaire> exemplaires = exemplaireRepository.findAll();
        model.addAttribute("exemplaires", exemplaires);
        return "user/demander-pret";
    }

    @PostMapping("/nouveau")
    public String demanderPret(@RequestParam Long exemplaireId, Principal principal) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(principal.getName())
                .orElse(null);
        if (utilisateur == null || utilisateur.getAdherent() == null) {
            return "user/mes-prets";
        }

        Adherent adherent = utilisateur.getAdherent();
        Exemplaire exemplaire = exemplaireRepository.findById(exemplaireId).orElseThrow();
        pretService.preterLivre(adherent, exemplaire, LocalDate.now(),
                LocalDate.now().plusDays(14), Pret.TypePret.SUR_PLACE);
        return "redirect:/user/mes-prets";
    }

    @PostMapping("/{id}/prolonger-user")
    public String prolongerPretUser(@PathVariable Long id,
                                    @RequestParam("nouvelleDate")
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                    LocalDate nouvelleDate, Principal principal) {
        Pret pret = pretRepository.findById(id).orElseThrow();
        pretService.prolongerPret(pret, nouvelleDate);
        return "redirect:/user/mes-prets";
    }
}
