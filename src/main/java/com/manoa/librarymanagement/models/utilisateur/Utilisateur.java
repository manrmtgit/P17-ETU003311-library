package com.manoa.librarymanagement.models.utilisateur;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Adherent adherent;

    @Column(unique = true)
    private String username;

    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean actif;

    public enum Role {BIBLIOTHECAIRE, ADHERENT}
}