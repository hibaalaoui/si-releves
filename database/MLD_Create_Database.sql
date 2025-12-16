-- =====================================================
-- MODÈLE LOGIQUE DE DONNÉES (MLD)
-- Système de Gestion des Relevés de Compteurs
-- SI Relevés - RABAT ENERGIE & EAU (REE)
-- =====================================================
-- Généré automatiquement par IA - Claude (Anthropic)
-- Date : 16 Décembre 2024
-- Base de données : MySQL 8.0+
-- =====================================================

-- Suppression de la base si elle existe (développement uniquement)
DROP DATABASE IF EXISTS si_releves;

-- Création de la base de données
CREATE DATABASE si_releves
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE si_releves;

-- =====================================================
-- TABLES
-- =====================================================

-- -----------------------------------------------------
-- Table : quartier
-- Description : Zones géographiques de Rabat
-- -----------------------------------------------------
CREATE TABLE quartier (
    id_quartier INT AUTO_INCREMENT,
    nom_quartier VARCHAR(100) NOT NULL,
    ville VARCHAR(100) NOT NULL DEFAULT 'Rabat',
    PRIMARY KEY (id_quartier),
    UNIQUE KEY uk_quartier_nom (nom_quartier)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Quartiers de la ville de Rabat';

-- -----------------------------------------------------
-- Table : client
-- Description : Clients REE (synchronisés depuis SI Commercial)
-- -----------------------------------------------------
CREATE TABLE client (
    id_client VARCHAR(50) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_creation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_client)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Clients REE abonnés eau et électricité';

-- -----------------------------------------------------
-- Table : adresse
-- Description : Adresses physiques des clients
-- -----------------------------------------------------
CREATE TABLE adresse (
    id_adresse INT AUTO_INCREMENT,
    id_client VARCHAR(50) NOT NULL,
    id_quartier INT NOT NULL,
    adresse_complete TEXT NOT NULL,
    type_bien ENUM('Standard', 'Immeuble') NOT NULL DEFAULT 'Standard',
    code_postal VARCHAR(10) DEFAULT NULL,
    date_creation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_adresse),
    KEY idx_adresse_client (id_client),
    KEY idx_adresse_quartier (id_quartier),
    CONSTRAINT fk_adresse_client 
        FOREIGN KEY (id_client) REFERENCES client(id_client)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_adresse_quartier 
        FOREIGN KEY (id_quartier) REFERENCES quartier(id_quartier)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Adresses des clients avec localisation par quartier';

-- -----------------------------------------------------
-- Table : agent
-- Description : Agents de terrain (synchronisés depuis SI RH)
-- -----------------------------------------------------
CREATE TABLE agent (
    id_agent VARCHAR(50) NOT NULL,
    id_quartier INT NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    tel_personnel VARCHAR(20) DEFAULT NULL,
    tel_professionnel VARCHAR(20) NOT NULL,
    date_affectation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_agent),
    KEY idx_agent_quartier (id_quartier),
    KEY idx_agent_actif (actif),
    CONSTRAINT fk_agent_quartier 
        FOREIGN KEY (id_quartier) REFERENCES quartier(id_quartier)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Agents de terrain effectuant les relevés';

-- -----------------------------------------------------
-- Table : compteur
-- Description : Compteurs d'eau et d'électricité
-- -----------------------------------------------------
CREATE TABLE compteur (
    id_compteur CHAR(9) NOT NULL,
    id_adresse INT NOT NULL,
    type ENUM('Eau', 'Electricite') NOT NULL,
    index_actuel DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    date_installation DATE NOT NULL DEFAULT (CURRENT_DATE),
    date_derniere_releve DATETIME DEFAULT NULL,
    pour_espaces_communs BOOLEAN NOT NULL DEFAULT FALSE,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_compteur),
    KEY idx_compteur_adresse (id_adresse),
    KEY idx_compteur_type (type),
    KEY idx_compteur_actif (actif),
    UNIQUE KEY uk_compteur_adresse_type (id_adresse, type, pour_espaces_communs),
    CONSTRAINT fk_compteur_adresse 
        FOREIGN KEY (id_adresse) REFERENCES adresse(id_adresse)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_compteur_type 
        CHECK (type IN ('Eau', 'Electricite')),
    CONSTRAINT chk_compteur_index 
        CHECK (index_actuel >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Compteurs eau et électricité installés aux adresses';

-- -----------------------------------------------------
-- Table : releve
-- Description : Relevés de consommation effectués
-- -----------------------------------------------------
CREATE TABLE releve (
    id_releve INT AUTO_INCREMENT,
    id_compteur CHAR(9) NOT NULL,
    id_agent VARCHAR(50) NOT NULL,
    date_releve DATETIME NOT NULL,
    ancien_index DECIMAL(10,2) NOT NULL,
    nouvel_index DECIMAL(10,2) NOT NULL,
    consommation DECIMAL(10,2) NOT NULL,
    unite ENUM('m3', 'kWh') NOT NULL,
    envoye_facturation BOOLEAN NOT NULL DEFAULT FALSE,
    date_envoi_facturation DATETIME DEFAULT NULL,
    PRIMARY KEY (id_releve),
    KEY idx_releve_compteur (id_compteur),
    KEY idx_releve_agent (id_agent),
    KEY idx_releve_date (date_releve),
    KEY idx_releve_envoye_facturation (envoye_facturation),
    KEY idx_releve_compteur_date (id_compteur, date_releve DESC),
    CONSTRAINT fk_releve_compteur 
        FOREIGN KEY (id_compteur) REFERENCES compteur(id_compteur)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_releve_agent 
        FOREIGN KEY (id_agent) REFERENCES agent(id_agent)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_releve_index 
        CHECK (nouvel_index >= ancien_index),
    CONSTRAINT chk_releve_consommation 
        CHECK (consommation >= 0),
    CONSTRAINT chk_releve_unite 
        CHECK (unite IN ('m3', 'kWh'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Relevés de consommation effectués par les agents';

-- -----------------------------------------------------
-- Table : utilisateur_backoffice
-- Description : Comptes utilisateurs du backoffice
-- -----------------------------------------------------
CREATE TABLE utilisateur_backoffice (
    id_utilisateur INT AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('Superadmin', 'Utilisateur') NOT NULL DEFAULT 'Utilisateur',
    premiere_connexion BOOLEAN NOT NULL DEFAULT TRUE,
    date_ajout DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    actif BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_utilisateur),
    UNIQUE KEY uk_user_email (email),
    KEY idx_user_role (role),
    KEY idx_user_actif (actif),
    CONSTRAINT chk_user_role 
        CHECK (role IN ('Superadmin', 'Utilisateur'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Utilisateurs du système backoffice SI Relevés';

-- =====================================================
-- TRIGGERS
-- =====================================================

-- -----------------------------------------------------
-- Trigger : Vérification du nombre maximum de compteurs par adresse
-- -----------------------------------------------------
DELIMITER //

CREATE TRIGGER trg_check_compteurs_max_per_adresse
BEFORE INSERT ON compteur
FOR EACH ROW
BEGIN
    DECLARE nb_compteurs_standard INT DEFAULT 0;
    DECLARE nb_compteurs_espaces_communs INT DEFAULT 0;
    DECLARE type_bien_adresse VARCHAR(20);
    
    -- Récupérer le type de bien de l'adresse
    SELECT type_bien INTO type_bien_adresse 
    FROM adresse 
    WHERE id_adresse = NEW.id_adresse;
    
    -- Compter les compteurs existants (standards)
    SELECT COUNT(*) INTO nb_compteurs_standard
    FROM compteur
    WHERE id_adresse = NEW.id_adresse 
    AND pour_espaces_communs = FALSE;
    
    -- Compter les compteurs pour espaces communs
    SELECT COUNT(*) INTO nb_compteurs_espaces_communs
    FROM compteur
    WHERE id_adresse = NEW.id_adresse 
    AND pour_espaces_communs = TRUE;
    
    -- Vérifications selon le type de compteur
    IF NEW.pour_espaces_communs = FALSE THEN
        -- Compteur standard
        IF nb_compteurs_standard >= 2 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Maximum 2 compteurs standards par adresse atteint';
        END IF;
    ELSE
        -- Compteur pour espaces communs
        IF type_bien_adresse = 'Standard' THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Les compteurs pour espaces communs sont réservés aux immeubles';
        END IF;
        IF nb_compteurs_espaces_communs >= 2 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Maximum 2 compteurs pour espaces communs par immeuble atteint';
        END IF;
    END IF;
END//

DELIMITER ;

-- -----------------------------------------------------
-- Trigger : Mise à jour de l'index du compteur après relevé
-- -----------------------------------------------------
DELIMITER //

CREATE TRIGGER trg_update_compteur_after_releve
AFTER INSERT ON releve
FOR EACH ROW
BEGIN
    UPDATE compteur
    SET index_actuel = NEW.nouvel_index,
        date_derniere_releve = NEW.date_releve
    WHERE id_compteur = NEW.id_compteur;
END//

DELIMITER ;

-- -----------------------------------------------------
-- Trigger : Calcul automatique de la consommation
-- -----------------------------------------------------
DELIMITER //

CREATE TRIGGER trg_calculate_consommation
BEFORE INSERT ON releve
FOR EACH ROW
BEGIN
    DECLARE compteur_type VARCHAR(20);
    
    -- Récupérer le type de compteur
    SELECT type INTO compteur_type
    FROM compteur
    WHERE id_compteur = NEW.id_compteur;
    
    -- Calculer la consommation
    SET NEW.consommation = NEW.nouvel_index - NEW.ancien_index;
    
    -- Définir l'unité selon le type de compteur
    IF compteur_type = 'Eau' THEN
        SET NEW.unite = 'm3';
    ELSE
        SET NEW.unite = 'kWh';
    END IF;
END//

DELIMITER ;

-- -----------------------------------------------------
-- Trigger : Récupération automatique de l'ancien index
-- -----------------------------------------------------
DELIMITER //

CREATE TRIGGER trg_set_ancien_index
BEFORE INSERT ON releve
FOR EACH ROW
BEGIN
    DECLARE ancien_index_compteur DECIMAL(10,2);
    
    -- Récupérer l'index actuel du compteur
    SELECT index_actuel INTO ancien_index_compteur
    FROM compteur
    WHERE id_compteur = NEW.id_compteur;
    
    -- Si ancien_index n'est pas fourni, utiliser l'index actuel du compteur
    IF NEW.ancien_index IS NULL OR NEW.ancien_index = 0 THEN
        SET NEW.ancien_index = ancien_index_compteur;
    END IF;
END//

DELIMITER ;

-- =====================================================
-- PROCÉDURES STOCKÉES
-- =====================================================

-- -----------------------------------------------------
-- Procédure : Générer un nouvel ID de compteur
-- Format : 9 chiffres avec zéros précédents (ex: 000000001)
-- -----------------------------------------------------
DELIMITER //

CREATE PROCEDURE sp_generate_compteur_id(OUT new_id CHAR(9))
BEGIN
    DECLARE max_id INT DEFAULT 0;
    
    -- Récupérer le dernier ID numérique
    SELECT CAST(MAX(id_compteur) AS UNSIGNED) INTO max_id
    FROM compteur;
    
    -- Incrémenter
    SET max_id = COALESCE(max_id, 0) + 1;
    
    -- Formater avec des zéros précédents
    SET new_id = LPAD(max_id, 9, '0');
END//

DELIMITER ;

-- -----------------------------------------------------
-- Procédure : Calculer le taux de couverture des relevés
-- Formule : (Nombre de compteurs relevés / Nombre total de compteurs) × 100
-- -----------------------------------------------------
DELIMITER //

CREATE PROCEDURE sp_calculate_taux_couverture(
    IN p_date_debut DATE,
    IN p_date_fin DATE,
    IN p_id_quartier INT,
    OUT p_taux_couverture DECIMAL(5,2)
)
BEGIN
    DECLARE nb_total_compteurs INT DEFAULT 0;
    DECLARE nb_compteurs_releves INT DEFAULT 0;
    
    -- Compter le nombre total de compteurs actifs
    IF p_id_quartier IS NULL THEN
        SELECT COUNT(*) INTO nb_total_compteurs
        FROM compteur
        WHERE actif = TRUE;
    ELSE
        SELECT COUNT(*) INTO nb_total_compteurs
        FROM compteur c
        INNER JOIN adresse a ON c.id_adresse = a.id_adresse
        WHERE c.actif = TRUE AND a.id_quartier = p_id_quartier;
    END IF;
    
    -- Compter le nombre de compteurs relevés dans la période
    IF p_id_quartier IS NULL THEN
        SELECT COUNT(DISTINCT id_compteur) INTO nb_compteurs_releves
        FROM releve
        WHERE date_releve BETWEEN p_date_debut AND p_date_fin;
    ELSE
        SELECT COUNT(DISTINCT r.id_compteur) INTO nb_compteurs_releves
        FROM releve r
        INNER JOIN compteur c ON r.id_compteur = c.id_compteur
        INNER JOIN adresse a ON c.id_adresse = a.id_adresse
        WHERE r.date_releve BETWEEN p_date_debut AND p_date_fin
        AND a.id_quartier = p_id_quartier;
    END IF;
    
    -- Calculer le taux de couverture
    IF nb_total_compteurs > 0 THEN
        SET p_taux_couverture = (nb_compteurs_releves / nb_total_compteurs) * 100;
    ELSE
        SET p_taux_couverture = 0;
    END IF;
END//

DELIMITER ;

-- -----------------------------------------------------
-- Procédure : Calculer les relevés moyens par jour par agent
-- -----------------------------------------------------
DELIMITER //

CREATE PROCEDURE sp_calculate_releves_par_jour_agent(
    IN p_id_agent VARCHAR(50),
    IN p_date_debut DATE,
    IN p_date_fin DATE,
    OUT p_moyenne DECIMAL(10,2)
)
BEGIN
    DECLARE nb_releves INT DEFAULT 0;
    DECLARE nb_jours INT DEFAULT 0;
    
    -- Compter le nombre de relevés
    SELECT COUNT(*) INTO nb_releves
    FROM releve
    WHERE id_agent = p_id_agent
    AND DATE(date_releve) BETWEEN p_date_debut AND p_date_fin;
    
    -- Calculer le nombre de jours
    SET nb_jours = DATEDIFF(p_date_fin, p_date_debut) + 1;
    
    -- Calculer la moyenne
    IF nb_jours > 0 THEN
        SET p_moyenne = nb_releves / nb_jours;
    ELSE
        SET p_moyenne = 0;
    END IF;
END//

DELIMITER ;

-- =====================================================
-- VUES
-- =====================================================

-- -----------------------------------------------------
-- Vue : Vue complète des relevés avec informations détaillées
-- -----------------------------------------------------
CREATE VIEW v_releves_detail AS
SELECT 
    r.id_releve,
    r.date_releve,
    r.ancien_index,
    r.nouvel_index,
    r.consommation,
    r.unite,
    r.envoye_facturation,
    r.date_envoi_facturation,
    c.id_compteur,
    c.type AS type_compteur,
    c.pour_espaces_communs,
    a.id_agent,
    a.nom AS agent_nom,
    a.prenom AS agent_prenom,
    addr.id_adresse,
    addr.adresse_complete,
    addr.type_bien,
    q.id_quartier,
    q.nom_quartier,
    cl.id_client,
    cl.nom AS client_nom,
    cl.prenom AS client_prenom
FROM releve r
INNER JOIN compteur c ON r.id_compteur = c.id_compteur
INNER JOIN agent a ON r.id_agent = a.id_agent
INNER JOIN adresse addr ON c.id_adresse = addr.id_adresse
INNER JOIN quartier q ON addr.id_quartier = q.id_quartier
INNER JOIN client cl ON addr.id_client = cl.id_client;

-- -----------------------------------------------------
-- Vue : Statistiques des agents par quartier
-- -----------------------------------------------------
CREATE VIEW v_agents_stats AS
SELECT 
    q.id_quartier,
    q.nom_quartier,
    COUNT(DISTINCT a.id_agent) AS nb_agents,
    COUNT(DISTINCT addr.id_adresse) AS nb_adresses,
    COUNT(DISTINCT c.id_compteur) AS nb_compteurs,
    ROUND(COUNT(DISTINCT addr.id_adresse) / NULLIF(COUNT(DISTINCT a.id_agent), 0), 0) AS ratio_adresses_par_agent
FROM quartier q
LEFT JOIN agent a ON q.id_quartier = a.id_quartier AND a.actif = TRUE
LEFT JOIN adresse addr ON q.id_quartier = addr.id_quartier
LEFT JOIN compteur c ON addr.id_adresse = c.id_adresse AND c.actif = TRUE
GROUP BY q.id_quartier, q.nom_quartier;

-- -----------------------------------------------------
-- Vue : Compteurs sans relevé récent (> 40 jours)
-- -----------------------------------------------------
CREATE VIEW v_compteurs_sans_releve_recent AS
SELECT 
    c.id_compteur,
    c.type,
    c.date_derniere_releve,
    DATEDIFF(CURRENT_DATE, DATE(c.date_derniere_releve)) AS jours_depuis_dernier_releve,
    addr.adresse_complete,
    q.nom_quartier,
    cl.nom AS client_nom,
    cl.prenom AS client_prenom
FROM compteur c
INNER JOIN adresse addr ON c.id_adresse = addr.id_adresse
INNER JOIN quartier q ON addr.id_quartier = q.id_quartier
INNER JOIN client cl ON addr.id_client = cl.id_client
WHERE c.actif = TRUE
AND (
    c.date_derniere_releve IS NULL 
    OR DATEDIFF(CURRENT_DATE, DATE(c.date_derniere_releve)) > 40
)
ORDER BY jours_depuis_dernier_releve DESC;

-- =====================================================
-- DONNÉES DE TEST (OPTIONNEL)
-- =====================================================

-- Insertion de quartiers de test
INSERT INTO quartier (nom_quartier, ville) VALUES
('Agdal', 'Rabat'),
('Hassan', 'Rabat'),
('Océan', 'Rabat'),
('Souissi', 'Rabat'),
('Hay Riad', 'Rabat');

-- Insertion d'un superadmin par défaut
-- Mot de passe : Admin@123 (à changer en production)
-- Hash bcrypt (cost 10) : $2y$10$YourHashHere...
INSERT INTO utilisateur_backoffice (nom, prenom, email, password_hash, role, premiere_connexion)
VALUES ('ADMIN', 'Super', 'admin@ree.ma', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Superadmin', TRUE);

-- Note : Les autres données (clients, agents, adresses, compteurs) 
-- seront synchronisées depuis les systèmes ERP via les batchs planifiés

-- =====================================================
-- FIN DU SCRIPT
-- =====================================================

-- Afficher les tables créées
SHOW TABLES;

-- Afficher la structure de chaque table
SHOW CREATE TABLE quartier;
SHOW CREATE TABLE client;
SHOW CREATE TABLE adresse;
SHOW CREATE TABLE agent;
SHOW CREATE TABLE compteur;
SHOW CREATE TABLE releve;
SHOW CREATE TABLE utilisateur_backoffice;
