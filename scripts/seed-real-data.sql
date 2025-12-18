-- ============================================
-- Script SQL pour peupler la base de données avec de vraies données
-- Clients et Adresses réalistes pour Rabat, Maroc
-- ============================================

USE si_releves;

-- ============================================
-- 1. Créer des Quartiers (si pas déjà créés)
-- ============================================
-- Supprimer les quartiers existants pour repartir à zéro (optionnel)
-- DELETE FROM quartier WHERE nom_quartier IN ('Agdal', 'Hay Riad', 'Souissi', 'Hassan', 'Akkari', 'Océan', 'Hay Nahda', 'Hay Riad Extension', 'Agdal Riyad', 'Touarga');

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

-- Récupérer les IDs des quartiers pour utilisation dans les adresses
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
-- 2. Créer des Clients avec de vrais noms marocains
-- ============================================
INSERT INTO client (id_client, nom, prenom, date_creation) VALUES
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
-- 3. Créer des Adresses réalistes à Rabat
-- ============================================
-- Utilisation des variables pour les IDs de quartiers

INSERT INTO adresse (id_client, id_quartier, adresse_complete, type_bien, code_postal, date_creation) VALUES
-- Agdal
('CLI001', @quartier_agdal, '123 Avenue Mohammed V, Agdal', 'Standard', '10000', NOW()),
('CLI002', @quartier_agdal, '45 Rue Al Fath, Agdal', 'Standard', '10000', NOW()),
('CLI003', @quartier_agdal, '78 Boulevard Zerktouni, Agdal', 'Immeuble', '10000', NOW()),
('CLI004', @quartier_agdal, '12 Avenue Allal Ben Abdellah, Agdal', 'Standard', '10000', NOW()),
('CLI005', @quartier_agdal, '89 Rue Ibn Sina, Agdal', 'Immeuble', '10000', NOW()),
('CLI006', @quartier_agdal, '34 Avenue Hassan II, Agdal', 'Standard', '10000', NOW()),
('CLI007', @quartier_agdal, '56 Rue Ibn Battuta, Agdal', 'Standard', '10000', NOW()),
('CLI008', @quartier_agdal, '101 Boulevard Mohammed VI, Agdal', 'Immeuble', '10000', NOW()),

-- Hay Riad
('CLI009', @quartier_hay_riad, '23 Avenue de France, Hay Riad', 'Standard', '10100', NOW()),
('CLI010', @quartier_hay_riad, '67 Rue Ibn Khaldoun, Hay Riad', 'Standard', '10100', NOW()),
('CLI011', @quartier_hay_riad, '145 Avenue Al Maghrib Al Arabi, Hay Riad', 'Immeuble', '10100', NOW()),
('CLI012', @quartier_hay_riad, '89 Rue Ibn Tachfine, Hay Riad', 'Standard', '10100', NOW()),
('CLI013', @quartier_hay_riad, '12 Avenue de la Victoire, Hay Riad', 'Standard', '10100', NOW()),
('CLI014', @quartier_hay_riad, '234 Boulevard Zerktouni, Hay Riad', 'Immeuble', '10100', NOW()),
('CLI015', @quartier_hay_riad, '56 Rue Al Andalous, Hay Riad', 'Standard', '10100', NOW()),
('CLI016', @quartier_hay_riad, '78 Avenue Mohammed VI, Hay Riad', 'Standard', '10100', NOW()),

-- Souissi
('CLI017', @quartier_souissi, '45 Avenue de la Résistance, Souissi', 'Standard', '10120', NOW()),
('CLI018', @quartier_souissi, '123 Rue Ibn Zaidoun, Souissi', 'Immeuble', '10120', NOW()),
('CLI019', @quartier_souissi, '67 Avenue Al Akkari, Souissi', 'Standard', '10120', NOW()),
('CLI020', @quartier_souissi, '234 Boulevard Zerktouni, Souissi', 'Standard', '10120', NOW()),
('CLI021', @quartier_souissi, '89 Rue Al Fath, Souissi', 'Immeuble', '10120', NOW()),
('CLI022', @quartier_souissi, '12 Avenue Hassan II, Souissi', 'Standard', '10120', NOW()),
('CLI023', @quartier_souissi, '156 Rue Ibn Sina, Souissi', 'Standard', '10120', NOW()),
('CLI024', @quartier_souissi, '78 Avenue Mohammed V, Souissi', 'Immeuble', '10120', NOW()),

