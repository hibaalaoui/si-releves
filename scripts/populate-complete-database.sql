-- ============================================
-- Script complet pour peupler toute la base de données SI Relevés
-- Selon le cahier de charges - données réalistes pour Rabat
-- ============================================

USE si_releves;

-- ============================================
-- 1. Quartiers de Rabat
-- ============================================
INSERT IGNORE INTO quartier (nom_quartier, ville) VALUES
('Agdal', 'Rabat'),
('Hay Riad', 'Rabat'),
('Souissi', 'Rabat'),
('Hassan', 'Rabat'),
('Akkari', 'Rabat'),
('Océan', 'Rabat'),
('Hay Nahda', 'Rabat'),
('Hay Riad Extension', 'Rabat'),
('Agdal Riyad', 'Rabat'),
('Touarga', 'Rabat');

-- Variables pour les IDs de quartiers
SET @quartier_agdal = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Agdal' LIMIT 1);
SET @quartier_hay_riad = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Hay Riad' LIMIT 1);
SET @quartier_souissi = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Souissi' LIMIT 1);
SET @quartier_hassan = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Hassan' LIMIT 1);
SET @quartier_akkari = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Akkari' LIMIT 1);
SET @quartier_ocean = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Océan' LIMIT 1);
SET @quartier_hay_nahda = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Hay Nahda' LIMIT 1);
SET @quartier_hay_riad_ext = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Hay Riad Extension' LIMIT 1);
SET @quartier_agdal_riyad = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Agdal Riyad' LIMIT 1);
SET @quartier_touarga = (SELECT id_quartier FROM quartier WHERE nom_quartier = 'Touarga' LIMIT 1);

-- ============================================
-- 2. Clients (50 clients réalistes)
-- ============================================
INSERT IGNORE INTO client (id_client, nom, prenom, date_creation) VALUES
('CLI001', 'Alaoui', 'Ahmed', NOW()),
('CLI002', 'Benali', 'Fatima', NOW()),
('CLI003', 'Idrissi', 'Mohamed', NOW()),
('CLI004', 'Bennani', 'Aicha', NOW()),
('CLI005', 'Chraibi', 'Omar', NOW()),
('CLI006', 'Tazi', 'Sanae', NOW()),
('CLI007', 'Berrada', 'Youssef', NOW()),
('CLI008', 'Lahlou', 'Nadia', NOW()),
('CLI009', 'Bouazza', 'Karim', NOW()),
('CLI010', 'Amrani', 'Leila', NOW()),
('CLI011', 'Fassi', 'Hassan', NOW()),
('CLI012', 'Mekouar', 'Souad', NOW()),
('CLI013', 'Bensaid', 'Rachid', NOW()),
('CLI014', 'El Fassi', 'Khadija', NOW()),
('CLI015', 'Alami', 'Mehdi', NOW()),
('CLI016', 'Bennani', 'Samira', NOW()),
('CLI017', 'Cherkaoui', 'Amine', NOW()),
('CLI018', 'Dari', 'Salma', NOW()),
('CLI019', 'El Ouazzani', 'Hicham', NOW()),
('CLI020', 'Fihri', 'Nabila', NOW()),
('CLI021', 'Guedira', 'Tarik', NOW()),
('CLI022', 'Hajji', 'Imane', NOW()),
('CLI023', 'Ibrahimi', 'Yassine', NOW()),
('CLI024', 'Jazouli', 'Zineb', NOW()),
('CLI025', 'Kettani', 'Anass', NOW()),
('CLI026', 'Lamrani', 'Houda', NOW()),
('CLI027', 'Mansouri', 'Bilal', NOW()),
('CLI028', 'Naciri', 'Siham', NOW()),
('CLI029', 'Ouazzani', 'Reda', NOW()),
('CLI030', 'Rahali', 'Nour', NOW()),
('CLI031', 'Saadi', 'Walid', NOW()),
('CLI032', 'Tahiri', 'Ines', NOW()),
('CLI033', 'Zahiri', 'Adil', NOW()),
('CLI034', 'Ait Ali', 'Meriem', NOW()),
('CLI035', 'Bouhaddou', 'Said', NOW()),
('CLI036', 'Chakir', 'Layla', NOW()),
('CLI037', 'Dahbi', 'Younes', NOW()),
('CLI038', 'El Amrani', 'Hafsa', NOW()),
('CLI039', 'Fadili', 'Zakaria', NOW()),
('CLI040', 'Ghazali', 'Rim', NOW()),
('CLI041', 'Hamdaoui', 'Ayoub', NOW()),
('CLI042', 'Iqbal', 'Dounia', NOW()),
('CLI043', 'Jilali', 'Othmane', NOW()),
('CLI044', 'Kadiri', 'Sara', NOW()),
('CLI045', 'Lazrak', 'Hamza', NOW()),
('CLI046', 'Mazouz', 'Ibtissam', NOW()),
('CLI047', 'Naji', 'Anouar', NOW()),
('CLI048', 'Ouali', 'Hind', NOW()),
('CLI049', 'Qadiri', 'Mehdi', NOW()),
('CLI050', 'Raji', 'Nisrine', NOW());

