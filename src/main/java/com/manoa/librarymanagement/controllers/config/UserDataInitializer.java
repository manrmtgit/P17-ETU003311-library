package com.manoa.librarymanagement.controllers.config;


import com.manoa.librarymanagement.models.utilisateur.Utilisateur;
import com.manoa.librarymanagement.repositories.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserDataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initUsers(UtilisateurRepository utilisateurRepository) {
        return args -> {
            if (utilisateurRepository.findByUsername("admin").isEmpty()) {
                Utilisateur admin = Utilisateur.builder()
                        .username("admin")
                        .motDePasse(passwordEncoder.encode("adminpass"))
                        .role(Utilisateur.Role.BIBLIOTHECAIRE)
                        .actif(true)
                        .build();
                utilisateurRepository.save(admin);
            }

            if (utilisateurRepository.findByUsername("john").isEmpty()) {
                Utilisateur adherent = Utilisateur.builder()
                        .username("john")
                        .motDePasse(passwordEncoder.encode("johnpass"))
                        .role(Utilisateur.Role.ADHERENT)
                        .actif(true)
                        .build();
                utilisateurRepository.save(adherent);
            }
        };
    }
}
