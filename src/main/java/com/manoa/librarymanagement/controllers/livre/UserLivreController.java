package com.manoa.librarymanagement.controllers.livre;

import com.manoa.librarymanagement.models.livre.Exemplaire;
import com.manoa.librarymanagement.repositories.livre.ExemplaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user/livres")
@RequiredArgsConstructor
public class UserLivreController {

    private final ExemplaireRepository exemplaireRepository;

    @GetMapping
    public String afficherExemplaires(Model model) {
        List<Exemplaire> exemplaires = exemplaireRepository.findAll();
        model.addAttribute("exemplaires", exemplaires);
        return "user/livres";
    }
}