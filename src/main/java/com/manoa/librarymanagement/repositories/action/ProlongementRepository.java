package com.manoa.librarymanagement.repositories.action;

import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.models.action.Prolongement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProlongementRepository extends JpaRepository<Prolongement, Long> {
    Optional<Object> findByPret(Pret pret);
}