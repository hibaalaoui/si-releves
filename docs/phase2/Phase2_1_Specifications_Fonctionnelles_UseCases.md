# PHASE 2 : CONCEPTION - SPÉCIFICATIONS FONCTIONNELLES
## Diagrammes de Cas d'Utilisation

**Projet :** Système de Gestion des Relevés de Compteurs d'Eau et d'Électricité  
**Client :** RABAT ENERGIE & EAU (REE)  
**Application concernée :** SI Relevés (Backoffice Web)  
**Date de conception :** 16 Décembre 2024

---

## INTRODUCTION

Ce document présente les spécifications fonctionnelles détaillées du système "SI Relevés" sous forme de diagrammes de cas d'utilisation (Use Case Diagrams).

Les diagrammes ont été générés automatiquement par **IA générative** en utilisant PlantUML et Mermaid, conformément aux exigences de la phase de conception du projet.

---

## 1. DIAGRAMME DE CAS D'UTILISATION GLOBAL

### 1.1. Vue d'ensemble

Le diagramme global présente l'ensemble des acteurs et leurs interactions avec le système SI Relevés.

### 1.2. Acteurs identifiés

| Acteur | Type | Description |
|--------|------|-------------|
| **Superadmin** | Interne | Administrateur système gérant les comptes utilisateurs du backoffice |
| **Utilisateur (Admin Backoffice)** | Interne | Utilisateur métier gérant les compteurs, agents, relevés et rapports |
| **Agent de Terrain** | Externe | Personnel effectuant les relevés via application mobile |
| **SI Commercial (ERP)** | Système externe | Système ERP fournissant les données clients et adresses |
| **SI RH (ERP)** | Système externe | Système ERP fournissant les données des agents de terrain |
| **SI Facturation (ERP)** | Système externe | Système ERP recevant les données de consommation pour facturation |

### 1.3. Packages fonctionnels

Le système est organisé en 7 packages fonctionnels majeurs :

1. **Authentification** (commun à tous les utilisateurs)
2. **Gestion des utilisateurs** (Superadmin uniquement)
3. **Gestion des compteurs** (Utilisateur)
4. **Gestion des agents de terrain** (Utilisateur)
5. **Gestion des relevés** (Utilisateur)
6. **Tableaux de bord et rapports** (Utilisateur)
7. **Intégrations externes** (Système)

### 1.4. Diagramme

Les fichiers PlantUML ont été générés pour une visualisation détaillée :
- `UseCase_Global.puml` : Vue globale du système
- `UseCase_Superadmin.puml` : Vue détaillée Superadmin
- `UseCase_Utilisateur.puml` : Vue détaillée Utilisateur

---

## 2. CAS D'UTILISATION - SUPERADMIN

### 2.1. Rôle et responsabilités

Le **Superadmin** est responsable de la gestion administrative du système, notamment :
- Création et gestion des comptes utilisateurs du backoffice
- Attribution des rôles (Superadmin ou Utilisateur)
- Réinitialisation des mots de passe
- Maintenance des accès au système

### 2.2. Liste des cas d'utilisation

| ID | Cas d'utilisation | Priorité | Complexité |
|----|-------------------|----------|------------|
| UC-SA-01 | Se connecter au système | Haute | Faible |
| UC-SA-02 | Créer un utilisateur | Haute | Moyenne |
| UC-SA-03 | Consulter liste des utilisateurs | Moyenne | Faible |
| UC-SA-04 | Consulter détails d'un utilisateur | Moyenne | Faible |
| UC-SA-05 | Modifier un utilisateur | Moyenne | Faible |
| UC-SA-06 | Réinitialiser mot de passe | Moyenne | Moyenne |
| UC-SA-07 | Changer son propre mot de passe | Basse | Faible |
| UC-SA-08 | Se déconnecter | Basse | Faible |

### 2.3. Détail du cas d'utilisation principal : Créer un utilisateur

**UC-SA-02 : Créer un utilisateur**

**Acteur principal :** Superadmin

