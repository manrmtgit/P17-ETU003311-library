package com.manoa.librarymanagement.repositories.action;


import com.manoa.librarymanagement.models.action.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {}