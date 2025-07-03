package com.manoa.p17etu003311library.repository;


import com.manoa.p17etu003311library.model.Pret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PretRepository extends JpaRepository<Pret, Long> {
}