-- Hassan
('CLI025', @quartier_hassan, '34 Avenue Allal Ben Abdellah, Hassan', 'Standard', '10030', NOW()),
('CLI026', @quartier_hassan, '123 Rue Ibn Battuta, Hassan', 'Standard', '10030', NOW()),
('CLI027', @quartier_hassan, '67 Boulevard Mohammed VI, Hassan', 'Immeuble', '10030', NOW()),
('CLI028', @quartier_hassan, '45 Rue Al Andalous, Hassan', 'Standard', '10030', NOW()),
('CLI029', @quartier_hassan, '189 Avenue de France, Hassan', 'Standard', '10030', NOW()),
('CLI030', @quartier_hassan, '234 Rue Ibn Khaldoun, Hassan', 'Immeuble', '10030', NOW()),
('CLI031', @quartier_hassan, '12 Avenue Al Maghrib Al Arabi, Hassan', 'Standard', '10030', NOW()),
('CLI032', @quartier_hassan, '78 Rue Ibn Tachfine, Hassan', 'Standard', '10030', NOW()),

-- Akkari
('CLI033', @quartier_akkari, '56 Avenue de la Victoire, Akkari', 'Standard', '10040', NOW()),
('CLI034', @quartier_akkari, '123 Rue Ibn Zaidoun, Akkari', 'Standard', '10040', NOW()),
('CLI035', @quartier_akkari, '67 Boulevard Zerktouni, Akkari', 'Immeuble', '10040', NOW()),
('CLI036', @quartier_akkari, '45 Avenue Al Akkari, Akkari', 'Standard', '10040', NOW()),
('CLI037', @quartier_akkari, '234 Rue Al Fath, Akkari', 'Standard', '10040', NOW()),
('CLI038', @quartier_akkari, '89 Avenue Hassan II, Akkari', 'Immeuble', '10040', NOW()),
('CLI039', @quartier_akkari, '12 Rue Ibn Sina, Akkari', 'Standard', '10040', NOW()),
('CLI040', @quartier_akkari, '156 Avenue Mohammed V, Akkari', 'Standard', '10040', NOW()),

-- Océan
('CLI041', @quartier_ocean, '78 Avenue de la Corniche, Océan', 'Standard', '10050', NOW()),
('CLI042', @quartier_ocean, '123 Rue de la Plage, Océan', 'Immeuble', '10050', NOW()),
('CLI043', @quartier_ocean, '45 Boulevard de l''Océan, Océan', 'Standard', '10050', NOW()),
('CLI044', @quartier_ocean, '67 Avenue des Nations Unies, Océan', 'Standard', '10050', NOW()),
('CLI045', @quartier_ocean, '234 Rue Ibn Zaidoun, Océan', 'Immeuble', '10050', NOW()),

-- Hay Nahda
('CLI046', @quartier_hay_nahda, '12 Avenue Al Nahda, Hay Nahda', 'Standard', '10110', NOW()),
('CLI047', @quartier_hay_nahda, '89 Rue Ibn Khaldoun, Hay Nahda', 'Standard', '10110', NOW()),
('CLI048', @quartier_hay_nahda, '156 Boulevard Mohammed VI, Hay Nahda', 'Immeuble', '10110', NOW()),
('CLI049', @quartier_hay_nahda, '34 Avenue de France, Hay Nahda', 'Standard', '10110', NOW()),
('CLI050', @quartier_hay_nahda, '123 Rue Al Andalous, Hay Nahda', 'Standard', '10110', NOW()),

-- Adresses supplémentaires pour certains clients (plusieurs adresses par client)
('CLI001', @quartier_agdal, '456 Avenue Hassan II, Agdal - Résidence secondaire', 'Immeuble', '10000', NOW()),
('CLI005', @quartier_hay_riad, '789 Rue Ibn Sina, Hay Riad - Bureau', 'Standard', '10100', NOW()),
('CLI010', @quartier_souissi, '321 Avenue Mohammed V, Souissi - Appartement', 'Standard', '10120', NOW()),
('CLI015', @quartier_hassan, '654 Boulevard Zerktouni, Hassan - Commerce', 'Standard', '10030', NOW()),
('CLI020', @quartier_akkari, '987 Rue Al Fath, Akkari - Villa', 'Standard', '10040', NOW());

-- ============================================
-- Vérification des données insérées
-- ============================================
SELECT 'Clients créés:' AS info, COUNT(*) AS total FROM client;
SELECT 'Adresses créées:' AS info, COUNT(*) AS total FROM adresse;
SELECT 'Quartiers disponibles:' AS info, COUNT(*) AS total FROM quartier;

-- Afficher quelques exemples
SELECT 'Exemples de clients:' AS info;
SELECT id_client, nom, prenom, date_creation FROM client LIMIT 10;

SELECT 'Exemples d''adresses:' AS info;
SELECT a.id_adresse, c.nom, c.prenom, q.nom_quartier, a.adresse_complete, a.type_bien 
FROM adresse a
JOIN client c ON a.id_client = c.id_client
JOIN quartier q ON a.id_quartier = q.id_quartier
LIMIT 10;
