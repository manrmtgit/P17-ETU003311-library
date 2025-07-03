package com.manoa.p17etu003311library.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pret_statut")
public class PretStatut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pret_id", nullable = false)
    private Pret pret;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPret statut;

    @Column(nullable = false)
    private LocalDateTime dateChangement;

    public enum StatutPret {
        EN_COURS, RETOURNE, EN_RETARD
    }
}