-- ============================================
-- 3. Adresses pour chaque client
-- ============================================
INSERT IGNORE INTO adresse (id_client, id_quartier, adresse_complete, type_bien, code_postal, date_creation) VALUES
-- Agdal
('CLI001', @quartier_agdal, '123 Avenue Mohammed V, Agdal', 'Standard', '10000', NOW()),
('CLI002', @quartier_agdal, '456 Boulevard Hassan II, Agdal', 'Immeuble', '10000', NOW()),
('CLI003', @quartier_agdal, '789 Rue Allal Ben Abdallah, Agdal', 'Villa', '10000', NOW()),
('CLI004', @quartier_agdal, '321 Avenue des FAR, Agdal', 'Appartement', '10000', NOW()),
('CLI005', @quartier_agdal, '654 Rue Ibn Sina, Agdal', 'Standard', '10000', NOW()),

-- Hay Riad
('CLI006', @quartier_hay_riad, '12 Rue de la Kasbah, Hay Riad', 'Standard', '10000', NOW()),
('CLI007', @quartier_hay_riad, '34 Avenue de l''Armée Royale, Hay Riad', 'Immeuble', '10000', NOW()),
('CLI008', @quartier_hay_riad, '56 Boulevard de la Corniche, Hay Riad', 'Villa', '10000', NOW()),
('CLI009', @quartier_hay_riad, '78 Rue Moulay Ismail, Hay Riad', 'Appartement', '10000', NOW()),
('CLI010', @quartier_hay_riad, '90 Place de la Victoire, Hay Riad', 'Standard', '10000', NOW()),

-- Souissi
('CLI011', @quartier_souissi, '1 Avenue Kennedy, Souissi', 'Villa', '10000', NOW()),
('CLI012', @quartier_souissi, '2 Boulevard de l''Océan, Souissi', 'Immeuble', '10000', NOW()),
('CLI013', @quartier_souissi, '3 Rue de Rome, Souissi', 'Appartement', '10000', NOW()),
('CLI014', @quartier_souissi, '4 Avenue des Nations Unies, Souissi', 'Standard', '10000', NOW()),
('CLI015', @quartier_souissi, '5 Boulevard de Paris, Souissi', 'Villa', '10000', NOW()),

-- Hassan
('CLI016', @quartier_hassan, '10 Rue Sidi Mohamed, Hassan', 'Standard', '10000', NOW()),
('CLI017', @quartier_hassan, '20 Avenue Moulay Youssef, Hassan', 'Immeuble', '10000', NOW()),
('CLI018', @quartier_hassan, '30 Boulevard Zerktouni, Hassan', 'Appartement', '10000', NOW()),
('CLI019', @quartier_hassan, '40 Rue de la Marine, Hassan', 'Villa', '10000', NOW()),
('CLI020', @quartier_hassan, '50 Place de l''Indépendance, Hassan', 'Standard', '10000', NOW()),

-- Akkari
('CLI021', @quartier_akkari, '100 Rue de l''Atlas, Akkari', 'Standard', '10000', NOW()),
('CLI022', @quartier_akkari, '200 Avenue de l''Université, Akkari', 'Immeuble', '10000', NOW()),
('CLI023', @quartier_akkari, '300 Boulevard de l''Avenir, Akkari', 'Appartement', '10000', NOW()),
('CLI024', @quartier_akkari, '400 Rue de la Liberté, Akkari', 'Villa', '10000', NOW()),
('CLI025', @quartier_akkari, '500 Place de la Paix, Akkari', 'Standard', '10000', NOW()),

