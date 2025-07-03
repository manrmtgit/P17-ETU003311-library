package com.manoa.p17etu003311library.repository;

import com.manoa.p17etu003311library.model.PenaliteStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaliteStatutRepository extends JpaRepository<PenaliteStatut, Long> {
}