**Préconditions :**
- Le Superadmin est authentifié et connecté au système
- Le Superadmin possède les droits de création d'utilisateur

**Scénario nominal :**
1. Le Superadmin accède à la page de création d'utilisateur
2. Le système affiche un formulaire de création
3. Le Superadmin saisit les informations :
   - Nom de famille (en MAJUSCULES)
   - Prénom (en Nom Propre)
   - Email
   - Rôle (Superadmin ou Utilisateur)
4. Le Superadmin valide le formulaire
5. Le système valide les formats de données
6. Le système génère automatiquement un mot de passe (8 caractères min, complexité)
7. Le système crypte et stocke le mot de passe en base de données
8. Le système envoie le mot de passe par email (MailCatcher/MailHog)
9. Le système active le flag "première connexion"
10. Le système affiche un message de confirmation
11. Le système retourne à la liste des utilisateurs

**Scénarios alternatifs :**

**5a. Format de données invalide :**
- 5a.1. Le système affiche un message d'erreur spécifique
- 5a.2. Le système met en évidence les champs en erreur
- 5a.3. Retour à l'étape 3

**6a. Email déjà existant :**
- 6a.1. Le système affiche un message "Email déjà utilisé"
- 6a.2. Retour à l'étape 3

**8a. Échec d'envoi de l'email :**
- 8a.1. Le système enregistre l'utilisateur malgré tout
- 8a.2. Le système affiche un avertissement
- 8a.3. Le Superadmin peut réinitialiser le mot de passe plus tard

**Postconditions :**
- Un nouveau compte utilisateur est créé dans le système
- L'utilisateur reçoit son mot de passe par email
- L'utilisateur devra changer son mot de passe à sa première connexion

**Règles métier :**
- RG-01 : Le nom de famille doit être en MAJUSCULES
- RG-02 : Le prénom doit être en Nom Propre (première lettre en majuscule)
- RG-03 : Le mot de passe doit contenir au moins 8 caractères avec complexité
- RG-04 : Le mot de passe est crypté avant stockage
- RG-05 : Le changement de mot de passe est obligatoire à la première connexion

**Cas d'utilisation inclus :**
- Valider format des données
- Générer mot de passe aléatoire
- Envoyer email avec mot de passe

---

## 3. CAS D'UTILISATION - UTILISATEUR (ADMIN BACKOFFICE)

### 3.1. Rôle et responsabilités

L'**Utilisateur (Admin Backoffice)** est responsable de la gestion quotidienne du système, notamment :
- Création et gestion des compteurs
- Affectation des agents aux quartiers
- Consultation des relevés et tableaux de bord
- Génération de rapports
- Envoi des données à la facturation

### 3.2. Liste des cas d'utilisation par package

#### 3.2.1. Package Authentification

| ID | Cas d'utilisation | Priorité |
|----|-------------------|----------|
| UC-U-01 | Se connecter au système | Haute |
| UC-U-15 | Changer son mot de passe | Basse |
| UC-U-16 | Se déconnecter | Basse |

#### 3.2.2. Package Tableaux de bord

| ID | Cas d'utilisation | Priorité |
|----|-------------------|----------|
| UC-U-02 | Consulter tableau de bord principal | Haute |
| UC-U-02a | Afficher taux de couverture | Haute |
| UC-U-02b | Afficher relevés par jour par agent | Haute |
| UC-U-02c | Afficher évolution consommation moyenne | Haute |

#### 3.2.3. Package Gestion des compteurs

| ID | Cas d'utilisation | Priorité |
|----|-------------------|----------|
| UC-U-03 | Créer un compteur | Haute |
| UC-U-04 | Consulter liste des compteurs | Moyenne |
| UC-U-05 | Consulter détails d'un compteur | Moyenne |

#### 3.2.4. Package Gestion des agents

| ID | Cas d'utilisation | Priorité |
|----|-------------------|----------|
| UC-U-06 | Affecter un agent à un quartier | Haute |
| UC-U-07 | Consulter liste des agents | Moyenne |
| UC-U-08 | Consulter détails d'un agent | Moyenne |
| UC-U-09 | Modifier affectation d'un agent | Moyenne |

