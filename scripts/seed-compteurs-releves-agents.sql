-- ============================================
-- Script SQL pour peupler les Agents, Compteurs et Releves
-- Complément au script seed-real-data.sql
-- ============================================

USE si_releves;

-- ============================================
-- 1. Créer des Agents répartis dans les quartiers
-- ============================================

-- Récupérer les IDs des quartiers
SET @quartier_agdal = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Agdal' LIMIT 1);
SET @quartier_hay_riad = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Hay Riad' LIMIT 1);
SET @quartier_souissi = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Souissi' LIMIT 1);
SET @quartier_hassan = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Hassan' LIMIT 1);
SET @quartier_akkari = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Akkari' LIMIT 1);
SET @quartier_ocean = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Océan' LIMIT 1);
SET @quartier_hay_nahda = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Hay Nahda' LIMIT 1);

-- Insérer les agents
INSERT IGNORE INTO agent (id_agent, id_quartier, nom, prenom, tel_personnel, tel_professionnel, date_affectation, actif) VALUES
-- Agdal
('AGT001', @quartier_agdal, 'Bouazza', 'Karim', '0661234567', '0537123456', DATE_SUB(NOW(), INTERVAL 2 YEAR), TRUE),
('AGT002', @quartier_agdal, 'Tazi', 'Sanae', '0662345678', '0537234567', DATE_SUB(NOW(), INTERVAL 1 YEAR), TRUE),
('AGT003', @quartier_agdal, 'Lahlou', 'Youssef', '0663456789', '0537345678', DATE_SUB(NOW(), INTERVAL 6 MONTH), TRUE),

-- Hay Riad
('AGT004', @quartier_hay_riad, 'Berrada', 'Nadia', '0664567890', '0537456789', DATE_SUB(NOW(), INTERVAL 18 MONTH), TRUE),
('AGT005', @quartier_hay_riad, 'Chraibi', 'Omar', '0665678901', '0537567890', DATE_SUB(NOW(), INTERVAL 9 MONTH), TRUE),
('AGT006', @quartier_hay_riad, 'Amrani', 'Leila', '0666789012', '0537678901', DATE_SUB(NOW(), INTERVAL 3 MONTH), TRUE),

-- Souissi
('AGT007', @quartier_souissi, 'Fassi', 'Hassan', '0667890123', '0537789012', DATE_SUB(NOW(), INTERVAL 2 YEAR), TRUE),
('AGT008', @quartier_souissi, 'Mekouar', 'Souad', '0668901234', '0537890123', DATE_SUB(NOW(), INTERVAL 12 MONTH), TRUE),

-- Hassan
('AGT009', @quartier_hassan, 'Bensaid', 'Rachid', '0669012345', '0537901234', DATE_SUB(NOW(), INTERVAL 15 MONTH), TRUE),
('AGT010', @quartier_hassan, 'El Fassi', 'Khadija', '0660123456', '0537012345', DATE_SUB(NOW(), INTERVAL 8 MONTH), TRUE),

-- Akkari
('AGT011', @quartier_akkari, 'Alami', 'Mehdi', '0661234509', '0537123409', DATE_SUB(NOW(), INTERVAL 10 MONTH), TRUE),
('AGT012', @quartier_akkari, 'Bennani', 'Samira', '0662345610', '0537234510', DATE_SUB(NOW(), INTERVAL 4 MONTH), TRUE),

-- Océan
('AGT013', @quartier_ocean, 'Cherkaoui', 'Amine', '0663456721', '0537345621', DATE_SUB(NOW(), INTERVAL 7 MONTH), TRUE),

-- Hay Nahda
('AGT014', @quartier_hay_nahda, 'Dari', 'Salma', '0664567832', '0537456732', DATE_SUB(NOW(), INTERVAL 5 MONTH), TRUE),
('AGT015', @quartier_hay_nahda, 'El Ouazzani', 'Hicham', '0665678943', '0537567843', DATE_SUB(NOW(), INTERVAL 11 MONTH), TRUE);

-- ============================================
-- 2. Créer des Compteurs pour les adresses
-- ============================================

