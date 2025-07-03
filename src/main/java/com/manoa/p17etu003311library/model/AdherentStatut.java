package com.manoa.p17etu003311library.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "adherent_statut")
public class AdherentStatut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adherent_id", nullable = false)
    private Adherent adherent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAdherent statut;

    @Column(nullable = false)
    private LocalDateTime dateChangement;

    public enum StatutAdherent {
        ACTIF, SUSPENDU
    }
}