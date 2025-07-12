package com.manoa.librarymanagement.models.action;

import com.manoa.librarymanagement.models.livre.Exemplaire;
import com.manoa.librarymanagement.models.utilisateur.Adherent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exemplaire exemplaire;

    @ManyToOne
    private Adherent adherent;

    private LocalDate datePret;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;

    @Enumerated(EnumType.STRING)
    private TypePret typePret;

    @OneToOne(mappedBy = "pret")
    private Prolongement prolongement;

    public enum TypePret {SUR_PLACE, EMPORTE}
}