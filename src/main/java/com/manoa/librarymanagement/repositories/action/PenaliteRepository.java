package com.manoa.librarymanagement.repositories.action;

import com.manoa.librarymanagement.models.action.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PenaliteRepository extends JpaRepository<Penalite, Long> {
    boolean existsByAdherentIdAndDateFinAfter(Long id, LocalDate datePret);
}