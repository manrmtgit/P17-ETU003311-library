package com.manoa.librarymanagement.repositories.action;


import com.manoa.librarymanagement.models.action.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
}