package com.manoa.librarymanagement.controllers.config;

import com.manoa.librarymanagement.models.utilisateur.Adherent;
import com.manoa.librarymanagement.models.utilisateur.Utilisateur;
import com.manoa.librarymanagement.repositories.utilisateur.AdherentRepository;
import com.manoa.librarymanagement.repositories.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class UserDataInitializer {

    private final PasswordEncoder passwordEncoder;
    private final AdherentRepository adherentRepository;

    @Bean
    public CommandLineRunner initUsers(UtilisateurRepository utilisateurRepository) {
        return args -> {
            // Admin user
            if (utilisateurRepository.findByUsername("manoa").isEmpty()) {
                Adherent adminAdherent = Adherent.builder()
                        .nom("Manoa")
                        .prenom("RMT")
                        .email("rmt") // Match the username
                        .dateNaissance(LocalDate.of(1980, 1, 1))
                        .build();
                adherentRepository.save(adminAdherent);

                Utilisateur admin = Utilisateur.builder()
                        .username("manoa")
                        .motDePasse(passwordEncoder.encode("123"))
                        .role(Utilisateur.Role.BIBLIOTHECAIRE)
                        .actif(true)
                        .adherent(adminAdherent)
                        .build();
                utilisateurRepository.save(admin);
            }
        };
    }
}