-- Océan
('CLI026', @quartier_ocean, '15 Boulevard de l''Océan, Océan', 'Villa', '10000', NOW()),
('CLI027', @quartier_ocean, '25 Rue des Palmiers, Océan', 'Immeuble', '10000', NOW()),
('CLI028', @quartier_ocean, '35 Avenue des Dunes, Océan', 'Appartement', '10000', NOW()),
('CLI029', @quartier_ocean, '45 Boulevard Maritime, Océan', 'Standard', '10000', NOW()),
('CLI030', @quartier_ocean, '55 Rue des vagues, Océan', 'Villa', '10000', NOW()),

-- Hay Nahda
('CLI031', @quartier_hay_nahda, '60 Rue de l''Espoir, Hay Nahda', 'Standard', '10000', NOW()),
('CLI032', @quartier_hay_nahda, '70 Avenue du Progrès, Hay Nahda', 'Immeuble', '10000', NOW()),
('CLI033', @quartier_hay_nahda, '80 Boulevard de l''Innovation, Hay Nahda', 'Appartement', '10000', NOW()),
('CLI034', @quartier_hay_nahda, '90 Rue de l''Avenir, Hay Nahda', 'Villa', '10000', NOW()),
('CLI035', @quartier_hay_nahda, '100 Place de l''Espoir, Hay Nahda', 'Standard', '10000', NOW()),

-- Hay Riad Extension
('CLI036', @quartier_hay_riad_ext, '110 Rue de l''Extension, Hay Riad Extension', 'Standard', '10000', NOW()),
('CLI037', @quartier_hay_riad_ext, '120 Avenue Nouvelle, Hay Riad Extension', 'Immeuble', '10000', NOW()),
('CLI038', @quartier_hay_riad_ext, '130 Boulevard Moderne, Hay Riad Extension', 'Appartement', '10000', NOW()),
('CLI039', @quartier_hay_riad_ext, '140 Rue Contemporaine, Hay Riad Extension', 'Villa', '10000', NOW()),
('CLI040', @quartier_hay_riad_ext, '150 Place du Futur, Hay Riad Extension', 'Standard', '10000', NOW()),

-- Agdal Riyad
('CLI041', @quartier_agdal_riyad, '160 Rue Royale, Agdal Riyad', 'Villa', '10000', NOW()),
('CLI042', @quartier_agdal_riyad, '170 Avenue Princière, Agdal Riyad', 'Immeuble', '10000', NOW()),
('CLI043', @quartier_agdal_riyad, '180 Boulevard Aristocratique, Agdal Riyad', 'Appartement', '10000', NOW()),
('CLI044', @quartier_agdal_riyad, '190 Rue Prestige, Agdal Riyad', 'Standard', '10000', NOW()),
('CLI045', @quartier_agdal_riyad, '200 Place d''Honneur, Agdal Riyad', 'Villa', '10000', NOW()),

-- Touarga
('CLI046', @quartier_touarga, '210 Rue Traditionnelle, Touarga', 'Standard', '10000', NOW()),
('CLI047', @quartier_touarga, '220 Avenue Ancestrale, Touarga', 'Immeuble', '10000', NOW()),
('CLI048', @quartier_touarga, '230 Boulevard Historique, Touarga', 'Appartement', '10000', NOW()),
('CLI049', @quartier_touarga, '240 Rue Patrimoniale, Touarga', 'Villa', '10000', NOW()),
('CLI050', @quartier_touarga, '250 Place Héritage, Touarga', 'Standard', '10000', NOW());

-- ============================================
-- 4. Agents de terrain
-- ============================================
INSERT IGNORE INTO agent (id_agent, id_quartier, nom, prenom, tel_personnel, tel_professionnel, date_affectation, actif) VALUES
-- Agdal (3 agents)
('AGT001', @quartier_agdal, 'Bouazza', 'Karim', '0661234567', '0537123456', DATE_SUB(NOW(), INTERVAL 2 YEAR), TRUE),
('AGT002', @quartier_agdal, 'Tazi', 'Sanae', '0662345678', '0537234567', DATE_SUB(NOW(), INTERVAL 1 YEAR), TRUE),
('AGT003', @quartier_agdal, 'Lahlou', 'Youssef', '0663456789', '0537345678', DATE_SUB(NOW(), INTERVAL 6 MONTH), TRUE),