#### 3.2.5. Package Gestion des relevés

| ID | Cas d'utilisation | Priorité |
|----|-------------------|----------|
| UC-U-10 | Consulter liste des relevés | Haute |
| UC-U-11 | Consulter détails d'un relevé | Moyenne |

#### 3.2.6. Package Rapports et exports

| ID | Cas d'utilisation | Priorité |
|----|-------------------|----------|
| UC-U-12 | Générer rapport mensuel des relevés | Moyenne |
| UC-U-13 | Générer rapport évolution consommation | Moyenne |
| UC-U-14 | Envoyer données à la facturation | Haute |

### 3.3. Détail d'un cas d'utilisation principal : Créer un compteur

**UC-U-03 : Créer un compteur**

**Acteur principal :** Utilisateur (Admin Backoffice)

**Préconditions :**
- L'Utilisateur est authentifié et connecté au système
- L'Utilisateur possède le rôle "Utilisateur"
- Il existe des adresses sans compteur dans le système

**Scénario nominal :**
1. L'Utilisateur accède à la page "Liste des compteurs"
2. L'Utilisateur clique sur le bouton "Ajouter"
3. Le système affiche un formulaire de création de compteur
4. L'Utilisateur clique sur le champ "Sélectionner l'adresse"
5. Le système affiche une popup avec la liste des adresses sans compteur
6. L'Utilisateur peut filtrer par quartier (optionnel)
7. L'Utilisateur peut rechercher une adresse spécifique (optionnel)
8. L'Utilisateur sélectionne une adresse
9. Le système ferme la popup et affiche l'adresse sélectionnée
10. L'Utilisateur sélectionne le type de compteur (Eau ou Électricité)
11. L'Utilisateur clique sur "Enregistrer"
12. Le système valide les contraintes :
    - L'adresse ne possède pas déjà un compteur du même type
    - Maximum 2 compteurs par adresse standard (ou 4 pour immeuble)
13. Le système génère automatiquement l'identifiant (9 chiffres avec zéros précédents)
14. Le système initialise l'index à 0
15. Le système enregistre le compteur en base de données
16. Le système affiche un message de confirmation
17. Le système redirige vers la page "Détails du compteur" créé

**Scénarios alternatifs :**

**5a. Aucune adresse disponible :**
- 5a.1. Le système affiche un message "Aucune adresse sans compteur disponible"
- 5a.2. Fin du cas d'utilisation

**12a. Contrainte violée (compteur du même type déjà existant) :**
- 12a.1. Le système affiche "Cette adresse possède déjà un compteur de ce type"
- 12a.2. Retour à l'étape 4

**12b. Maximum de compteurs atteint :**
- 12b.1. Le système affiche "Nombre maximum de compteurs atteint pour cette adresse"
- 12b.2. Retour à l'étape 4

**Postconditions :**
- Un nouveau compteur est créé et associé à l'adresse
- L'identifiant unique est généré
- L'index initial est à 0
- Le compteur est prêt à recevoir des relevés

**Règles métier :**
- RG-06 : L'identifiant compteur est un nombre sur 9 chiffres avec zéros précédents
- RG-07 : L'index initial est toujours 0
- RG-08 : Maximum 2 compteurs par adresse (1 Eau + 1 Électricité)
- RG-09 : Pour les immeubles, maximum 4 compteurs (+ 2 pour espaces communs)
- RG-10 : Un compteur est associé de façon permanente à une adresse

**Cas d'utilisation inclus :**
- Sélectionner adresse sans compteur
- Valider contraintes compteur/adresse
- Générer identifiant compteur

---

## 4. CAS D'UTILISATION - INTÉGRATIONS SYSTÈMES

### 4.1. Intégrations entrantes

#### 4.1.1. UC-EXT-01 : Recevoir données clients depuis SI Commercial

**Acteur principal :** SI Commercial (ERP)

**Préconditions :**
- Le batch est planifié et actif
- Le SI Commercial contient de nouveaux clients ou adresses

