package com.manoa.p17etu003311library.repository;

import com.manoa.p17etu003311library.model.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, LocalDate> {
}