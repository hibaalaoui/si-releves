# PHASE 2 : CONCEPTION - MODÈLE LOGIQUE DE DONNÉES (MLD)

**Projet :** Système de Gestion des Relevés de Compteurs d'Eau et d'Électricité  
**Client :** RABAT ENERGIE & EAU (REE)  
**Application concernée :** SI Relevés (Backoffice Web)  
**Date de conception :** 16 Décembre 2024

---

## INTRODUCTION

Ce document présente le Modèle Logique de Données (MLD) du système "SI Relevés", résultant de la transformation du MCD en structure de base de données MySQL.

Le MLD a été généré automatiquement par **IA générative** et comprend :
- Le script SQL complet de création de la base de données
- Le mapping MCD → MLD
- Les triggers et procédures stockées
- Les vues métier
- Les recommandations d'implémentation

---

## 1. TRANSFORMATION MCD → MLD

### 1.1. Règles de transformation appliquées

| Élément MCD | Transformation MLD | Exemple |
|-------------|-------------------|---------|
| **Entité** | → Table | Entité "Compteur" → Table `compteur` |
| **Attribut** | → Colonne | Attribut "nom" → Colonne `nom VARCHAR(100)` |
| **Identifiant** | → Clé primaire (PRIMARY KEY) | id_compteur → `PRIMARY KEY (id_compteur)` |
| **Association 1:N** | → Clé étrangère dans la table côté N | Client-Adresse → `id_client` dans table `adresse` |
| **Association N:M** | → Table de liaison | (Aucune dans ce modèle) |
| **Contrainte d'unicité** | → UNIQUE KEY | email → `UNIQUE KEY (email)` |
| **Contrainte de domaine** | → CHECK ou ENUM | type → `ENUM('Eau', 'Electricite')` |

### 1.2. Nomenclature adoptée

**Convention de nommage :**
- Tables : `nom_table` (minuscules, snake_case)
- Colonnes : `nom_colonne` (minuscules, snake_case)
- Clés primaires : `id_nom_table` (préfixe "id_")
- Clés étrangères : `id_table_referencee` (même nom que la PK référencée)
- Contraintes FK : `fk_table_source_table_cible`
- Contraintes CHECK : `chk_table_attribut`
- Index : `idx_table_colonne`
- Contraintes UNIQUE : `uk_table_colonne`
- Triggers : `trg_description_action`
- Procédures : `sp_description_action`
- Vues : `v_description`

---

## 2. MAPPING DÉTAILLÉ MCD → MLD

### 2.1. Entité "UtilisateurBackoffice" → Table `utilisateur_backoffice`

| Attribut MCD | Colonne MLD | Type MySQL | Contraintes |
|--------------|-------------|------------|-------------|
| id_utilisateur (PK) | id_utilisateur | INT | PRIMARY KEY, AUTO_INCREMENT |
| nom | nom | VARCHAR(100) | NOT NULL |
| prenom | prenom | VARCHAR(100) | NOT NULL |
| email | email | VARCHAR(255) | NOT NULL, UNIQUE |
| password_hash | password_hash | VARCHAR(255) | NOT NULL |
| role | role | ENUM('Superadmin', 'Utilisateur') | NOT NULL, DEFAULT 'Utilisateur' |
| premiere_connexion | premiere_connexion | BOOLEAN | NOT NULL, DEFAULT TRUE |
| date_ajout | date_ajout | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| date_modification | date_modification | DATETIME | NULL, ON UPDATE CURRENT_TIMESTAMP |
| actif | actif | BOOLEAN | NOT NULL, DEFAULT TRUE |

**Index créés :**
- PRIMARY KEY : `id_utilisateur`
- UNIQUE KEY : `email`
- INDEX : `role`, `actif`

---

### 2.2. Entité "Client" → Table `client`

