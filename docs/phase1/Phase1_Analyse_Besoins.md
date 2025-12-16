# PHASE 1 : ANALYSE DES BESOINS
## Extraction automatique par IA (NLP + Analyse Sémantique)

**Projet :** Système de Gestion des Relevés de Compteurs d'Eau et d'Électricité  
**Client :** RABAT ENERGIE & EAU (REE)  
**Application concernée :** SI Relevés (Backoffice Web)  
**Date d'analyse :** 16 Décembre 2024

---

## 1. ACTEURS / RÔLES IDENTIFIÉS

### 1.1. Acteurs internes au système "SI Relevés"
| Acteur | Description | Permissions |
|--------|-------------|-------------|
| **Superadmin** | Administrateur système du backoffice | - Créer des comptes utilisateurs<br>- Affecter les rôles (Superadmin ou Utilisateur)<br>- Gérer les utilisateurs<br>- Réinitialiser les mots de passe |
| **Utilisateur** (Admin Backoffice) | Utilisateur du backoffice avec accès aux fonctionnalités métier | - Créer et gérer des compteurs<br>- Affecter agents aux quartiers<br>- Consulter tableaux de bord<br>- Gérer les relevés<br>- Exporter des rapports<br>- Envoyer données à la facturation |

### 1.2. Acteurs externes (hors système SI Relevés)
| Acteur | Description | Interaction avec SI Relevés |
|--------|-------------|----------------------------|
| **Agent de Terrain** | Personnel REE effectuant les relevés sur le terrain | - Reçoit les adresses à relever (via app mobile)<br>- Envoie les relevés au SI Relevés |
| **Chargé de clientèle** | Personnel du SI Commercial | - Ajoute des clients dans l'ERP<br>- Les données sont transmises au SI Relevés |
| **Admin RH** | Personnel du SI RH | - Ajoute des agents dans l'ERP<br>- Les données sont transmises au SI Relevés |
| **Service Comptabilité** | Service gérant la facturation | - Reçoit les données de consommation du SI Relevés |

---

## 2. ENTITÉS MÉTIER PRINCIPALES

### 2.1. Utilisateur Backoffice
**Description :** Compte d'accès au système SI Relevés

**Attributs :**
- Nom de famille (MAJUSCULES obligatoire)
- Prénom (Première lettre en majuscule)
- Email (pour envoi du mot de passe)
- Mot de passe (crypté en base)
- Rôle (Superadmin ou Utilisateur)
- Date d'ajout
- Dernière date de modification
- Statut de première connexion (pour forcer changement de mot de passe)

**Règles métier :**
- Mot de passe généré automatiquement (8 caractères min, complexité)
- Envoi par email (MailCatcher/MailHog)
- Changement obligatoire à la première connexion
- Formats : NOM DE FAMILLE en MAJUSCULES, Prénom en Nom Propre

---

### 2.2. Client
**Description :** Client REE abonné à l'eau et/ou l'électricité

**Attributs :**
- Identifiant client (provenant du SI Commercial)
- Liste des adresses associées

**Règles métier :**
- Créé dans le SI Commercial (ERP)
- Données transmises au SI Relevés via batch récurrent planifié

---

### 2.3. Adresse
**Description :** Localisation physique où sont installés les compteurs

**Attributs :**
- Identifiant adresse (optionnel)
- Adresse complète (texte)
- Quartier
- Type de bien (standard ou immeuble avec espaces communs)

**Règles métier :**
- Une adresse peut avoir jusqu'à 2 compteurs (Eau + Électricité)
- Pour les immeubles : 2 compteurs supplémentaires pour espaces communs
- Maximum 4 compteurs par adresse (2 standards + 2 espaces communs)
- Les adresses ne sont jamais modifiées une fois créées
- Un agent doit gérer ~300 adresses en moyenne

---

### 2.4. Agent de Terrain
**Description :** Personnel REE effectuant les relevés physiques des compteurs

**Attributs :**
- Nom de famille
- Prénom
- Numéro de téléphone personnel
- Numéro de téléphone professionnel
- Quartier d'affectation

**Règles métier :**
- Créé dans le SI RH (ERP)
- Données transmises au SI Relevés via batch récurrent planifié
- Peut être affecté à un seul quartier
- Plusieurs agents peuvent être affectés au même quartier
- Ratio optimal : 1 agent pour 300 adresses
- Performance moyenne : 50 compteurs relevés par jour
- Reçoit un téléphone avec l'app mobile pré-installée

