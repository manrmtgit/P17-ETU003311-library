package com.manoa.librarymanagement.repositories.action;


import com.manoa.librarymanagement.models.action.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    boolean existsByAdherentIdAndDateDebutBeforeAndDateFinAfter(Long id, LocalDate datePret, LocalDate dateRetourPrevue);
}