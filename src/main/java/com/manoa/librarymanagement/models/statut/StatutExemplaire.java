package com.manoa.librarymanagement.models.statut;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatutExemplaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DescriptionStatut description;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    public enum DescriptionStatut {DISPONIBLE, PRETE, RESERVE}
}
