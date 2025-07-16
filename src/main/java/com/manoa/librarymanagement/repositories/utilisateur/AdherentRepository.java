package com.manoa.librarymanagement.repositories.utilisateur;

import com.manoa.librarymanagement.models.utilisateur.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdherentRepository extends JpaRepository<Adherent, Long> {
    Adherent findByEmail(String name);
}