-- Categories de livre
INSERT INTO categorie_livre (nom)
VALUES ('Roman'),
       ('Science'),
       ('Histoire');

-- Livres
INSERT INTO livre (categorie_id, titre, auteur, annee_parution, age_minimum)
VALUES (1, 'Le petit prince', 'Antoine de Saint-Exupery', 1943, 8),
       (2, 'Introduction a la physique', 'Marie Curie', 1923, 16),
       (3, 'Histoire du monde', 'Paul Durant', 2001, 12);

-- Profils
INSERT INTO profil (nom, quota_pret, quota_reservation, jours_penalite)
VALUES ('Etudiant', 2, 1, 3),
       ('Professeur', 5, 3, 5),
       ('Personnel', 10, 5, 2);

-- Statuts exemplaire
INSERT INTO statut_exemplaire (description)
VALUES ('DISPONIBLE');

-- Exemplaires
INSERT INTO exemplaire (livre_id, statut_id, code_barre)
VALUES (1, 1, 'EX001-LPP'),
       (2, 1, 'EX002-PHY'),
       (3, 1, 'EX003-HIS');
/*
-- Adherents
INSERT INTO adherent (nom, prenom, email, date_naissance, profil_id)
VALUES ('Doe', 'John', 'john.doe@email.com', '2000-05-12', 2),
       ('Smith', 'Anna', 'anna.smith@email.com', '2010-04-01', 1);

-- Abonnements
INSERT INTO abonnement (adherent_id, date_debut, date_fin)
VALUES (1, '2025-01-01', '2025-12-31'),
       (2, '2025-06-01', '2025-12-31');

-- Utilisateurs
INSERT INTO utilisateur (adherent_id, username, mot_de_passe, role)
VALUES (1, 'johndoe', 'password1', 'ADHERENT'),
       (2, 'annasmith', 'password2', 'ADHERENT');

-- Prets
INSERT INTO pret (exemplaire_id, adherent_id, date_pret, date_retour_prevue, type_pret)
VALUES (1, 1, '2025-07-01', '2025-07-15', 'EMPORTE');

-- Prolongement
INSERT INTO prolongement (pret_id, date_prolongement, nouvelle_date_retour)
VALUES (1, '2025-07-10', '2025-07-22');

-- Statuts reservation
INSERT INTO statut_reservation (description, date_debut, date_fin)
VALUES ('EN_ATTENTE', '2025-01-01', '2025-12-31'),
       ('ACCEPTEE', '2025-01-01', '2025-12-31'),
       ('REFUSEE', '2025-01-01', '2025-12-31'),
       ('ANNULEE', '2025-01-01', '2025-12-31');

-- Reservation
INSERT INTO reservation (exemplaire_id, adherent_id, statut_id, date_reservation)
VALUES (2, 2, 1, '2025-07-05');

-- Penalite
INSERT INTO penalite (adherent_id, date_debut, date_fin, motif)
VALUES (1, '2025-07-16', '2025-07-20', 'retard de retour');
*/
-- Jour ferie
INSERT INTO jour_ferie (date, description)
VALUES ('2025-12-25', 'Noel'),
       ('2025-01-01', 'Jour de l an');
