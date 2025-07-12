package com.manoa.librarymanagement.models.action;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prolongement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Pret pret;

    private LocalDate dateProlongement;
    private LocalDate nouvelleDateRetour;
}