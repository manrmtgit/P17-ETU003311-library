package com.manoa.librarymanagement.repositories.action;

import com.manoa.librarymanagement.models.action.Pret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PretRepository extends JpaRepository<Pret, Long> {}