| Attribut MCD | Colonne MLD | Type MySQL | Contraintes |
|--------------|-------------|------------|-------------|
| id_client (PK) | id_client | VARCHAR(50) | PRIMARY KEY |
| nom | nom | VARCHAR(100) | NOT NULL |
| prenom | prenom | VARCHAR(100) | NOT NULL |
| date_creation | date_creation | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP |

**Index créés :**
- PRIMARY KEY : `id_client`

**Note :** `id_client` est de type VARCHAR car il provient du système externe (SI Commercial).

---

### 2.3. Entité "Quartier" → Table `quartier`

| Attribut MCD | Colonne MLD | Type MySQL | Contraintes |
|--------------|-------------|------------|-------------|
| id_quartier (PK) | id_quartier | INT | PRIMARY KEY, AUTO_INCREMENT |
| nom_quartier | nom_quartier | VARCHAR(100) | NOT NULL, UNIQUE |
| ville | ville | VARCHAR(100) | NOT NULL, DEFAULT 'Rabat' |

**Index créés :**
- PRIMARY KEY : `id_quartier`
- UNIQUE KEY : `nom_quartier`

---

### 2.4. Entité "Adresse" → Table `adresse`

| Attribut MCD | Colonne MLD | Type MySQL | Contraintes |
|--------------|-------------|------------|-------------|
| id_adresse (PK) | id_adresse | INT | PRIMARY KEY, AUTO_INCREMENT |
| id_client (FK) | id_client | VARCHAR(50) | NOT NULL, FK → client(id_client) |
| id_quartier (FK) | id_quartier | INT | NOT NULL, FK → quartier(id_quartier) |
| adresse_complete | adresse_complete | TEXT | NOT NULL |
| type_bien | type_bien | ENUM('Standard', 'Immeuble') | NOT NULL, DEFAULT 'Standard' |
| code_postal | code_postal | VARCHAR(10) | NULL |
| date_creation | date_creation | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP |

**Index créés :**
- PRIMARY KEY : `id_adresse`
- INDEX : `id_client`, `id_quartier`

**Contraintes d'intégrité référentielle :**
```sql
FOREIGN KEY (id_client) REFERENCES client(id_client)
    ON DELETE RESTRICT ON UPDATE CASCADE
FOREIGN KEY (id_quartier) REFERENCES quartier(id_quartier)
    ON DELETE RESTRICT ON UPDATE CASCADE
```

---

### 2.5. Entité "Agent" → Table `agent`

| Attribut MCD | Colonne MLD | Type MySQL | Contraintes |
|--------------|-------------|------------|-------------|
| id_agent (PK) | id_agent | VARCHAR(50) | PRIMARY KEY |
| id_quartier (FK) | id_quartier | INT | NOT NULL, FK → quartier(id_quartier) |
| nom | nom | VARCHAR(100) | NOT NULL |
| prenom | prenom | VARCHAR(100) | NOT NULL |
| tel_personnel | tel_personnel | VARCHAR(20) | NULL |
| tel_professionnel | tel_professionnel | VARCHAR(20) | NOT NULL |
| date_affectation | date_affectation | DATETIME | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| actif | actif | BOOLEAN | NOT NULL, DEFAULT TRUE |

**Index créés :**
- PRIMARY KEY : `id_agent`
- INDEX : `id_quartier`, `actif`

**Contraintes d'intégrité référentielle :**
```sql
FOREIGN KEY (id_quartier) REFERENCES quartier(id_quartier)
    ON DELETE RESTRICT ON UPDATE CASCADE
```

**Note :** `id_agent` est de type VARCHAR car il provient du système externe (SI RH).

---

### 2.6. Entité "Compteur" → Table `compteur`

