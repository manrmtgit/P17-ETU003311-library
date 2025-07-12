package com.manoa.librarymanagement.models.livre;


import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CategorieLivre categorie;

    private String titre;
    private String auteur;
    private Integer anneeParution;
    private Integer ageMinimum;

    @OneToMany(mappedBy = "livre")
    private List<Exemplaire> exemplaires;
}
