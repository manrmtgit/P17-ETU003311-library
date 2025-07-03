package com.manoa.p17etu003311library.repository;

import com.manoa.p17etu003311library.model.PretStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PretStatutRepository extends JpaRepository<PretStatut, Long> {
}