| Attribut MCD | Colonne MLD | Type MySQL | Contraintes |
|--------------|-------------|------------|-------------|
| id_compteur (PK) | id_compteur | CHAR(9) | PRIMARY KEY |
| id_adresse (FK) | id_adresse | INT | NOT NULL, FK → adresse(id_adresse) |
| type | type | ENUM('Eau', 'Electricite') | NOT NULL |
| index_actuel | index_actuel | DECIMAL(10,2) | NOT NULL, DEFAULT 0.00, CHECK >= 0 |
| date_installation | date_installation | DATE | NOT NULL, DEFAULT CURRENT_DATE |
| date_derniere_releve | date_derniere_releve | DATETIME | NULL |
| pour_espaces_communs | pour_espaces_communs | BOOLEAN | NOT NULL, DEFAULT FALSE |
| actif | actif | BOOLEAN | NOT NULL, DEFAULT TRUE |

**Index créés :**
- PRIMARY KEY : `id_compteur`
- INDEX : `id_adresse`, `type`, `actif`
- UNIQUE KEY : `(id_adresse, type, pour_espaces_communs)`

**Contraintes d'intégrité référentielle :**
```sql
FOREIGN KEY (id_adresse) REFERENCES adresse(id_adresse)
    ON DELETE RESTRICT ON UPDATE CASCADE
```

**Contraintes CHECK :**
```sql
CHECK (type IN ('Eau', 'Electricite'))
CHECK (index_actuel >= 0)
```

**Contrainte d'unicité composite :**
Garantit qu'une adresse ne peut avoir qu'un seul compteur d'un type donné (Eau standard, Eau espaces communs, etc.).

---

### 2.7. Entité "Relevé" → Table `releve`

| Attribut MCD | Colonne MLD | Type MySQL | Contraintes |
|--------------|-------------|------------|-------------|
| id_releve (PK) | id_releve | INT | PRIMARY KEY, AUTO_INCREMENT |
| id_compteur (FK) | id_compteur | CHAR(9) | NOT NULL, FK → compteur(id_compteur) |
| id_agent (FK) | id_agent | VARCHAR(50) | NOT NULL, FK → agent(id_agent) |
| date_releve | date_releve | DATETIME | NOT NULL |
| ancien_index | ancien_index | DECIMAL(10,2) | NOT NULL |
| nouvel_index | nouvel_index | DECIMAL(10,2) | NOT NULL, CHECK >= ancien_index |
| consommation | consommation | DECIMAL(10,2) | NOT NULL, CHECK >= 0 |
| unite | unite | ENUM('m3', 'kWh') | NOT NULL |
| envoye_facturation | envoye_facturation | BOOLEAN | NOT NULL, DEFAULT FALSE |
| date_envoi_facturation | date_envoi_facturation | DATETIME | NULL |

**Index créés :**
- PRIMARY KEY : `id_releve`
- INDEX : `id_compteur`, `id_agent`, `date_releve`, `envoye_facturation`
- INDEX COMPOSITE : `(id_compteur, date_releve DESC)`

**Contraintes d'intégrité référentielle :**
```sql
FOREIGN KEY (id_compteur) REFERENCES compteur(id_compteur)
    ON DELETE RESTRICT ON UPDATE CASCADE
FOREIGN KEY (id_agent) REFERENCES agent(id_agent)
    ON DELETE RESTRICT ON UPDATE CASCADE
```

**Contraintes CHECK :**
```sql
CHECK (nouvel_index >= ancien_index)
CHECK (consommation >= 0)
CHECK (unite IN ('m3', 'kWh'))
```

---

## 3. DIAGRAMME MLD (STRUCTURE PHYSIQUE)

### 3.1. Schéma relationnel

```
QUARTIER (id_quartier PK, nom_quartier UK, ville)
  ↓ (1,N)
ADRESSE (id_adresse PK, id_client FK, id_quartier FK, adresse_complete, type_bien, code_postal, date_creation)
  ↓ (1,4)
COMPTEUR (id_compteur PK, id_adresse FK, type, index_actuel, date_installation, date_derniere_releve, pour_espaces_communs, actif)
  ↓ (1,N)
RELEVE (id_releve PK, id_compteur FK, id_agent FK, date_releve, ancien_index, nouvel_index, consommation, unite, envoye_facturation, date_envoi_facturation)

CLIENT (id_client PK, nom, prenom, date_creation)
  ↓ (1,N)
ADRESSE (voir ci-dessus)

QUARTIER (voir ci-dessus)
  ↓ (1,N)
AGENT (id_agent PK, id_quartier FK, nom, prenom, tel_personnel, tel_professionnel, date_affectation, actif)
  ↓ (1,N)
RELEVE (voir ci-dessus)

UTILISATEUR_BACKOFFICE (id_utilisateur PK, nom, prenom, email UK, password_hash, role, premiere_connexion, date_ajout, date_modification, actif)
```

