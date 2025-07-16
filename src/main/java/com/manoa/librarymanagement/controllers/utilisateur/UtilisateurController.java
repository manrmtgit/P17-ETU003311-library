package com.manoa.librarymanagement.controllers.utilisateur;

import com.manoa.librarymanagement.models.utilisateur.Adherent;
import com.manoa.librarymanagement.models.utilisateur.Utilisateur;
import com.manoa.librarymanagement.repositories.utilisateur.AdherentRepository;
import com.manoa.librarymanagement.repositories.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final AdherentRepository adherentRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String listeUtilisateur(Model model) {
        model.addAttribute("utilisateurs", utilisateurRepository.findAll());
        return "admin/utilisateurs";
    }

    @GetMapping("/ajouter")
    public String formAjout(Model model) {
        model.addAttribute("adherents", adherentRepository.findAll());
        return "admin/utilisateur-ajout";
    }

    @PostMapping("/ajouter")
    public String ajouter(@RequestParam Long adherentId,
                          @RequestParam String username,
                          @RequestParam String motDePasse,
                          @RequestParam String role) {
        Adherent adherent = adherentRepository.findById(adherentId).orElseThrow();
        Utilisateur utilisateur = Utilisateur.builder()
                .adherent(adherent)
                .username(username)
                .motDePasse(passwordEncoder.encode(motDePasse))
                .role(Utilisateur.Role.valueOf(role))
                .actif(true)
                .build();
        utilisateurRepository.save(utilisateur);
        return "redirect:/admin/adherents";
    }
}