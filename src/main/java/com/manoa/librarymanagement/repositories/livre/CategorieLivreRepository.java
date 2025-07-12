package com.manoa.librarymanagement.repositories.livre;

import com.manoa.librarymanagement.models.livre.CategorieLivre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieLivreRepository extends JpaRepository<CategorieLivre, Long> {
}