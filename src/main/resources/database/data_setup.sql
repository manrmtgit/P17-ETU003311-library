\c postgres;
DROP DATABASE IF EXISTS library_management;
CREATE DATABASE library_management;
\c library_management;


CREATE TABLE livre
(
    id               BIGSERIAL PRIMARY KEY,
    titre            VARCHAR(255) NOT NULL,
    auteur           VARCHAR(255) NOT NULL,
    date_publication DATE,
    categorie        VARCHAR(100),
    description      TEXT
);

CREATE TABLE exemplaire
(
    id               BIGSERIAL PRIMARY KEY,
    livre_id         BIGINT NOT NULL,
    localisation     VARCHAR(100),
    date_acquisition DATE   NOT NULL,
    prix             DECIMAL(10, 2),
    FOREIGN KEY (livre_id) REFERENCES livre (id)
);

CREATE TABLE adherent
(
    id                  BIGSERIAL PRIMARY KEY,
    nom                 VARCHAR(100)        NOT NULL,
    prenom              VARCHAR(100)        NOT NULL,
    email               VARCHAR(255) UNIQUE NOT NULL,
    telephone           VARCHAR(20),
    adresse             TEXT,
    date_inscription    DATE                NOT NULL,
    date_fin_abonnement DATE                NOT NULL,
    type                VARCHAR(50)         NOT NULL CHECK (type IN ('ETUDIANT', 'PERSONNEL', 'PROFESSEUR', 'ANONYME'))
);

CREATE TABLE pret
(
    id                    BIGSERIAL PRIMARY KEY,
    exemplaire_id         BIGINT      NOT NULL,
    adherent_id           BIGINT      NOT NULL,
    date_debut            DATE        NOT NULL,
    date_fin_prevue       DATE        NOT NULL,
    date_retour_effective DATE,
    type                  VARCHAR(50) NOT NULL CHECK (type IN ('DOMICILE', 'SUR_PLACE')),
    FOREIGN KEY (exemplaire_id) REFERENCES exemplaire (id),
    FOREIGN KEY (adherent_id) REFERENCES adherent (id)
);

CREATE TABLE reservation
(
    id              BIGSERIAL PRIMARY KEY,
    livre_id        BIGINT NOT NULL,
    adherent_id     BIGINT NOT NULL,
    date_demande    DATE   NOT NULL,
    date_expiration DATE,
    FOREIGN KEY (livre_id) REFERENCES livre (id),
    FOREIGN KEY (adherent_id) REFERENCES adherent (id)
);

CREATE TABLE penalite
(
    id            BIGSERIAL PRIMARY KEY,
    pret_id       BIGINT         NOT NULL,
    montant       DECIMAL(10, 2) NOT NULL,
    date_emission DATE           NOT NULL,
    date_paiement DATE,
    FOREIGN KEY (pret_id) REFERENCES pret (id)
);

CREATE TABLE jour_ferie
(
    date        DATE PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE exemplaire_statut
(
    id              BIGSERIAL PRIMARY KEY,
    exemplaire_id   BIGINT      NOT NULL,
    statut          VARCHAR(50) NOT NULL CHECK (statut IN ('DISPONIBLE', 'PRETE', 'RESERVE', 'EN_REPARATION')),
    date_changement TIMESTAMP   NOT NULL,
    FOREIGN KEY (exemplaire_id) REFERENCES exemplaire (id)
);

CREATE TABLE adherent_statut
(
    id              BIGSERIAL PRIMARY KEY,
    adherent_id     BIGINT      NOT NULL,
    statut          VARCHAR(50) NOT NULL CHECK (statut IN ('ACTIF', 'SUSPENDU')),
    date_changement TIMESTAMP   NOT NULL,
    FOREIGN KEY (adherent_id) REFERENCES adherent (id)
);

CREATE TABLE pret_statut
(
    id              BIGSERIAL PRIMARY KEY,
    pret_id         BIGINT      NOT NULL,
    statut          VARCHAR(50) NOT NULL CHECK (statut IN ('EN_COURS', 'RETOURNE', 'EN_RETARD')),
    date_changement TIMESTAMP   NOT NULL,
    FOREIGN KEY (pret_id) REFERENCES pret (id)
);

CREATE TABLE reservation_statut
(
    id              BIGSERIAL PRIMARY KEY,
    reservation_id  BIGINT      NOT NULL,
    statut          VARCHAR(50) NOT NULL CHECK (statut IN ('EN_ATTENTE', 'ANNULEE', 'HONOREE')),
    date_changement TIMESTAMP   NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation (id)
);

CREATE TABLE penalite_statut
(
    id              BIGSERIAL PRIMARY KEY,
    penalite_id     BIGINT      NOT NULL,
    statut          VARCHAR(50) NOT NULL CHECK (statut IN ('IMPAYEE', 'PAYEE')),
    date_changement TIMESTAMP   NOT NULL,
    FOREIGN KEY (penalite_id) REFERENCES penalite (id)
);