-- Récupérer les IDs des adresses
SET @adresse_1 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 0);
SET @adresse_2 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 1);
SET @adresse_3 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 2);
SET @adresse_4 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 3);
SET @adresse_5 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 4);
SET @adresse_6 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 5);
SET @adresse_7 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 6);
SET @adresse_8 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 7);
SET @adresse_9 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 8);
SET @adresse_10 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 9);
SET @adresse_11 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 10);
SET @adresse_12 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 11);
SET @adresse_13 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 12);
SET @adresse_14 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 13);
SET @adresse_15 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 14);
SET @adresse_16 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 15);
SET @adresse_17 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 16);
SET @adresse_18 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 17);
SET @adresse_19 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 18);
SET @adresse_20 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 19);
SET @adresse_21 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 20);
SET @adresse_22 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 21);
SET @adresse_23 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 22);
SET @adresse_24 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 23);
SET @adresse_25 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 24);
SET @adresse_26 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 25);
SET @adresse_27 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 26);
SET @adresse_28 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 27);
SET @adresse_29 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 28);
SET @adresse_30 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 29);
SET @adresse_31 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 30);
SET @adresse_32 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 31);
SET @adresse_33 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 32);
SET @adresse_34 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 33);
SET @adresse_35 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 34);
SET @adresse_36 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 35);
SET @adresse_37 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 36);
SET @adresse_38 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 37);
SET @adresse_39 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 38);
SET @adresse_40 = (SELECT id_adresse FROM adresse LIMIT 1 OFFSET 39);

-- Compteurs Eau
INSERT IGNORE INTO compteur (id_compteur, id_adresse, type, index_actuel, date_installation, pour_espaces_communs, actif) VALUES
('EAU000001', @adresse_1, 'Eau', 1250.50, DATE_SUB(CURDATE(), INTERVAL 450 DAY), FALSE, TRUE),
('EAU000002', @adresse_2, 'Eau', 890.25, DATE_SUB(CURDATE(), INTERVAL 320 DAY), FALSE, TRUE),
('EAU000003', @adresse_3, 'Eau', 2100.75, DATE_SUB(CURDATE(), INTERVAL 180 DAY), FALSE, TRUE),
('EAU000004', @adresse_4, 'Eau', 567.00, DATE_SUB(CURDATE(), INTERVAL 600 DAY), FALSE, TRUE),
('EAU000005', @adresse_5, 'Eau', 1890.50, DATE_SUB(CURDATE(), INTERVAL 250 DAY), TRUE, TRUE),
('EAU000006', @adresse_6, 'Eau', 1345.25, DATE_SUB(CURDATE(), INTERVAL 380 DAY), FALSE, TRUE),
('EAU000007', @adresse_7, 'Eau', 980.75, DATE_SUB(CURDATE(), INTERVAL 520 DAY), FALSE, TRUE),
('EAU000008', @adresse_8, 'Eau', 2450.00, DATE_SUB(CURDATE(), INTERVAL 150 DAY), TRUE, TRUE),
('EAU000009', @adresse_9, 'Eau', 1120.50, DATE_SUB(CURDATE(), INTERVAL 420 DAY), FALSE, TRUE),
('EAU000010', @adresse_10, 'Eau', 765.25, DATE_SUB(CURDATE(), INTERVAL 290 DAY), FALSE, TRUE),
('EAU000011', @adresse_11, 'Eau', 1980.75, DATE_SUB(CURDATE(), INTERVAL 200 DAY), FALSE, TRUE),
('EAU000012', @adresse_12, 'Eau', 1456.00, DATE_SUB(CURDATE(), INTERVAL 350 DAY), FALSE, TRUE),
('EAU000013', @adresse_13, 'Eau', 890.50, DATE_SUB(CURDATE(), INTERVAL 480 DAY), FALSE, TRUE),
('EAU000014', @adresse_14, 'Eau', 2234.25, DATE_SUB(CURDATE(), INTERVAL 120 DAY), TRUE, TRUE),
('EAU000015', @adresse_15, 'Eau', 1678.75, DATE_SUB(CURDATE(), INTERVAL 270 DAY), FALSE, TRUE),
('EAU000016', @adresse_16, 'Eau', 1023.00, DATE_SUB(CURDATE(), INTERVAL 390 DAY), FALSE, TRUE),
('EAU000017', @adresse_17, 'Eau', 1890.50, DATE_SUB(CURDATE(), INTERVAL 220 DAY), FALSE, TRUE),
('EAU000018', @adresse_18, 'Eau', 1345.25, DATE_SUB(CURDATE(), INTERVAL 310 DAY), FALSE, TRUE),
('EAU000019', @adresse_19, 'Eau', 2567.75, DATE_SUB(CURDATE(), INTERVAL 100 DAY), TRUE, TRUE),
('EAU000020', @adresse_20, 'Eau', 1123.00, DATE_SUB(CURDATE(), INTERVAL 410 DAY), FALSE, TRUE),
('EAU000021', @adresse_21, 'Eau', 1789.50, DATE_SUB(CURDATE(), INTERVAL 240 DAY), FALSE, TRUE),
('EAU000022', @adresse_22, 'Eau', 987.25, DATE_SUB(CURDATE(), INTERVAL 550 DAY), FALSE, TRUE),
('EAU000023', @adresse_23, 'Eau', 2345.75, DATE_SUB(CURDATE(), INTERVAL 130 DAY), FALSE, TRUE),
('EAU000024', @adresse_24, 'Eau', 1456.00, DATE_SUB(CURDATE(), INTERVAL 360 DAY), FALSE, TRUE),
('EAU000025', @adresse_25, 'Eau', 2012.50, DATE_SUB(CURDATE(), INTERVAL 190 DAY), TRUE, TRUE),
('EAU000026', @adresse_26, 'Eau', 1234.25, DATE_SUB(CURDATE(), INTERVAL 430 DAY), FALSE, TRUE),
('EAU000027', @adresse_27, 'Eau', 1678.75, DATE_SUB(CURDATE(), INTERVAL 280 DAY), FALSE, TRUE),
('EAU000028', @adresse_28, 'Eau', 890.00, DATE_SUB(CURDATE(), INTERVAL 500 DAY), FALSE, TRUE),
('EAU000029', @adresse_29, 'Eau', 2456.50, DATE_SUB(CURDATE(), INTERVAL 110 DAY), FALSE, TRUE),
('EAU000030', @adresse_30, 'Eau', 1345.25, DATE_SUB(CURDATE(), INTERVAL 370 DAY), FALSE, TRUE);

