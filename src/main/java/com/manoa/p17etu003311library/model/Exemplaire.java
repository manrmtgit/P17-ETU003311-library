package com.manoa.p17etu003311library.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "exemplaire")
public class Exemplaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "livre_id", nullable = false)
    private Livre livre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutExemplaire statut;

    private String localisation;

    @Column(nullable = false)
    private LocalDate dateAcquisition;

    private Double prix;

    public enum StatutExemplaire {
        DISPONIBLE, PRETE, RESERVE, EN_REPARATION
    }
}