-- Hay Riad (3 agents)
('AGT004', @quartier_hay_riad, 'Berrada', 'Nadia', '0664567890', '0537456789', DATE_SUB(NOW(), INTERVAL 18 MONTH), TRUE),
('AGT005', @quartier_hay_riad, 'Chraibi', 'Omar', '0665678901', '0537567890', DATE_SUB(NOW(), INTERVAL 9 MONTH), TRUE),
('AGT006', @quartier_hay_riad, 'Amrani', 'Leila', '0666789012', '0537678901', DATE_SUB(NOW(), INTERVAL 3 MONTH), TRUE),

-- Souissi (2 agents)
('AGT007', @quartier_souissi, 'Fassi', 'Hassan', '0667890123', '0537789012', DATE_SUB(NOW(), INTERVAL 2 YEAR), TRUE),
('AGT008', @quartier_souissi, 'Mekouar', 'Souad', '0668901234', '0537890123', DATE_SUB(NOW(), INTERVAL 12 MONTH), TRUE),

-- Hassan (2 agents)
('AGT009', @quartier_hassan, 'Bensaid', 'Rachid', '0669012345', '0537901234', DATE_SUB(NOW(), INTERVAL 15 MONTH), TRUE),
('AGT010', @quartier_hassan, 'El Fassi', 'Khadija', '0660123456', '0537012345', DATE_SUB(NOW(), INTERVAL 8 MONTH), TRUE),

-- Akkari (2 agents)
('AGT011', @quartier_akkari, 'Alami', 'Mehdi', '0661234509', '0537123409', DATE_SUB(NOW(), INTERVAL 10 MONTH), TRUE),
('AGT012', @quartier_akkari, 'Bennani', 'Samira', '0662345610', '0537234510', DATE_SUB(NOW(), INTERVAL 4 MONTH), TRUE),

-- Océan (1 agent)
('AGT013', @quartier_ocean, 'Cherkaoui', 'Amine', '0663456721', '0537345621', DATE_SUB(NOW(), INTERVAL 7 MONTH), TRUE),

-- Hay Nahda (2 agents)
('AGT014', @quartier_hay_nahda, 'Dari', 'Salma', '0664567832', '0537456732', DATE_SUB(NOW(), INTERVAL 5 MONTH), TRUE),
('AGT015', @quartier_hay_nahda, 'El Ouazzani', 'Hicham', '0665678943', '0537567843', DATE_SUB(NOW(), INTERVAL 11 MONTH), TRUE),

-- Hay Riad Extension (1 agent)
('AGT016', @quartier_hay_riad_ext, 'Ghazali', 'Rim', '0666789054', '0537890567', DATE_SUB(NOW(), INTERVAL 2 MONTH), TRUE),

-- Agdal Riyad (1 agent)
('AGT017', @quartier_agdal_riyad, 'Hamdaoui', 'Ayoub', '0667890165', '0537901678', DATE_SUB(NOW(), INTERVAL 1 MONTH), TRUE),

-- Touarga (1 agent)
('AGT018', @quartier_touarga, 'Iqbal', 'Dounia', '0668901276', '0537012789', DATE_SUB(NOW(), INTERVAL 4 MONTH), TRUE);

-- ============================================
-- 5. Compteurs (2 par adresse maximum : Eau + Électricité)
-- ============================================
-- Variables pour les adresses
SET @adresse_1 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI001' LIMIT 1);
SET @adresse_2 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI002' LIMIT 1);
SET @adresse_3 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI003' LIMIT 1);
SET @adresse_4 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI004' LIMIT 1);
SET @adresse_5 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI005' LIMIT 1);
SET @adresse_6 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI006' LIMIT 1);
SET @adresse_7 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI007' LIMIT 1);
SET @adresse_8 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI008' LIMIT 1);
SET @adresse_9 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI009' LIMIT 1);
SET @adresse_10 = (SELECT id_adresse FROM adresse WHERE id_client = 'CLI010' LIMIT 1);