-- Compteurs Electricite
INSERT IGNORE INTO compteur (id_compteur, id_adresse, type, index_actuel, date_installation, pour_espaces_communs, actif) VALUES
('ELEC00001', @adresse_1, 'Electricite', 4520.50, DATE_SUB(CURDATE(), INTERVAL 450 DAY), FALSE, TRUE),
('ELEC00002', @adresse_2, 'Electricite', 3120.25, DATE_SUB(CURDATE(), INTERVAL 320 DAY), FALSE, TRUE),
('ELEC00003', @adresse_3, 'Electricite', 6780.75, DATE_SUB(CURDATE(), INTERVAL 180 DAY), FALSE, TRUE),
('ELEC00004', @adresse_4, 'Electricite', 2340.00, DATE_SUB(CURDATE(), INTERVAL 600 DAY), FALSE, TRUE),
('ELEC00005', @adresse_5, 'Electricite', 5890.50, DATE_SUB(CURDATE(), INTERVAL 250 DAY), TRUE, TRUE),
('ELEC00006', @adresse_6, 'Electricite', 4120.25, DATE_SUB(CURDATE(), INTERVAL 380 DAY), FALSE, TRUE),
('ELEC00007', @adresse_7, 'Electricite', 3456.75, DATE_SUB(CURDATE(), INTERVAL 520 DAY), FALSE, TRUE),
('ELEC00008', @adresse_8, 'Electricite', 7890.00, DATE_SUB(CURDATE(), INTERVAL 150 DAY), TRUE, TRUE),
('ELEC00009', @adresse_9, 'Electricite', 3678.50, DATE_SUB(CURDATE(), INTERVAL 420 DAY), FALSE, TRUE),
('ELEC00010', @adresse_10, 'Electricite', 2890.25, DATE_SUB(CURDATE(), INTERVAL 290 DAY), FALSE, TRUE),
('ELEC00011', @adresse_11, 'Electricite', 6234.75, DATE_SUB(CURDATE(), INTERVAL 200 DAY), FALSE, TRUE),
('ELEC00012', @adresse_12, 'Electricite', 4567.00, DATE_SUB(CURDATE(), INTERVAL 350 DAY), FALSE, TRUE),
('ELEC00013', @adresse_13, 'Electricite', 3123.50, DATE_SUB(CURDATE(), INTERVAL 480 DAY), FALSE, TRUE),
('ELEC00014', @adresse_14, 'Electricite', 7123.25, DATE_SUB(CURDATE(), INTERVAL 120 DAY), TRUE, TRUE),
('ELEC00015', @adresse_15, 'Electricite', 5234.75, DATE_SUB(CURDATE(), INTERVAL 270 DAY), FALSE, TRUE),
('ELEC00016', @adresse_16, 'Electricite', 3456.00, DATE_SUB(CURDATE(), INTERVAL 390 DAY), FALSE, TRUE),
('ELEC00017', @adresse_17, 'Electricite', 5678.50, DATE_SUB(CURDATE(), INTERVAL 220 DAY), FALSE, TRUE),
('ELEC00018', @adresse_18, 'Electricite', 4123.25, DATE_SUB(CURDATE(), INTERVAL 310 DAY), FALSE, TRUE),
('ELEC00019', @adresse_19, 'Electricite', 8234.75, DATE_SUB(CURDATE(), INTERVAL 100 DAY), TRUE, TRUE),
('ELEC00020', @adresse_20, 'Electricite', 3789.00, DATE_SUB(CURDATE(), INTERVAL 410 DAY), FALSE, TRUE),
('ELEC00021', @adresse_21, 'Electricite', 6123.50, DATE_SUB(CURDATE(), INTERVAL 240 DAY), FALSE, TRUE),
('ELEC00022', @adresse_22, 'Electricite', 3456.25, DATE_SUB(CURDATE(), INTERVAL 550 DAY), FALSE, TRUE),
('ELEC00023', @adresse_23, 'Electricite', 7234.75, DATE_SUB(CURDATE(), INTERVAL 130 DAY), FALSE, TRUE),
('ELEC00024', @adresse_24, 'Electricite', 4567.00, DATE_SUB(CURDATE(), INTERVAL 360 DAY), FALSE, TRUE),
('ELEC00025', @adresse_25, 'Electricite', 6345.50, DATE_SUB(CURDATE(), INTERVAL 190 DAY), TRUE, TRUE),
('ELEC00026', @adresse_26, 'Electricite', 3890.25, DATE_SUB(CURDATE(), INTERVAL 430 DAY), FALSE, TRUE),
('ELEC00027', @adresse_27, 'Electricite', 5234.75, DATE_SUB(CURDATE(), INTERVAL 280 DAY), FALSE, TRUE),
('ELEC00028', @adresse_28, 'Electricite', 3123.00, DATE_SUB(CURDATE(), INTERVAL 500 DAY), FALSE, TRUE),
('ELEC00029', @adresse_29, 'Electricite', 7456.50, DATE_SUB(CURDATE(), INTERVAL 110 DAY), FALSE, TRUE),
('ELEC00030', @adresse_30, 'Electricite', 4234.25, DATE_SUB(CURDATE(), INTERVAL 370 DAY), FALSE, TRUE);