---

## 4. TRIGGERS MÉTIER

### 4.1. Trigger : Validation du nombre de compteurs par adresse

**Nom :** `trg_check_compteurs_max_per_adresse`

**Type :** BEFORE INSERT

**Table :** `compteur`

**Objectif :** Valider que le nombre maximum de compteurs par adresse n'est pas dépassé selon le type de bien.

**Règles :**
- Adresse standard : maximum 2 compteurs (1 Eau + 1 Électricité)
- Immeuble : maximum 4 compteurs (2 standards + 2 espaces communs)
- Les compteurs pour espaces communs sont réservés aux immeubles

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 205-250

---

### 4.2. Trigger : Mise à jour automatique de l'index du compteur

**Nom :** `trg_update_compteur_after_releve`

**Type :** AFTER INSERT

**Table :** `releve`

**Objectif :** Mettre à jour automatiquement l'index actuel du compteur et la date du dernier relevé.

**Actions :**
1. Récupère le `nouvel_index` du relevé inséré
2. Met à jour `index_actuel` du compteur
3. Met à jour `date_derniere_releve` du compteur

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 255-270

---

### 4.3. Trigger : Calcul automatique de la consommation

**Nom :** `trg_calculate_consommation`

**Type :** BEFORE INSERT

**Table :** `releve`

**Objectif :** Calculer automatiquement la consommation et définir l'unité selon le type de compteur.

**Actions :**
1. Récupère le type de compteur (Eau ou Électricité)
2. Calcule : `consommation = nouvel_index - ancien_index`
3. Définit l'unité : "m3" pour Eau, "kWh" pour Électricité

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 275-295

---

### 4.4. Trigger : Récupération automatique de l'ancien index

**Nom :** `trg_set_ancien_index`

**Type :** BEFORE INSERT

**Table :** `releve`

**Objectif :** Récupérer automatiquement l'ancien index du compteur si non fourni.

**Actions :**
1. Si `ancien_index` est NULL ou 0
2. Récupère `index_actuel` du compteur
3. Assigne cette valeur à `ancien_index`

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 300-320

---

## 5. PROCÉDURES STOCKÉES

### 5.1. Procédure : Générer un ID de compteur

**Nom :** `sp_generate_compteur_id`

**Paramètres :**
- OUT `new_id` CHAR(9) : Nouvel identifiant généré

**Objectif :** Générer automatiquement un identifiant de compteur au format 9 chiffres avec zéros précédents.

**Algorithme :**
1. Récupère le dernier ID numérique (MAX)
2. Incrémente de 1
3. Formate avec LPAD pour obtenir 9 chiffres

**Exemple :**
```sql
CALL sp_generate_compteur_id(@new_id);
-- Résultat : '000000042'
```

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 330-350

---

### 5.2. Procédure : Calculer le taux de couverture

**Nom :** `sp_calculate_taux_couverture`

**Paramètres :**
- IN `p_date_debut` DATE : Date de début de la période
- IN `p_date_fin` DATE : Date de fin de la période
- IN `p_id_quartier` INT : Quartier (NULL = tous)
- OUT `p_taux_couverture` DECIMAL(5,2) : Taux calculé (%)

**Objectif :** Calculer le taux de couverture des relevés sur une période donnée.

**Formule :** `(Compteurs relevés / Total compteurs) × 100`

