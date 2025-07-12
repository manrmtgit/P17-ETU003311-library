package com.manoa.librarymanagement.repositories.livre;


import com.manoa.librarymanagement.models.livre.Livre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivreRepository extends JpaRepository<Livre, Long> {}
