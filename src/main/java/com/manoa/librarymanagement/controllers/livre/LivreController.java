package com.manoa.librarymanagement.controllers.livre;

import com.manoa.librarymanagement.repositories.livre.LivreRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@RequiredArgsConstructor
public class LivreController {

    private final LivreRepository livreRepository;

    @GetMapping("/recherche")
    public String rechercheLivre(@RequestParam String titre, Model model) {
        model.addAttribute("livres", livreRepository.findByTitreContainingIgnoreCase(titre));
        return "admin/livres";
    }
}
