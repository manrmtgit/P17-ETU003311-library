package com.manoa.librarymanagement.models.action;

import com.manoa.librarymanagement.models.utilisateur.Adherent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Adherent adherent;

    private LocalDate dateDebut;
    private LocalDate dateFin;
}