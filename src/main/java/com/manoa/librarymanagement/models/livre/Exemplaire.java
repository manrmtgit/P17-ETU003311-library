package com.manoa.librarymanagement.models.livre;

import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.models.action.Reservation;
import com.manoa.librarymanagement.models.statut.StatutExemplaire;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exemplaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Livre livre;

    @ManyToOne
    private StatutExemplaire statut;

    @Column(unique = true)
    private String codeBarre;

    private LocalDate dateAcquisition;
    private Boolean disponible;

    @OneToMany(mappedBy = "exemplaire")
    private List<Pret> prets;

    @OneToMany(mappedBy = "exemplaire")
    private List<Reservation> reservations;
}
