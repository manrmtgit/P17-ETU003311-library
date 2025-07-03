package com.manoa.p17etu003311library.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "adherent")
public class Adherent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    @Column(columnDefinition = "TEXT")
    private String adresse;

    @Column(nullable = false)
    private LocalDate dateInscription;

    @Column(nullable = false)
    private LocalDate dateFinAbonnement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAdherent type;

    public enum TypeAdherent {
        ETUDIANT, PERSONNEL, PROFESSEUR, ANONYME
    }
}