**Scénario nominal :**
1. Le batch récurrent se déclenche automatiquement (planification)
2. Le SI Commercial envoie les données au SI Relevés :
   - Identifiant client
   - Nom et prénom du client
   - Liste des adresses associées (adresse complète + identifiant)
3. Le SI Relevés reçoit les données
4. Le SI Relevés valide le format des données
5. Le SI Relevés enregistre ou met à jour les clients en base de données
6. Le SI Relevés enregistre ou met à jour les adresses en base de données
7. Le SI Relevés logue l'opération de synchronisation

**Postconditions :**
- Les nouveaux clients et adresses sont disponibles dans le SI Relevés
- Les adresses peuvent être associées à des compteurs

#### 4.1.2. UC-EXT-02 : Recevoir données agents depuis SI RH

**Acteur principal :** SI RH (ERP)

**Préconditions :**
- Le batch est planifié et actif
- Le SI RH contient de nouveaux agents

**Scénario nominal :**
1. Le batch récurrent se déclenche automatiquement (planification)
2. Le SI RH envoie les données au SI Relevés :
   - Identifiant agent
   - Nom de famille et prénom
   - Numéros de téléphone (personnel et professionnel)
3. Le SI Relevés reçoit les données
4. Le SI Relevés valide le format des données
5. Le SI Relevés enregistre ou met à jour les agents en base de données
6. Le SI Relevés logue l'opération de synchronisation

**Postconditions :**
- Les nouveaux agents sont disponibles dans le SI Relevés
- Les agents peuvent être affectés à des quartiers

#### 4.1.3. UC-EXT-03 : Recevoir relevés depuis application mobile

**Acteur principal :** Agent de Terrain (via App Mobile)

**Préconditions :**
- L'agent a effectué un relevé sur le terrain
- L'application mobile est connectée à Internet

**Scénario nominal :**
1. L'agent valide un relevé dans l'application mobile
2. L'application mobile envoie les données au SI Relevés via web service (POST) :
   - Numéro de compteur
   - Nouvel index
   - Date et heure du relevé
3. Le SI Relevés reçoit les données
4. Le SI Relevés valide l'existence du compteur
5. Le SI Relevés récupère l'ancien index du compteur
6. Le SI Relevés calcule la consommation : Nouvel index - Ancien index
7. Le SI Relevés enregistre le relevé en base de données
8. Le SI Relevés met à jour l'index actuel du compteur
9. Le SI Relevés retourne une confirmation à l'application mobile

**Postconditions :**
- Le relevé est enregistré dans le système
- L'index du compteur est mis à jour
- La consommation est calculée et prête pour la facturation

**Règle métier :**
- RG-11 : Consommation = Nouvel index - Ancien index
- RG-12 : Unité : m³ pour l'eau, kWh pour l'électricité

### 4.2. Intégrations sortantes

#### 4.2.1. UC-EXT-04 : Fournir liste adresses à relever

**Acteur principal :** Agent de Terrain (via App Mobile)

**Préconditions :**
- L'agent démarre sa tournée
- L'agent est affecté à un quartier
- L'application mobile est connectée à Internet

**Scénario nominal :**
1. L'application mobile envoie une requête au SI Relevés (GET) avec l'identifiant de l'agent
2. Le SI Relevés identifie le quartier de l'agent
3. Le SI Relevés récupère toutes les adresses du quartier
4. Le SI Relevés filtre les adresses dont les compteurs n'ont pas été relevés ce mois-ci
5. Le SI Relevés retourne la liste des adresses avec leurs compteurs
6. L'application mobile affiche la liste à l'agent

**Postconditions :**
- L'agent connaît les adresses qu'il doit visiter

#### 4.2.2. UC-EXT-05 : Envoyer données à la facturation

**Acteur principal :** Système (automatique)

**Acteur secondaire :** SI Facturation (ERP)

**Préconditions :**
- Des relevés ont été effectués et les consommations calculées
- Le déclencheur automatique est actif

