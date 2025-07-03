package com.manoa.p17etu003311library.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "penalite_statut")
public class PenaliteStatut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "penalite_id", nullable = false)
    private Penalite penalite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPenalite statut;

    @Column(nullable = false)
    private LocalDateTime dateChangement;

    public enum StatutPenalite {
        IMPAYEE, PAYEE
    }
}