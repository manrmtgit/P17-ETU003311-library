\c postgres;
DROP DATABASE IF EXISTS library_management;
CREATE DATABASE library_management;
\c library_management;

-- TABLE categorie livre
CREATE TABLE categorie_livre
(
    id  SERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL
);

-- TABLE livre
CREATE TABLE livre
(
    id             SERIAL PRIMARY KEY,
    categorie_id   INT REFERENCES categorie_livre (id) ON DELETE CASCADE,
    titre          VARCHAR(255) NOT NULL,
    auteur         VARCHAR(255),
    annee_parution INT,
    age_minimum    INT DEFAULT 0
);

-- TABLE statut exemplaire
CREATE TABLE statut_exemplaire
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR(50) CHECK ( description IN ('DISPONIBLE', 'PRETE', 'RESERVE')),
    date_debut  DATE NOT NULL,
    date_fin    DATE NOT NULL
);

-- TABLE exemplaire
CREATE TABLE exemplaire
(
    id               SERIAL PRIMARY KEY,
    livre_id         INT REFERENCES livre (id) ON DELETE CASCADE,
    statut_id        INT REFERENCES statut_exemplaire (id) ON DELETE CASCADE,
    code_barre       VARCHAR(100) UNIQUE NOT NULL,
    date_acquisition DATE    DEFAULT CURRENT_DATE,
    disponible       BOOLEAN DEFAULT TRUE
);

-- TABLE profil
CREATE TABLE profil
(
    id                SERIAL PRIMARY KEY,
    nom               VARCHAR(100) NOT NULL,
    quota_pret        INT          NOT NULL,
    quota_reservation INT          NOT NULL,
    jours_penalite    INT          NOT NULL
);

-- TABLE adherent
CREATE TABLE adherent
(
    id             SERIAL PRIMARY KEY,
    nom            VARCHAR(100)        NOT NULL,
    prenom         VARCHAR(100),
    email          VARCHAR(100) UNIQUE NOT NULL,
    date_naissance DATE                NOT NULL,
    profil_id      INT REFERENCES profil (id),
    utilisateur_id INT UNIQUE,
    CONSTRAINT age_check CHECK (date_naissance < CURRENT_DATE)
);

-- TABLE abonnement
CREATE TABLE abonnement
(
    id          SERIAL PRIMARY KEY,
    adherent_id INT REFERENCES adherent (id) ON DELETE CASCADE,
    date_debut  DATE NOT NULL,
    date_fin    DATE NOT NULL,
    CONSTRAINT valid_abonnement CHECK (date_fin > date_debut)
);

-- TABLE utilisateur
CREATE TABLE utilisateur
(
    id           SERIAL PRIMARY KEY,
    adherent_id  INT REFERENCES adherent (id),
    username     VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe TEXT                NOT NULL,
    role         VARCHAR(50) CHECK (role IN ('BIBLIOTHECAIRE', 'ADHERENT')),
    actif        BOOLEAN DEFAULT TRUE
);

-- TABLE pret
CREATE TABLE pret
(
    id                    SERIAL PRIMARY KEY,
    exemplaire_id         INT REFERENCES exemplaire (id),
    adherent_id           INT REFERENCES adherent (id),
    date_pret             DATE        NOT NULL,
    date_retour_prevue    DATE        NOT NULL,
    date_retour_effective DATE,
    type_pret             VARCHAR(50) NOT NULL CHECK ( type_pret IN ('SUR_PLACE', 'EMPORTE')),
    CONSTRAINT unique_pret_exemplaire CHECK (date_retour_effective IS NULL)
);

-- TABLE prolongement
CREATE TABLE prolongement
(
    id                   SERIAL PRIMARY KEY,
    pret_id              INT UNIQUE REFERENCES pret (id),
    date_prolongement    DATE NOT NULL,
    nouvelle_date_retour DATE NOT NULL
);

-- TABLE statut reservation
CREATE TABLE statut_reservation
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR(50) NOT NULL CHECK ( description IN ('EN_ATTENTE', 'ACCEPTEE', 'REFUSEE', 'ANNULEE')),
    date_debut  DATE        NOT NULL,
    date_fin    DATE        NOT NULL
);

-- TABLE reservation
CREATE TABLE reservation
(
    id                   SERIAL PRIMARY KEY,
    exemplaire_id        INT REFERENCES exemplaire (id),
    adherent_id          INT REFERENCES adherent (id),
    statut_id            INT REFERENCES statut_reservation (id),
    date_reservation     DATE NOT NULL,
    date_conversion_pret DATE
);

-- TABLE penalite
CREATE TABLE penalite
(
    id          SERIAL PRIMARY KEY,
    adherent_id INT REFERENCES adherent (id),
    date_debut  DATE    NOT NULL,
    date_fin    DATE    NOT NULL,
    motif       TEXT,
    reglee      BOOLEAN NOT NULL DEFAULT FALSE
);

-- TABLE jour_ferie
CREATE TABLE jour_ferie
(
    id          SERIAL PRIMARY KEY,
    date        DATE UNIQUE NOT NULL,
    description TEXT
);
