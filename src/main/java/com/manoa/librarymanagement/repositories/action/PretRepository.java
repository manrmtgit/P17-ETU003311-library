package com.manoa.librarymanagement.repositories.action;

import com.manoa.librarymanagement.models.action.Pret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PretRepository extends JpaRepository<Pret, Long> {
    long countByAdherentIdAndDateRetourEffectiveIsNull(Long id);

    List<Pret> findByAdherentId(Long id);
}