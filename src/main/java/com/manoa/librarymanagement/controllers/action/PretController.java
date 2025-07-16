package com.manoa.librarymanagement.controllers.action;


import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.models.livre.Exemplaire;
import com.manoa.librarymanagement.models.utilisateur.Adherent;
import com.manoa.librarymanagement.repositories.action.PretRepository;
import com.manoa.librarymanagement.repositories.livre.ExemplaireRepository;
import com.manoa.librarymanagement.repositories.utilisateur.AdherentRepository;
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
@RequestMapping("/admin/prets")
@RequiredArgsConstructor
public class PretController {

    private final PretRepository pretRepository;
    private final PretService pretService;
    private final AdherentRepository adherentRepository;
    private final ExemplaireRepository exemplaireRepository;

    @GetMapping
    public String listerPrets(Model model) {
        List<Pret> prets = pretRepository.findAll();
        model.addAttribute("prets", prets);
        return "admin/prets";
    }

    // Pour l'admin
    @GetMapping("/{id}")
    public String voirDetailPret(@PathVariable Long id, Model model) {
        Pret pret = pretRepository.findById(id).orElseThrow();
        model.addAttribute("pret", pret);
        return "admin/pret-detail";
    }

    @PostMapping("/{id}/retour")
    public String validerRetour(@PathVariable Long id) {
        Pret pret = pretRepository.findById(id).orElseThrow();
        pretService.rendreLivre(pret, LocalDate.now());
        return "redirect:/admin/prets";
    }

    @PostMapping("/{id}/prolonger")
    public String prolongerPret(@PathVariable Long id, @RequestParam("nouvelleDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nouvelleDate) {
        Pret pret = pretRepository.findById(id).orElseThrow();
        pretService.prolongerPret(pret, nouvelleDate);
        return "redirect:/admin/prets";
    }

    @PostMapping("/valider")
    public String validerPret(@RequestParam Long adherentId, @RequestParam Long exemplaireId, Model model) {
        Adherent adherent = adherentRepository.findById(adherentId).orElseThrow();
        Exemplaire exemplaire = exemplaireRepository.findById(exemplaireId).orElseThrow();
        LocalDate datePret = LocalDate.now();
        LocalDate dateRetourPrevue = datePret.plusDays(14);

        if (!pretService.peutPreter(adherent, exemplaire, datePret, dateRetourPrevue)) {
            model.addAttribute("error", "Conditions de prêt non remplies");
            return "error/default";
        }
        pretService.preterLivre(adherent, exemplaire, datePret, dateRetourPrevue, Pret.TypePret.SUR_PLACE);
        return "redirect:/admin/prets";
    }
}
