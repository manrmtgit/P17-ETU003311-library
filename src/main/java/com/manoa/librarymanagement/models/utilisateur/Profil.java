package com.manoa.librarymanagement.models.utilisateur;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profil {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int quotaPret;
    private int quotaReservation;
    private int joursPenalite;

    @OneToMany(mappedBy = "profil")
    private List<Adherent> adherents;
}