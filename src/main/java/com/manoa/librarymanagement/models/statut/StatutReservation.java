package com.manoa.librarymanagement.models.statut;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatutReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DescriptionStatut statut;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    public enum DescriptionStatut {
        EN_ATTENTE, ACCEPTEE, REFUSEE, ANNULEE
    }
}