-- Compteurs d'eau (commencent par EAU)
INSERT IGNORE INTO compteur (id_compteur, id_adresse, type, index_actuel, date_installation, pour_espaces_communs, actif) VALUES
('EAU000001', @adresse_1, 'Eau', 1250.50, DATE_SUB(CURDATE(), INTERVAL 450 DAY), FALSE, TRUE),
('EAU000002', @adresse_2, 'Eau', 980.75, DATE_SUB(CURDATE(), INTERVAL 420 DAY), FALSE, TRUE),
('EAU000003', @adresse_3, 'Eau', 1540.20, DATE_SUB(CURDATE(), INTERVAL 380 DAY), FALSE, TRUE),
('EAU000004', @adresse_4, 'Eau', 750.90, DATE_SUB(CURDATE(), INTERVAL 360 DAY), FALSE, TRUE),
('EAU000005', @adresse_5, 'Eau', 1100.30, DATE_SUB(CURDATE(), INTERVAL 400 DAY), FALSE, TRUE),
('EAU000006', @adresse_6, 'Eau', 890.60, DATE_SUB(CURDATE(), INTERVAL 350 DAY), FALSE, TRUE),
('EAU000007', @adresse_7, 'Eau', 1320.45, DATE_SUB(CURDATE(), INTERVAL 410 DAY), FALSE, TRUE),
('EAU000008', @adresse_8, 'Eau', 650.80, DATE_SUB(CURDATE(), INTERVAL 370 DAY), FALSE, TRUE),
('EAU000009', @adresse_9, 'Eau', 1680.90, DATE_SUB(CURDATE(), INTERVAL 430 DAY), FALSE, TRUE),
('EAU000010', @adresse_10, 'Eau', 920.25, DATE_SUB(CURDATE(), INTERVAL 390 DAY), FALSE, TRUE);

-- Compteurs d'électricité (commencent par ELE)
INSERT IGNORE INTO compteur (id_compteur, id_adresse, type, index_actuel, date_installation, pour_espaces_communs, actif) VALUES
('ELE000001', @adresse_1, 'Electricite', 3450.75, DATE_SUB(CURDATE(), INTERVAL 450 DAY), FALSE, TRUE),
('ELE000002', @adresse_2, 'Electricite', 2800.90, DATE_SUB(CURDATE(), INTERVAL 420 DAY), FALSE, TRUE),
('ELE000003', @adresse_3, 'Electricite', 4120.30, DATE_SUB(CURDATE(), INTERVAL 380 DAY), FALSE, TRUE),
('ELE000004', @adresse_4, 'Electricite', 1950.45, DATE_SUB(CURDATE(), INTERVAL 360 DAY), FALSE, TRUE),
('ELE000005', @adresse_5, 'Electricite', 2980.60, DATE_SUB(CURDATE(), INTERVAL 400 DAY), FALSE, TRUE),
('ELE000006', @adresse_6, 'Electricite', 2340.80, DATE_SUB(CURDATE(), INTERVAL 350 DAY), FALSE, TRUE),
('ELE000007', @adresse_7, 'Electricite', 3650.25, DATE_SUB(CURDATE(), INTERVAL 410 DAY), FALSE, TRUE),
('ELE000008', @adresse_8, 'Electricite', 1780.90, DATE_SUB(CURDATE(), INTERVAL 370 DAY), FALSE, TRUE),
('ELE000009', @adresse_9, 'Electricite', 4580.15, DATE_SUB(CURDATE(), INTERVAL 430 DAY), FALSE, TRUE),
('ELE000010', @adresse_10, 'Electricite', 2520.40, DATE_SUB(CURDATE(), INTERVAL 390 DAY), FALSE, TRUE);

-- ============================================
-- 6. Historique des relevés (données de test)
-- ============================================
-- Variables pour les compteurs
SET @compteur_eau_1 = 'EAU000001';
SET @compteur_elec_1 = 'ELE000001';
SET @compteur_eau_2 = 'EAU000002';
SET @compteur_elec_2 = 'ELE000002';

-- Agent pour les relevés
SET @agent_1 = 'AGT001';

-- Historique des relevés pour les 6 derniers mois (mensuels)
INSERT IGNORE INTO releve (id_compteur, id_agent, ancien_index, nouvel_index, consommation, unite, date_releve, envoye_facturation) VALUES
-- Compteur EAU000001 (Eau)
(@compteur_eau_1, @agent_1, 1150.00, 1200.00, 50.00, 'm3', DATE_SUB(CURDATE(), INTERVAL 6 MONTH), TRUE),
(@compteur_eau_1, @agent_1, 1200.00, 1220.50, 20.50, 'm3', DATE_SUB(CURDATE(), INTERVAL 5 MONTH), TRUE),
(@compteur_eau_1, @agent_1, 1220.50, 1235.75, 15.25, 'm3', DATE_SUB(CURDATE(), INTERVAL 4 MONTH), TRUE),
(@compteur_eau_1, @agent_1, 1235.75, 1245.20, 9.45, 'm3', DATE_SUB(CURDATE(), INTERVAL 3 MONTH), TRUE),
(@compteur_eau_1, @agent_1, 1245.20, 1248.80, 3.60, 'm3', DATE_SUB(CURDATE(), INTERVAL 2 MONTH), TRUE),
(@compteur_eau_1, @agent_1, 1248.80, 1250.50, 1.70, 'm3', DATE_SUB(CURDATE(), INTERVAL 1 MONTH), FALSE),

