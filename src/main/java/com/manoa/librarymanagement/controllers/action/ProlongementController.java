package com.manoa.librarymanagement.controllers.action;


import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.models.action.Prolongement;
import com.manoa.librarymanagement.repositories.action.PretRepository;
import com.manoa.librarymanagement.repositories.action.ProlongementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/user/prolongements")
@RequiredArgsConstructor
public class ProlongementController {

    private final PretRepository pretRepository;
    private final ProlongementRepository prolongementRepository;

    @GetMapping
    public String afficherDemandes(Model model) {
        model.addAttribute("prets", pretRepository.findAll());
        return "user/prolongements";
    }

    @PostMapping("/ajouter/{idPret}")
    public String demanderProlongement(@PathVariable Long idPret) {
        Pret pret = pretRepository.findById(idPret).orElseThrow();
        if (prolongementRepository.findByPret(pret).isPresent()) {
            return "redirect:/user/prolongements?error=deja_prolonge";
        }
        Prolongement prolongement = Prolongement.builder()
                .pret(pret)
                .dateProlongement(LocalDate.now())
                .nouvelleDateRetour(pret.getDateRetourPrevue().plusDays(7))
                .build();
        prolongementRepository.save(prolongement);

        pret.setDateRetourPrevue(prolongement.getNouvelleDateRetour());
        pretRepository.save(pret);

        return "redirect:/user/prolongements?success=true";
    }
}