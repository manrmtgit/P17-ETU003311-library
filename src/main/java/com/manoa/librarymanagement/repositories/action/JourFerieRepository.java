package com.manoa.librarymanagement.repositories.action;

import com.manoa.librarymanagement.models.action.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {
    boolean existsByDate(LocalDate datePret);
}