**Exemple :**
```sql
CALL sp_calculate_taux_couverture('2024-12-01', '2024-12-31', NULL, @taux);
SELECT @taux;
-- Résultat : 87.50
```

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 355-395

---

### 5.3. Procédure : Calculer les relevés moyens par jour par agent

**Nom :** `sp_calculate_releves_par_jour_agent`

**Paramètres :**
- IN `p_id_agent` VARCHAR(50) : Identifiant de l'agent
- IN `p_date_debut` DATE : Date de début
- IN `p_date_fin` DATE : Date de fin
- OUT `p_moyenne` DECIMAL(10,2) : Moyenne calculée

**Objectif :** Calculer le nombre moyen de relevés effectués par jour par un agent.

**Formule :** `Nombre de relevés / Nombre de jours`

**Exemple :**
```sql
CALL sp_calculate_releves_par_jour_agent('AG001', '2024-12-01', '2024-12-31', @moy);
SELECT @moy;
-- Résultat : 48.50
```

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 400-430

---

## 6. VUES MÉTIER

### 6.1. Vue : Détails complets des relevés

**Nom :** `v_releves_detail`

**Objectif :** Fournir une vue enrichie des relevés avec toutes les informations liées (agent, compteur, adresse, client, quartier).

**Colonnes :**
- Informations du relevé : id_releve, date_releve, ancien_index, nouvel_index, consommation, unite
- Informations du compteur : id_compteur, type_compteur, pour_espaces_communs
- Informations de l'agent : id_agent, agent_nom, agent_prenom
- Informations de l'adresse : id_adresse, adresse_complete, type_bien
- Informations du quartier : id_quartier, nom_quartier
- Informations du client : id_client, client_nom, client_prenom

**Usage :**
```sql
SELECT * FROM v_releves_detail
WHERE nom_quartier = 'Agdal'
AND date_releve >= '2024-12-01'
ORDER BY date_releve DESC;
```

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 445-475

---

### 6.2. Vue : Statistiques des agents par quartier

**Nom :** `v_agents_stats`

**Objectif :** Fournir des statistiques agrégées par quartier pour l'aide à la décision.

**Colonnes :**
- id_quartier
- nom_quartier
- nb_agents : Nombre d'agents actifs
- nb_adresses : Nombre d'adresses
- nb_compteurs : Nombre de compteurs actifs
- ratio_adresses_par_agent : Ratio moyen

**Usage :**
```sql
SELECT * FROM v_agents_stats
WHERE ratio_adresses_par_agent > 300
ORDER BY ratio_adresses_par_agent DESC;
```

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 480-495

---

### 6.3. Vue : Compteurs sans relevé récent

**Nom :** `v_compteurs_sans_releve_recent`

**Objectif :** Identifier les compteurs qui n'ont pas été relevés depuis plus de 40 jours.

**Colonnes :**
- id_compteur
- type
- date_derniere_releve
- jours_depuis_dernier_releve
- adresse_complete
- nom_quartier
- client_nom, client_prenom

**Usage :**
```sql
SELECT * FROM v_compteurs_sans_releve_recent
WHERE jours_depuis_dernier_releve > 60;
```

**Implémentation :** Voir `MLD_Create_Database.sql` lignes 500-525

---

## 7. STRATÉGIE D'INDEXATION

### 7.1. Index créés automatiquement