**Scénario nominal :**
1. Le système détecte que de nouvelles consommations ont été calculées
2. Le système regroupe les données par client :
   - Identifiant client
   - Adresse (ou identifiant adresse)
   - Numéro de compteur
   - Date de relevé (+ heure si disponible)
   - Consommation eau (m³)
   - Consommation électricité (kWh)
3. Le système envoie les données au SI Facturation via web service (POST)
4. Le SI Facturation accuse réception
5. Le système marque les relevés comme "envoyés à la facturation"
6. Le système logue l'opération

**Scénarios alternatifs :**

**4a. Échec de l'envoi :**
- 4a.1. Le système réessaie après un délai
- 4a.2. Si échec après 3 tentatives, le système alerte l'administrateur
- 4a.3. Les données restent marquées comme "en attente d'envoi"

**Postconditions :**
- Les données de consommation sont transmises au SI Facturation
- Les factures peuvent être générées par le service comptabilité

---

## 5. MATRICE DE TRAÇABILITÉ

### 5.1. Traçabilité Besoins Fonctionnels → Cas d'Utilisation

| Besoin Fonctionnel | Cas d'Utilisation | Priorité |
|--------------------|-------------------|----------|
| BF-01 : Authentification utilisateur | UC-SA-01, UC-U-01 | Haute |
| BF-02 : Gestion des mots de passe | UC-SA-06, UC-U-15 | Haute |
| BF-03 : Gestion des rôles | UC-SA-02, UC-SA-05 | Haute |
| BF-04 : Création d'utilisateur | UC-SA-02 | Haute |
| BF-05 : Liste des utilisateurs | UC-SA-03 | Moyenne |
| BF-06 : Détails et modification utilisateur | UC-SA-04, UC-SA-05 | Moyenne |
| BF-07 : Création de compteur | UC-U-03 | Haute |
| BF-08 : Liste des compteurs | UC-U-04 | Moyenne |
| BF-09 : Détails d'un compteur | UC-U-05 | Moyenne |
| BF-10 : Réception données agents | UC-EXT-02 | Haute |
| BF-11 : Liste des agents | UC-U-07 | Moyenne |
| BF-12 : Détails et affectation agent | UC-U-06, UC-U-08, UC-U-09 | Haute |
| BF-13 : Réception des relevés | UC-EXT-03 | Haute |
| BF-14 : Calcul de la consommation | UC-EXT-03 (inclus) | Haute |
| BF-15 : Liste des relevés | UC-U-10 | Haute |
| BF-16 : Détails d'un relevé | UC-U-11 | Moyenne |
| BF-17 : Envoi données facturation | UC-EXT-05 | Haute |
| BF-18 : Taux de couverture | UC-U-02a | Haute |
| BF-19 : Relevés par jour par agent | UC-U-02b | Haute |
| BF-20 : Évolution consommation | UC-U-02c | Haute |
| BF-21 : Rapport mensuel | UC-U-12 | Moyenne |
| BF-22 : Rapport consommation | UC-U-13 | Moyenne |
| BF-23 : Réception données clients | UC-EXT-01 | Haute |
| BF-24 : Communication app mobile | UC-EXT-03, UC-EXT-04 | Haute |
| BF-25 : Envoi vers SI Facturation | UC-EXT-05 | Haute |

### 5.2. Couverture des besoins

**Statistiques :**
- Total des besoins fonctionnels : 25
- Total des cas d'utilisation : 33
- Couverture : 100%

Tous les besoins fonctionnels identifiés en Phase 1 sont couverts par au moins un cas d'utilisation.

---

## 6. RÈGLES DE GESTION TRANSVERSALES

### 6.1. Règles d'authentification et sécurité

| ID | Règle | Application |
|----|-------|-------------|
| RG-AUTH-01 | Session JWT avec expiration 30 minutes | Tous les cas d'utilisation |
| RG-AUTH-02 | Déconnexion auto après 10 min d'inactivité | Tous les cas d'utilisation |
| RG-AUTH-03 | Mots de passe cryptés en base | UC-SA-02, UC-SA-06, UC-U-15 |
| RG-AUTH-04 | HTTPS obligatoire | Tous les cas d'utilisation |
| RG-AUTH-05 | Contrôle d'accès basé sur le rôle (RBAC) | Tous les cas d'utilisation |

