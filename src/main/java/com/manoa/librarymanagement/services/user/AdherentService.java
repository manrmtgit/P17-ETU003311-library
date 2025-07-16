package com.manoa.librarymanagement.services.user;

import com.manoa.librarymanagement.models.utilisateur.Adherent;
import com.manoa.librarymanagement.models.utilisateur.Profil;
import com.manoa.librarymanagement.models.action.Abonnement;
import com.manoa.librarymanagement.repositories.utilisateur.AdherentRepository;
import com.manoa.librarymanagement.repositories.utilisateur.ProfilRepository;
import com.manoa.librarymanagement.repositories.action.AbonnementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdherentService {
    private final AdherentRepository adherentRepository;
    private final ProfilRepository profilRepository;
    private final AbonnementRepository abonnementRepository;

    public Adherent ajouterAdherent(Adherent adherent, Long profilId, LocalDate debut, LocalDate fin) {
        Profil profil = profilRepository.findById(profilId).orElseThrow();
        adherent.setProfil(profil);
        adherentRepository.save(adherent);

        Abonnement abonnement = Abonnement.builder()
                .adherent(adherent)
                .dateDebut(debut)
                .dateFin(fin)
                .build();
        abonnementRepository.save(abonnement);

        return adherent;
    }
}