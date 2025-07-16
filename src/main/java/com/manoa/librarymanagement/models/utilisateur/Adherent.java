package com.manoa.librarymanagement.models.utilisateur;

import com.manoa.librarymanagement.models.action.Abonnement;
import com.manoa.librarymanagement.models.action.Penalite;
import com.manoa.librarymanagement.models.action.Pret;
import com.manoa.librarymanagement.models.action.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adherent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;

    private LocalDate dateNaissance;

    @ManyToOne
    private Profil profil;

    @OneToMany(mappedBy = "adherent")
    private List<Abonnement> abonnements;

    @OneToMany(mappedBy = "adherent")
    private List<Pret> prets;

    @OneToMany(mappedBy = "adherent")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "adherent")
    private List<Penalite> penalites;
}