| Table | Index | Type | Colonnes |
|-------|-------|------|----------|
| quartier | PRIMARY | PRIMARY KEY | id_quartier |
| quartier | uk_quartier_nom | UNIQUE | nom_quartier |
| client | PRIMARY | PRIMARY KEY | id_client |
| adresse | PRIMARY | PRIMARY KEY | id_adresse |
| adresse | idx_adresse_client | INDEX | id_client |
| adresse | idx_adresse_quartier | INDEX | id_quartier |
| agent | PRIMARY | PRIMARY KEY | id_agent |
| agent | idx_agent_quartier | INDEX | id_quartier |
| agent | idx_agent_actif | INDEX | actif |
| compteur | PRIMARY | PRIMARY KEY | id_compteur |
| compteur | uk_compteur_adresse_type | UNIQUE | (id_adresse, type, pour_espaces_communs) |
| compteur | idx_compteur_adresse | INDEX | id_adresse |
| compteur | idx_compteur_type | INDEX | type |
| compteur | idx_compteur_actif | INDEX | actif |
| releve | PRIMARY | PRIMARY KEY | id_releve |
| releve | idx_releve_compteur | INDEX | id_compteur |
| releve | idx_releve_agent | INDEX | id_agent |
| releve | idx_releve_date | INDEX | date_releve |
| releve | idx_releve_envoye_facturation | INDEX | envoye_facturation |
| releve | idx_releve_compteur_date | INDEX | (id_compteur, date_releve DESC) |
| utilisateur_backoffice | PRIMARY | PRIMARY KEY | id_utilisateur |
| utilisateur_backoffice | uk_user_email | UNIQUE | email |
| utilisateur_backoffice | idx_user_role | INDEX | role |
| utilisateur_backoffice | idx_user_actif | INDEX | actif |

### 7.2. Justification des index

**Index sur les clés étrangères :**
- Améliore les performances des JOIN
- Accélère les vérifications d'intégrité référentielle
- Optimise les DELETE/UPDATE en CASCADE

**Index sur les colonnes de filtrage fréquent :**
- `actif` : Filtrage des entités actives/inactives
- `date_releve` : Requêtes par période
- `envoye_facturation` : Filtrage des relevés à envoyer
- `role` : Filtrage par rôle d'utilisateur

**Index composites :**
- `(id_compteur, date_releve DESC)` : Optimise la récupération de l'historique des relevés d'un compteur
- `(id_adresse, type, pour_espaces_communs)` : Garantit l'unicité métier

---

## 8. SÉCURITÉ ET DROITS D'ACCÈS

### 8.1. Utilisateurs de base de données recommandés

```sql
-- Utilisateur pour l'application backend (API)
CREATE USER 'si_releves_app'@'%' IDENTIFIED BY 'MotDePasseSecurise123!';
GRANT SELECT, INSERT, UPDATE ON si_releves.* TO 'si_releves_app'@'%';
GRANT DELETE ON si_releves.releve TO 'si_releves_app'@'%'; -- Si nécessaire
GRANT EXECUTE ON si_releves.* TO 'si_releves_app'@'%';

-- Utilisateur pour les batchs de synchronisation
CREATE USER 'si_releves_batch'@'localhost' IDENTIFIED BY 'MotDePasseBatch456!';
GRANT SELECT, INSERT, UPDATE ON si_releves.client TO 'si_releves_batch'@'localhost';
GRANT SELECT, INSERT, UPDATE ON si_releves.adresse TO 'si_releves_batch'@'localhost';
GRANT SELECT, INSERT, UPDATE ON si_releves.agent TO 'si_releves_batch'@'localhost';

-- Utilisateur en lecture seule pour le reporting
CREATE USER 'si_releves_read'@'%' IDENTIFIED BY 'MotDePasseRead789!';
GRANT SELECT ON si_releves.* TO 'si_releves_read'@'%';
```

### 8.2. Chiffrement des données sensibles

**Données à chiffrer :**
- `utilisateur_backoffice.password_hash` : Utiliser bcrypt (déjà hashé côté application)
- `agent.tel_personnel` : Chiffrement optionnel selon politique de sécurité

**Implémentation recommandée :**
- Utiliser le chiffrement au niveau applicatif (avant insertion)
- Considérer MySQL Enterprise Encryption pour chiffrement transparent

---

## 9. MAINTENANCE ET OPTIMISATION

### 9.1. Archivage des données

