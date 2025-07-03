package com.manoa.p17etu003311library.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "penalite")
public class Penalite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pret_id", nullable = false)
    private Pret pret;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private LocalDate dateEmission;

    private LocalDate datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPenalite statut;

    public enum StatutPenalite {
        IMPAYEE, PAYEE
    }
}