-- Compteur ELE000001 (Électricité)
(@compteur_elec_1, @agent_1, 3100.00, 3200.00, 100.00, 'kWh', DATE_SUB(CURDATE(), INTERVAL 6 MONTH), TRUE),
(@compteur_elec_1, @agent_1, 3200.00, 3280.75, 80.75, 'kWh', DATE_SUB(CURDATE(), INTERVAL 5 MONTH), TRUE),
(@compteur_elec_1, @agent_1, 3280.75, 3350.20, 69.45, 'kWh', DATE_SUB(CURDATE(), INTERVAL 4 MONTH), TRUE),
(@compteur_elec_1, @agent_1, 3350.20, 3410.45, 60.25, 'kWh', DATE_SUB(CURDATE(), INTERVAL 3 MONTH), TRUE),
(@compteur_elec_1, @agent_1, 3410.45, 3430.60, 20.15, 'kWh', DATE_SUB(CURDATE(), INTERVAL 2 MONTH), TRUE),
(@compteur_elec_1, @agent_1, 3430.60, 3450.75, 20.15, 'kWh', DATE_SUB(CURDATE(), INTERVAL 1 MONTH), FALSE),

-- Compteur EAU000002 (Eau)
(@compteur_eau_2, @agent_1, 900.00, 950.00, 50.00, 'm3', DATE_SUB(CURDATE(), INTERVAL 6 MONTH), TRUE),
(@compteur_eau_2, @agent_1, 950.00, 965.25, 15.25, 'm3', DATE_SUB(CURDATE(), INTERVAL 5 MONTH), TRUE),
(@compteur_eau_2, @agent_1, 965.25, 975.40, 10.15, 'm3', DATE_SUB(CURDATE(), INTERVAL 4 MONTH), TRUE),
(@compteur_eau_2, @agent_1, 975.40, 978.60, 3.20, 'm3', DATE_SUB(CURDATE(), INTERVAL 3 MONTH), TRUE),
(@compteur_eau_2, @agent_1, 978.60, 979.80, 1.20, 'm3', DATE_SUB(CURDATE(), INTERVAL 2 MONTH), TRUE),
(@compteur_eau_2, @agent_1, 979.80, 980.75, 0.95, 'm3', DATE_SUB(CURDATE(), INTERVAL 1 MONTH), FALSE),

-- Compteur ELE000002 (Électricité)
(@compteur_elec_2, @agent_1, 2550.00, 2650.00, 100.00, 'kWh', DATE_SUB(CURDATE(), INTERVAL 6 MONTH), TRUE),
(@compteur_elec_2, @agent_1, 2650.00, 2710.30, 60.30, 'kWh', DATE_SUB(CURDATE(), INTERVAL 5 MONTH), TRUE),
(@compteur_elec_2, @agent_1, 2710.30, 2760.45, 50.15, 'kWh', DATE_SUB(CURDATE(), INTERVAL 4 MONTH), TRUE),
(@compteur_elec_2, @agent_1, 2760.45, 2785.60, 25.15, 'kWh', DATE_SUB(CURDATE(), INTERVAL 3 MONTH), TRUE),
(@compteur_elec_2, @agent_1, 2785.60, 2795.75, 10.15, 'kWh', DATE_SUB(CURDATE(), INTERVAL 2 MONTH), TRUE),
(@compteur_elec_2, @agent_1, 2795.75, 2800.90, 5.15, 'kWh', DATE_SUB(CURDATE(), INTERVAL 1 MONTH), FALSE);

-- ============================================
-- Résumé des données créées
-- ============================================
SELECT
    (SELECT COUNT(*) FROM quartier) as 'Quartiers',
    (SELECT COUNT(*) FROM client) as 'Clients',
    (SELECT COUNT(*) FROM adresse) as 'Adresses',
    (SELECT COUNT(*) FROM agent) as 'Agents',
    (SELECT COUNT(*) FROM compteur) as 'Compteurs',
    (SELECT COUNT(*) FROM releve) as 'Relevés'
FROM dual;

SELECT 'Base de données SI Relevés peuplée avec succès !' as Status;