**Table `releve` - Croissance rapide :**
```sql
-- Créer une table d'archives pour les relevés > 2 ans
CREATE TABLE releve_archive LIKE releve;

-- Procédure d'archivage mensuel
DELIMITER //
CREATE PROCEDURE sp_archive_old_releves()
BEGIN
    -- Déplacer les relevés de plus de 2 ans
    INSERT INTO releve_archive
    SELECT * FROM releve
    WHERE date_releve < DATE_SUB(CURDATE(), INTERVAL 2 YEAR);
    
    -- Supprimer de la table principale
    DELETE FROM releve
    WHERE date_releve < DATE_SUB(CURDATE(), INTERVAL 2 YEAR);
END//
DELIMITER ;
```

### 9.2. Maintenance régulière

```sql
-- Optimiser les tables mensuellement
OPTIMIZE TABLE releve;
OPTIMIZE TABLE compteur;

-- Analyser les tables pour mettre à jour les statistiques
ANALYZE TABLE releve;
ANALYZE TABLE compteur;

-- Vérifier l'intégrité
CHECK TABLE releve;
CHECK TABLE compteur;
```

### 9.3. Monitoring des performances

**Requêtes à surveiller :**
```sql
-- Requêtes lentes (à activer dans my.cnf)
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;

-- Identifier les index non utilisés
SELECT * FROM sys.schema_unused_indexes
WHERE object_schema = 'si_releves';

-- Identifier les tables fragmentées
SELECT table_name, data_free, data_free / (data_length + index_length) AS fragmentation
FROM information_schema.tables
WHERE table_schema = 'si_releves'
AND data_free > 0
ORDER BY fragmentation DESC;
```

---

## 10. CONFIGURATION RECOMMANDÉE

### 10.1. Paramètres MySQL (my.cnf)

```ini
[mysqld]
# Encodage
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci

# InnoDB
innodb_buffer_pool_size=2G
innodb_log_file_size=512M
innodb_flush_log_at_trx_commit=2

# Logs
slow_query_log=ON
long_query_time=2
log_queries_not_using_indexes=ON

# Connexions
max_connections=200
```

### 10.2. Sauvegarde

**Stratégie recommandée :**
- Sauvegarde complète quotidienne (3h du matin)
- Sauvegarde incrémentale toutes les 6 heures
- Rétention : 7 jours complets + 4 semaines + 12 mois

**Script de sauvegarde :**
```bash
#!/bin/bash
mysqldump --single-transaction --routines --triggers \
  --databases si_releves \
  | gzip > /backups/si_releves_$(date +%Y%m%d_%H%M%S).sql.gz
```

---

## 11. FICHIER SQL LIVRÉ

**Fichier :** `MLD_Create_Database.sql`

**Contenu :**
- ✅ Création de la base de données
- ✅ Création des 7 tables avec contraintes
- ✅ 4 triggers métier
- ✅ 3 procédures stockées
- ✅ 3 vues métier
- ✅ Données de test (quartiers + superadmin)
- ✅ Commentaires détaillés

**Taille :** ~500 lignes de SQL

**Utilisation :**
```bash
mysql -u root -p < MLD_Create_Database.sql
```

---

## CONCLUSION

Le Modèle Logique de Données présenté dans ce document a été généré automatiquement par **IA générative** à partir du MCD.

**Caractéristiques du MLD :**
- ✅ 7 tables MySQL avec InnoDB
- ✅ 24 index (PRIMARY, UNIQUE, INDEX)
- ✅ 6 contraintes de clés étrangères
- ✅ 9 contraintes CHECK
- ✅ 4 triggers métier
- ✅ 3 procédures stockées
- ✅ 3 vues métier
- ✅ Script SQL complet et prêt à l'emploi

**Compatibilité :**
- MySQL 8.0+
- MariaDB 10.5+

**Prochaines étapes :**
- Cahier de tests (Phase 2.4)
- Architecture technique (Phase 2.5)

---

**Document généré automatiquement par IA - Claude (Anthropic)**  
**Technique utilisée :** IA générative pour transformation MCD→MLD et génération de scripts SQL  
**Base de données cible :** MySQL 8.0+