-- ============================================
-- 3. Créer des Relevés historiques
-- ============================================

-- Récupérer les IDs des compteurs et agents pour créer des relevés
-- Relevés pour compteurs Eau
INSERT INTO releve (id_compteur, id_agent, date_releve, ancien_index, nouvel_index, consommation, unite, envoye_facturation, date_envoi_facturation)
SELECT 
    c.id_compteur,
    (SELECT a.id_agent FROM agent a 
     JOIN quartier q ON a.id_quartier = q.id_quartier
     JOIN adresse adr ON adr.id_quartier = q.id_quartier
     WHERE adr.id_adresse = c.id_adresse
     AND a.actif = TRUE
     ORDER BY RAND()
     LIMIT 1) AS id_agent,
    DATE_SUB(NOW(), INTERVAL 30 DAY) AS date_releve,
    ROUND(c.index_actuel - 85.50, 2) AS ancien_index,
    c.index_actuel AS nouvel_index,
    85.50 AS consommation,
    'm3' AS unite,
    TRUE AS envoye_facturation,
    DATE_SUB(NOW(), INTERVAL 25 DAY) AS date_envoi_facturation
FROM compteur c
WHERE c.type = 'Eau' AND c.actif = TRUE
LIMIT 25;

-- Relevés supplémentaires pour Eau (historique)
INSERT INTO releve (id_compteur, id_agent, date_releve, ancien_index, nouvel_index, consommation, unite, envoye_facturation, date_envoi_facturation)
SELECT 
    c.id_compteur,
    (SELECT a.id_agent FROM agent a 
     JOIN quartier q ON a.id_quartier = q.id_quartier
     JOIN adresse adr ON adr.id_quartier = q.id_quartier
     WHERE adr.id_adresse = c.id_adresse
     AND a.actif = TRUE
     ORDER BY RAND()
     LIMIT 1) AS id_agent,
    DATE_SUB(NOW(), INTERVAL 90 DAY) AS date_releve,
    ROUND(c.index_actuel - 170.25, 2) AS ancien_index,
    ROUND(c.index_actuel - 85.50, 2) AS nouvel_index,
    84.75 AS consommation,
    'm3' AS unite,
    TRUE AS envoye_facturation,
    DATE_SUB(NOW(), INTERVAL 85 DAY) AS date_envoi_facturation
