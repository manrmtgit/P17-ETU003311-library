package com.manoa.p17etu003311library.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "exemplaire_statut")
public class ExemplaireStatut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exemplaire_id", nullable = false)
    private Exemplaire exemplaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutExemplaire statut;

    @Column(nullable = false)
    private LocalDateTime dateChangement;

    public enum StatutExemplaire {
        DISPONIBLE, PRETE, RESERVE, EN_REPARATION
    }
}