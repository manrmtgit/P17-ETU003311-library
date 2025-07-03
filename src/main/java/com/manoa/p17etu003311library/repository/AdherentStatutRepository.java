package com.manoa.p17etu003311library.repository;

import com.manoa.p17etu003311library.model.AdherentStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdherentStatutRepository extends JpaRepository<AdherentStatut, Long> {
}