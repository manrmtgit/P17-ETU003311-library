package com.manoa.librarymanagement.repositories.livre;

import com.manoa.librarymanagement.models.livre.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
}