### 6.2. Règles de formatage des données

| ID | Règle | Application |
|----|-------|-------------|
| RG-FORMAT-01 | NOM DE FAMILLE en MAJUSCULES | UC-SA-02, UC-SA-05 |
| RG-FORMAT-02 | Prénom en Nom Propre | UC-SA-02, UC-SA-05 |
| RG-FORMAT-03 | Identifiant compteur : 9 chiffres avec zéros | UC-U-03 |

### 6.3. Règles métier de gestion des compteurs

| ID | Règle | Application |
|----|-------|-------------|
| RG-METER-01 | Index initial = 0 | UC-U-03 |
| RG-METER-02 | Max 2 compteurs par adresse standard | UC-U-03 |
| RG-METER-03 | Max 4 compteurs pour immeubles | UC-U-03 |
| RG-METER-04 | Association compteur-adresse permanente | UC-U-03 |

### 6.4. Règles métier de calcul

| ID | Règle | Application |
|----|-------|-------------|
| RG-CALC-01 | Consommation = Nouvel index - Ancien index | UC-EXT-03 |
| RG-CALC-02 | Unité eau : m³ | UC-EXT-03, UC-U-11 |
| RG-CALC-03 | Unité électricité : kWh | UC-EXT-03, UC-U-11 |
| RG-CALC-04 | Taux couverture = (Relevés / Total) × 100 | UC-U-02a |

---

## 7. EXIGENCES NON FONCTIONNELLES

### 7.1. Performance

| ID | Exigence | Cas d'utilisation concernés |
|----|----------|---------------------------|
| ENF-PERF-01 | Temps de réponse < 2 secondes pour les pages | Tous |
| ENF-PERF-02 | Génération PDF < 5 secondes | UC-U-12, UC-U-13 |
| ENF-PERF-03 | Support de 100 utilisateurs simultanés | Tous |

### 7.2. Ergonomie

| ID | Exigence | Cas d'utilisation concernés |
|----|----------|---------------------------|
| ENF-ERGO-01 | Interface responsive (desktop, tablette) | Tous |
| ENF-ERGO-02 | Messages d'erreur clairs et explicites | Tous |
| ENF-ERGO-03 | Confirmation avant actions critiques | UC-SA-06, UC-U-03, UC-U-14 |

### 7.3. Disponibilité

| ID | Exigence | Cas d'utilisation concernés |
|----|----------|---------------------------|
| ENF-DISPO-01 | Disponibilité 99% pendant heures ouvrables | Tous |
| ENF-DISPO-02 | Sauvegarde quotidienne des données | N/A |
| ENF-DISPO-03 | Logs d'activité conservés 1 an | Tous |

---

## CONCLUSION

Ce document présente les spécifications fonctionnelles détaillées sous forme de diagrammes de cas d'utilisation, générés automatiquement par **IA générative** (PlantUML).

**Couverture :**
- ✅ 33 cas d'utilisation identifiés
- ✅ 6 acteurs (2 internes, 1 externe humain, 3 systèmes)
- ✅ 7 packages fonctionnels
- ✅ 100% des besoins fonctionnels couverts
- ✅ Règles de gestion documentées
- ✅ Exigences non fonctionnelles définies

**Fichiers générés :**
- `UseCase_Global.puml` : Diagramme global PlantUML
- `UseCase_Superadmin.puml` : Diagramme Superadmin détaillé
- `UseCase_Utilisateur.puml` : Diagramme Utilisateur détaillé
- `UseCase_Global.mermaid` : Diagramme global Mermaid (alternatif)

**Prochaine étape :** Conception du Modèle Conceptuel de Données (MCD)

---

**Document généré automatiquement par IA - Claude (Anthropic)**  
**Technique utilisée :** IA générative pour spécifications détaillées et générateurs de diagrammes UML  
**Outils :** PlantUML, Mermaid
