package com.manoa.librarymanagement.controllers.livre;

import com.manoa.librarymanagement.models.livre.Exemplaire;
import com.manoa.librarymanagement.models.livre.Livre;
import com.manoa.librarymanagement.repositories.livre.ExemplaireRepository;
import com.manoa.librarymanagement.repositories.livre.LivreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/livres")
@RequiredArgsConstructor
public class LivreController {

    private final ExemplaireRepository exemplaireRepository;

    @GetMapping
    public String afficherLivres(Model model) {
        List<Exemplaire> exemplaires = exemplaireRepository.findAll();
        model.addAttribute("exemplaires", exemplaires);

        return "admin/livres";
    }
}