FROM compteur c
WHERE c.type = 'Eau' AND c.actif = TRUE
LIMIT 20;

-- Relevés pour compteurs Electricite
INSERT INTO releve (id_compteur, id_agent, date_releve, ancien_index, nouvel_index, consommation, unite, envoye_facturation, date_envoi_facturation)
SELECT 
    c.id_compteur,
    (SELECT a.id_agent FROM agent a 
     JOIN quartier q ON a.id_quartier = q.id_quartier
     JOIN adresse adr ON adr.id_quartier = q.id_quartier
     WHERE adr.id_adresse = c.id_adresse
     AND a.actif = TRUE
     ORDER BY RAND()
     LIMIT 1) AS id_agent,
    DATE_SUB(NOW(), INTERVAL 30 DAY) AS date_releve,
    ROUND(c.index_actuel - 245.75, 2) AS ancien_index,
    c.index_actuel AS nouvel_index,
    245.75 AS consommation,
    'kWh' AS unite,
    TRUE AS envoye_facturation,
    DATE_SUB(NOW(), INTERVAL 25 DAY) AS date_envoi_facturation
FROM compteur c
WHERE c.type = 'Electricite' AND c.actif = TRUE
LIMIT 25;

-- Relevés supplémentaires pour Electricite (historique)
INSERT INTO releve (id_compteur, id_agent, date_releve, ancien_index, nouvel_index, consommation, unite, envoye_facturation, date_envoi_facturation)
SELECT 
    c.id_compteur,
    (SELECT a.id_agent FROM agent a 
     JOIN quartier q ON a.id_quartier = q.id_quartier
     JOIN adresse adr ON adr.id_quartier = q.id_quartier
     WHERE adr.id_adresse = c.id_adresse
     AND a.actif = TRUE
     ORDER BY RAND()
     LIMIT 1) AS id_agent,
    DATE_SUB(NOW(), INTERVAL 90 DAY) AS date_releve,
    ROUND(c.index_actuel - 512.50, 2) AS ancien_index,
    ROUND(c.index_actuel - 245.75, 2) AS nouvel_index,
    266.75 AS consommation,
    'kWh' AS unite,
    TRUE AS envoye_facturation,
    DATE_SUB(NOW(), INTERVAL 85 DAY) AS date_envoi_facturation
FROM compteur c
WHERE c.type = 'Electricite' AND c.actif = TRUE
LIMIT 20;

-- Mettre à jour date_derniere_releve des compteurs
UPDATE compteur c
SET c.date_derniere_releve = (
    SELECT MAX(r.date_releve) 
    FROM releve r 
    WHERE r.id_compteur = c.id_compteur
)
WHERE EXISTS (
    SELECT 1 FROM releve r WHERE r.id_compteur = c.id_compteur
);

-- ============================================
-- Vérification des données insérées
-- ============================================
SELECT 'Agents crees:' AS info, COUNT(*) AS total FROM agent;
SELECT 'Compteurs crees:' AS info, COUNT(*) AS total FROM compteur;
SELECT 'Releves crees:' AS info, COUNT(*) AS total FROM releve;

SELECT 'Repartition des compteurs par type:' AS info;
SELECT type, COUNT(*) AS total FROM compteur GROUP BY type;

SELECT 'Repartition des releves par unite:' AS info;
SELECT unite, COUNT(*) AS total FROM releve GROUP BY unite;

-- Afficher quelques exemples
SELECT 'Exemples d''agents:' AS info;
SELECT a.id_agent, a.nom, a.prenom, q.nom_quartier, a.tel_professionnel 
FROM agent a
JOIN quartier q ON a.id_quartier = q.id_quartier
LIMIT 10;

SELECT 'Exemples de compteurs:' AS info;
SELECT c.id_compteur, c.type, c.index_actuel, adr.adresse_complete, q.nom_quartier
FROM compteur c
JOIN adresse adr ON c.id_adresse = adr.id_adresse
JOIN quartier q ON adr.id_quartier = q.id_quartier
LIMIT 10;

SELECT 'Exemples de releves:' AS info;
SELECT r.id_releve, c.id_compteur, c.type, a.nom AS nom_agent, a.prenom AS prenom_agent, 
       r.date_releve, r.ancien_index, r.nouvel_index, r.consommation, r.unite, r.envoye_facturation
FROM releve r
JOIN compteur c ON r.id_compteur = c.id_compteur
JOIN agent a ON r.id_agent = a.id_agent
ORDER BY r.date_releve DESC
LIMIT 10;