---

### 2.5. Quartier
**Description :** Zone géographique de Rabat

**Attributs :**
- Nom du quartier
- Liste des adresses du quartier
- Liste des agents affectés

**Règles métier :**
- Plusieurs agents peuvent être affectés au même quartier
- Utilisé pour filtrage et répartition des agents

---

### 2.6. Compteur
**Description :** Dispositif de mesure de consommation d'eau ou d'électricité

**Attributs :**
- Identifiant unique (9 chiffres avec zéros précédents, ex: 000000001)
- Type (Eau | Électricité)
- Adresse associée
- Client associé (via l'adresse)
- Index actuel
- Date de la dernière relève

**Règles métier :**
- Identifiant généré automatiquement à la création
- Index initial = 0 à la création
- Associé à une seule adresse (relation permanente)
- Un compteur n'est jamais déplacé ou supprimé
- Maximum 2 compteurs standards par adresse (1 Eau + 1 Électricité)
- Pour immeubles : 2 compteurs supplémentaires pour espaces communs

---

### 2.7. Relevé
**Description :** Enregistrement d'une mesure de consommation effectuée par un agent

**Attributs :**
- Numéro de compteur (référence)
- Agent ayant effectué le relevé
- Date de la relève (jour + heure)
- Ancien index (index précédent du compteur)
- Nouvel index (valeur relevée)
- Consommation calculée (Nouvel index - Ancien index)
- Unité (m³ pour eau, kWh pour électricité)

**Règles métier :**
- Envoyé par l'application mobile vers le SI Relevés via web service
- Calcul automatique : Consommation = Relevé actuel - Relevé précédent
- Unités : m³ (eau), kWh (électricité)
- Les relevés sont envoyés quotidiennement par l'agent
- Une fois calculés, les relevés sont transmis au SI Facturation

---

## 3. RELATIONS ENTRE ENTITÉS

```
Client (1) ----possède----> (N) Adresse
Adresse (1) ----contient----> (2-4) Compteur
Compteur (1) ----génère----> (N) Relevé
Agent (N) ----affecté à----> (1) Quartier
Quartier (1) ----contient----> (N) Adresse
Agent (1) ----effectue----> (N) Relevé
Relevé (N) ----concerne----> (1) Compteur
UtilisateurBackoffice (N) ----gère----> (N) Compteur
Superadmin (N) ----administre----> (N) UtilisateurBackoffice
```

**Relations détaillées :**

1. **Client - Adresse** : Un client possède une ou plusieurs adresses (1,N)
2. **Adresse - Compteur** : Une adresse contient 2 à 4 compteurs (1,2-4)
3. **Adresse - Quartier** : Une adresse appartient à un quartier (N,1)
4. **Agent - Quartier** : Un agent est affecté à un quartier, plusieurs agents peuvent être dans le même quartier (N,1)
5. **Agent - Relevé** : Un agent effectue plusieurs relevés (1,N)
6. **Compteur - Relevé** : Un compteur génère plusieurs relevés dans le temps (1,N)
7. **Utilisateur - Rôle** : Un utilisateur backoffice a un rôle (Superadmin ou Utilisateur) (1,1)

---

## 4. CAS D'UTILISATION PAR ACTEUR

### 4.1. Superadmin

| ID | Cas d'utilisation | Description | Priorité |
|----|-------------------|-------------|----------|
| UC-SA-01 | Se connecter au système | Authentification avec login/mot de passe | Haute |
| UC-SA-02 | Créer un utilisateur | Ajouter un nouveau compte backoffice (Superadmin ou Utilisateur) | Haute |
| UC-SA-03 | Consulter liste des utilisateurs | Afficher tous les utilisateurs avec filtrage et tri | Moyenne |
| UC-SA-04 | Consulter détails d'un utilisateur | Voir et modifier les informations d'un utilisateur | Moyenne |
| UC-SA-05 | Modifier un utilisateur | Changer nom, prénom, ou rôle | Moyenne |
| UC-SA-06 | Réinitialiser mot de passe | Générer nouveau mot de passe et envoyer par email | Moyenne |
| UC-SA-07 | Changer son propre mot de passe | Modifier son mot de passe personnel | Basse |
| UC-SA-08 | Se déconnecter | Terminer la session | Basse |

---

### 4.2. Utilisateur (Admin Backoffice)

| ID | Cas d'utilisation | Description | Priorité |
|----|-------------------|-------------|----------|
| UC-U-01 | Se connecter au système | Authentification avec login/mot de passe | Haute |
| UC-U-02 | Consulter tableau de bord | Voir KPIs : taux de couverture, relevés par agent, évolution consommation | Haute |
| UC-U-03 | Créer un compteur | Ajouter un nouveau compteur et l'associer à une adresse | Haute |
| UC-U-04 | Consulter liste des compteurs | Afficher tous les compteurs | Moyenne |
| UC-U-05 | Consulter détails d'un compteur | Voir informations et historique des relevés d'un compteur | Moyenne |
| UC-U-06 | Affecter un agent à un quartier | Assigner un agent de terrain à un quartier spécifique | Haute |
| UC-U-07 | Consulter liste des agents | Afficher tous les agents avec filtrage par quartier | Moyenne |
| UC-U-08 | Consulter détails d'un agent | Voir informations et performances d'un agent | Moyenne |
| UC-U-09 | Modifier affectation d'un agent | Changer le quartier d'un agent | Moyenne |
| UC-U-10 | Consulter liste des relevés | Afficher tous les relevés avec filtres et tri | Haute |
| UC-U-11 | Consulter détails d'un relevé | Voir informations complètes d'un relevé | Moyenne |
| UC-U-12 | Générer rapport mensuel des relevés | Exporter PDF : répartition agents, moyennes par quartier | Moyenne |
| UC-U-13 | Générer rapport évolution consommation | Exporter PDF : tendances consommation eau/électricité | Moyenne |
| UC-U-14 | Envoyer données à la facturation | Transmettre consommations calculées au SI Facturation | Haute |
| UC-U-15 | Changer son mot de passe | Modifier son mot de passe personnel | Basse |
| UC-U-16 | Se déconnecter | Terminer la session | Basse |

---

### 4.3. Agent de Terrain (via Application Mobile)

| ID | Cas d'utilisation | Description | Priorité |
|----|-------------------|-------------|----------|
| UC-AT-01 | Récupérer adresses à relever | Obtenir liste des adresses du quartier non encore relevées | Haute |
| UC-AT-02 | Effectuer un relevé | Saisir le nouvel index d'un compteur | Haute |
| UC-AT-03 | Envoyer les relevés | Transmettre les relevés au SI Relevés | Haute |

**Note :** L'application mobile n'est pas dans le périmètre de développement, mais ses interactions avec le SI Relevés sont simulées.

---

## 5. BESOINS FONCTIONNELS DÉTAILLÉS

### 5.1. Gestion de l'authentification et des sessions

**BF-01 : Authentification utilisateur**
- Connexion par login/mot de passe
- Validation des identifiants contre la base de données
- Mots de passe cryptés en base
- Tokens JWT pour gestion des sessions (expiration 30 minutes)
- Déconnexion automatique après 10 minutes d'inactivité côté Frontend

**BF-02 : Gestion des mots de passe**
- Génération automatique à la création (8 caractères min avec complexité)
- Envoi par email via MailCatcher/MailHog
- Changement obligatoire à la première connexion
- Fonction de réinitialisation par Superadmin
- Fonction de changement par l'utilisateur lui-même

**BF-03 : Gestion des rôles et permissions**
- 2 rôles : Superadmin et Utilisateur
- Contrôle d'accès basé sur le rôle (RBAC)
- Restrictions API selon les rôles

---

### 5.2. Gestion des utilisateurs backoffice (Superadmin)

**BF-04 : Création d'utilisateur**
- Formulaire : Nom (MAJUSCULES), Prénom (Nom Propre), Rôle, Email
- Validation des formats de données
- Génération et envoi automatique du mot de passe

**BF-05 : Liste des utilisateurs**
- Affichage : Nom, Prénom, Rôle, Date d'ajout, Dernière modification
- Filtrage par rôle
- Tri par nom (défaut, ascendant) ou rôle

**BF-06 : Détails et modification d'utilisateur**
- Consultation et modification : Nom, Prénom, Rôle
- Fonction de réinitialisation du mot de passe

---

### 5.3. Gestion des compteurs

**BF-07 : Création de compteur**
- Sélection d'une adresse sans compteur (popup avec recherche et filtrage par quartier)
- Sélection du type (Eau ou Électricité)
- Génération automatique de l'identifiant (9 chiffres)
- Index initial = 0
- Validation : maximum 2 compteurs standards par adresse (ou 4 si immeuble)

**BF-08 : Liste des compteurs**
- Affichage : Identifiant, Adresse (tronquée)
- Navigation vers détails en cliquant sur un compteur
- Bouton "Ajouter" pour créer un compteur

**BF-09 : Détails d'un compteur**
- Affichage lecture seule : Identifiant, Adresse, Client, Index actuel, Date dernière relève
- Historique des 10 derniers relevés
- Bouton "Plus de détails" vers liste complète des relevés filtrée

---

### 5.4. Gestion des agents de terrain

**BF-10 : Réception des données agents depuis SI RH**
- Batch récurrent planifié
- Données reçues : Nom, Prénom, Téléphones (personnel et professionnel)

**BF-11 : Liste des agents**
- Affichage : Nom, Prénom, N° téléphone, Quartier d'affectation
- Filtrage par quartier
- Tri par nom (défaut, ascendant) ou quartier

**BF-12 : Détails et affectation d'agent**
- Affichage : Nom, Prénom, N° téléphone (lecture seule)
- Quartier : modifiable
- KPI : Nombre moyen de compteurs relevés par jour
- Graphique : Évolution "Relevés par jour" sur 3 derniers mois (ajustable de 1 semaine à 1 an)

---

### 5.5. Gestion des relevés

**BF-13 : Réception des relevés depuis l'application mobile**
- Web service pour recevoir : N° compteur, Nouvel index, Date et heure
- Envoi à chaque relevé effectué par l'agent

**BF-14 : Calcul automatique de la consommation**
- Formule : Consommation = Nouvel index - Ancien index
- Unités : m³ (eau), kWh (électricité)
- Mise à jour de l'index actuel du compteur

**BF-15 : Liste des relevés**
- Affichage : Date, Agent, Adresse (tronquée), Type compteur, Consommation
- Tri par date (défaut, descendant)
- Filtrage par : jour, quartier, agent, client, type de compteur
- Tri possible par : jour, quartier, agent

**BF-16 : Détails d'un relevé**
- Affichage lecture seule : Date, Agent, Adresse, Client, Identifiant compteur, Type, Ancien index, Nouvel index, Consommation calculée

**BF-17 : Envoi des données à la facturation**
- Web service automatique après calcul des consommations
- Données groupées : Identifiant client, Adresse, N° compteur, Date relève, Consommation eau, Consommation électricité

---

### 5.6. Tableaux de bord et KPIs

**BF-18 : Taux de couverture**
- Formule : (Compteurs relevés / Total compteurs) × 100
- Affichage : Pourcentage avec jauge visuelle
- Détail par quartier

**BF-19 : Relevés par jour par agent**
- Formule : Nombre moyen de compteurs relevés par agent
- Affichage : Nombre avec comparaison entre agents
- Détail par quartier et par agent

**BF-20 : Évolution de la consommation moyenne**
- Par type : eau (m³) et électricité (kWh)
- Évolution mensuelle avec graphique tendanciel
- Comparaison année N vs année N-1

---

### 5.7. Rapports et exports

**BF-21 : Rapport mensuel des relevés**
- Export PDF
- Contenu : Répartition agents par quartier, Nombre moyen relevés/agent/jour/quartier, Nombre relevés/quartier
- Objectif : Aide à la répartition des agents

**BF-22 : Rapport évolution de la consommation**
- Export PDF
- Contenu : Évolution mensuelle (eau/électricité), Graphique tendanciel, Comparaison N vs N-1
- Objectif : Étude des tendances de consommation

---

### 5.8. Intégrations externes

**BF-23 : Réception données clients depuis SI Commercial**
- Batch récurrent planifié
- Données reçues : Identifiant client, Adresses (complètes + identifiants)

**BF-24 : Communication avec l'application mobile**
- Web service OUT : Fournir liste des adresses à relever (appelé par l'app mobile)
- Web service IN : Recevoir les relevés (envoyés par l'app mobile)

**BF-25 : Envoi données vers SI Facturation**
- Web service automatique après calcul
- Données : Identifiant client, Adresse, N° compteur, Date relève, Consommations

---

## 6. BESOINS TECHNIQUES

### 6.1. Architecture technique

**BT-01 : Stack technologique Backend**
- Options : Laravel (PHP), Node.js, ou Spring Boot
- Architecture REST API

**BT-02 : Stack technologique Frontend**
- Options : Flutter, React, Angular, ou Vue
- Application web responsive

**BT-03 : Base de données**
- MySQL obligatoire

**BT-04 : Architecture globale**
- Séparation Frontend / Backend
- Communication via API REST
- Déploiement sur serveurs distincts possible

---

### 6.2. Sécurité

**BT-05 : Sécurité des mots de passe**
- Cryptage en base de données (bcrypt ou équivalent)
- Complexité minimale : 8 caractères
- Envoi sécurisé par email (MailCatcher/MailHog en développement)

**BT-06 : Gestion des sessions**
- Tokens JWT
- Expiration : 30 minutes
- Déconnexion automatique côté Frontend après 10 minutes d'inactivité

**BT-07 : Chiffrement des communications**
- HTTPS obligatoire
- Certificats auto-signés (self-signed certificates) acceptables en développement

**BT-08 : Chiffrement des données sensibles**
- Données sensibles chiffrées en base de données
- Mots de passe, tokens, informations personnelles

**BT-09 : Gestion des rôles et permissions**
- RBAC (Role-Based Access Control)
- Validation côté API des permissions selon le rôle

**BT-10 : Traçabilité et logs**
- Logs de connexion
- Logs des erreurs
- Messages d'erreur génériques pour l'utilisateur (pas de détails techniques exposés)

---

### 6.3. Flux de données et intégrations

**BT-11 : SI Commercial → SI Relevés**
- Mode : Batch récurrent planifié (cron job ou équivalent)
- Format : À définir (JSON recommandé)
- Simulation requise pour le projet

**BT-12 : SI RH → SI Relevés**
- Mode : Batch récurrent planifié
- Format : À définir (JSON recommandé)
- Simulation requise pour le projet

**BT-13 : SI Relevés ↔ Application Mobile**
- Mode : Web services REST
- OUT : Liste des adresses à relever (GET, appelé par mobile)
- IN : Envoi des relevés (POST, envoyé par mobile à chaque relevé)
- Simulation requise pour le projet

**BT-14 : SI Relevés → SI Facturation**
- Mode : Web service REST automatique
- Déclencheur : Après calcul des consommations
- Format : JSON avec données groupées
- Simulation requise pour le projet

---

### 6.4. Performance et fiabilité

**BT-15 : Gestion des erreurs**
- Gestion centralisée des exceptions
- Messages d'erreur génériques côté utilisateur
- Logs détaillés côté serveur

**BT-16 : Disponibilité**
- Déconnexion automatique après inactivité
- Reconnexion simple
- Sessions persistantes avec JWT

**BT-17 : Email**
- Utilisation de MailCatcher ou MailHog (pas de SMTP Google)
- Envoi asynchrone recommandé

---

## 7. CONTRAINTES ET RÈGLES MÉTIER

### 7.1. Contraintes sur les données

**CM-01 : Format des noms**
- NOM DE FAMILLE : MAJUSCULES obligatoires
  - Exemples valides : DRIOUECH, AIT MOHAMED, ES-SERGHINI
- Prénom : Nom Propre (première lettre majuscule)
  - Exemples valides : Ali, Fatima Ezzahra, Mohamed-Amine

**CM-02 : Identifiant compteur**
- Format : 9 chiffres avec zéros précédents
- Exemples : 000000001, 006738193
- Génération automatique

**CM-03 : Association compteur-adresse**
- Maximum 2 compteurs standards par adresse (1 Eau + 1 Électricité)
- Maximum 4 compteurs pour immeubles (+ 2 pour espaces communs)
- Association permanente (pas de déplacement)

---

### 7.2. Contraintes opérationnelles

**CM-04 : Performance agent**
- Performance moyenne : 50 compteurs relevés par jour
- Ratio optimal : 1 agent pour 300 adresses

**CM-05 : Organisation des relevés**
- Relevés mensuels
- Nouveau compteur ajouté avant la tournée de l'agent
- Pas de suppression de compteurs
- Pas de modification d'adresses existantes

**CM-06 : Gestion des tournées**
- Agent connaît déjà ses adresses à visiter
- Application mobile pré-installée sur téléphone professionnel
- Envoi quotidien des relevés au SI Relevés

---

### 7.3. Règles de calcul

**CM-07 : Calcul de consommation**
- Formule : Consommation = Nouvel index - Ancien index
- Unité eau : m³ (mètres cubes)
- Unité électricité : kWh (kilowattheures)

**CM-08 : KPI Taux de couverture**
- Formule : (Nombre de compteurs relevés / Nombre total de compteurs) × 100

**CM-09 : KPI Relevés par jour**
- Formule : Nombre moyen de compteurs relevés par agent

---

## 8. PRIORITÉS DES FONCTIONNALITÉS

### Priorité HAUTE (MVP - Minimum Viable Product)
1. Authentification et gestion des sessions
2. Gestion des utilisateurs (création, liste, modification)
3. Création et gestion des compteurs
4. Affectation des agents aux quartiers
5. Réception et affichage des relevés
6. Calcul automatique des consommations
7. Tableau de bord principal (KPIs essentiels)
8. Envoi des données à la facturation

### Priorité MOYENNE
1. Détails et historiques (compteurs, agents, relevés)
2. Filtres et tris avancés
3. Graphiques de performance des agents
4. Rapports PDF exportables
5. Gestion des quartiers

### Priorité BASSE
1. Changement de mot de passe utilisateur
2. Comparaisons année N vs N-1
3. Ajustements des périodes sur les graphiques
4. Optimisations UX avancées

---

## 9. RÉSUMÉ DES ENTITÉS ET ATTRIBUTS (POUR MCD)

| Entité | Attributs clés | Relations |
|--------|----------------|-----------|
| **UtilisateurBackoffice** | id, nom, prenom, email, password, role, date_ajout, date_modification, premiere_connexion | - |
| **Client** | id_client, nom, prenom | → Adresse (1,N) |
| **Adresse** | id_adresse, adresse_complete, quartier, type_bien | → Client (N,1)<br>→ Compteur (1,2-4)<br>→ Quartier (N,1) |
| **Quartier** | id_quartier, nom_quartier | → Adresse (1,N)<br>→ Agent (1,N) |
| **Agent** | id_agent, nom, prenom, tel_personnel, tel_professionnel, id_quartier | → Quartier (N,1)<br>→ Relevé (1,N) |
| **Compteur** | id_compteur (9 chiffres), type, index_actuel, date_derniere_releve, id_adresse | → Adresse (N,1)<br>→ Relevé (1,N) |
| **Relevé** | id_releve, date_releve, ancien_index, nouvel_index, consommation, unite, id_compteur, id_agent | → Compteur (N,1)<br>→ Agent (N,1) |

---

## 10. RÉSUMÉ DES FLUX DE DONNÉES

```
[SI Commercial (ERP)] --batch--> [SI Relevés] : Clients + Adresses
[SI RH (ERP)] --batch--> [SI Relevés] : Agents de terrain
[App Mobile] --WS GET--> [SI Relevés] : Liste adresses à relever
[App Mobile] --WS POST--> [SI Relevés] : Relevés effectués
[SI Relevés] --WS POST--> [SI Facturation] : Consommations calculées
```

---

## 11. LIVRABLES ATTENDUS POUR LA CONCEPTION (PROCHAINE PHASE)

D'après le cahier des charges, la phase de conception devra produire :

1. **Spécifications fonctionnelles détaillées** incluant :
   - Diagrammes de cas d'utilisation (Use Case Diagrams)
   - Modèle Conceptuel de Données (MCD)

2. **Cahier de tests**

3. **Architecture technique** incluant :
   - Modèle Logique de Données (MLD)
   - Liste des technologies/frameworks avec versions
   - Liste des outils utilisés
   - Schéma d'architecture de la solution

---

## CONCLUSION DE LA PHASE D'ANALYSE

Cette analyse a permis d'extraire automatiquement, grâce à l'IA (NLP + analyse sémantique), l'ensemble des besoins fonctionnels et techniques du cahier des charges.

**Éléments identifiés :**
- ✅ 8 acteurs (4 internes, 4 externes)
- ✅ 7 entités métier principales
- ✅ 25 cas d'utilisation détaillés
- ✅ 25 besoins fonctionnels
- ✅ 17 besoins techniques
- ✅ 9 contraintes et règles métier

**Prochaine étape :** Phase 2 - CONCEPTION
- Création des diagrammes de cas d'utilisation
- Élaboration du MCD
- Définition du MLD
- Rédaction du cahier de tests

---

**Document généré automatiquement par IA - Claude (Anthropic)**  
**Technique utilisée :** Natural Language Processing (NLP) + Analyse Sémantique  
**Source :** Cahier des charges - Système de Gestion des Relevés de Compteurs REE
