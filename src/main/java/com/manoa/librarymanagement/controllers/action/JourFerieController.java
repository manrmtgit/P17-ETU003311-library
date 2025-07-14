package com.manoa.librarymanagement.controllers.action;


import com.manoa.librarymanagement.models.action.JourFerie;
import com.manoa.librarymanagement.repositories.action.JourFerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/jours-feries")
@RequiredArgsConstructor
public class JourFerieController {

    private final JourFerieRepository jourFerieRepository;

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("jours", jourFerieRepository.findAll());
        model.addAttribute("nouveauJour", new JourFerie());
        return "admin/jours-feries";
    }

    @PostMapping("/ajouter")
    public String ajouter(@ModelAttribute JourFerie jourFerie) {
        jourFerieRepository.save(jourFerie);
        return "redirect:/admin/jours-feries";
    }

    @PostMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        jourFerieRepository.deleteById(id);
        return "redirect:/admin/jours-feries";
    }
}