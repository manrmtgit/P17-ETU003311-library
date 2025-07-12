package com.manoa.librarymanagement.models.action;

import com.manoa.librarymanagement.models.livre.Exemplaire;
import com.manoa.librarymanagement.models.statut.StatutReservation;
import com.manoa.librarymanagement.models.utilisateur.Adherent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exemplaire exemplaire;

    @ManyToOne
    private Adherent adherent;

    @ManyToOne
    private StatutReservation statut;

    private LocalDate dateReservation;
    private LocalDate dateConversionPret;
}