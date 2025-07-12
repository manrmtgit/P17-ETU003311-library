package com.manoa.librarymanagement.repositories.utilisateur;

import com.manoa.librarymanagement.models.utilisateur.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilRepository extends JpaRepository<Profil, Long> {}