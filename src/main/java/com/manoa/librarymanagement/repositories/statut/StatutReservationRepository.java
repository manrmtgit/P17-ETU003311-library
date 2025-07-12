package com.manoa.librarymanagement.repositories.statut;

import com.manoa.librarymanagement.models.statut.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatutReservationRepository extends JpaRepository<StatutReservation, Long> {}