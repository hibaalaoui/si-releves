# SI Relevés - Système de Gestion des Relevés de Compteurs

**RABAT ENERGIE & EAU (REE)**

Système de gestion des relevés de compteurs d'eau et d'électricité pour RABAT ENERGIE & EAU.

---

## 📋 Table des Matières

- [Vue d'ensemble](#vue-densemble)
- [Architecture](#architecture)
- [État du Projet](#état-du-projet)
- [Structure du Projet](#structure-du-projet)
- [Fonctionnalités Implémentées](#fonctionnalités-implémentées)
- [Fonctionnalités Manquantes](#fonctionnalités-manquantes)
- [Installation et Configuration](#installation-et-configuration)
- [Documentation](#documentation)

---

## 🎯 Vue d'ensemble

Ce projet est un système de gestion des relevés de compteurs d'eau et d'électricité développé avec une architecture moderne :
- **Backend**: Spring Boot 3.2.1 + Java 17
- **Frontend**: React 18 + Vite 5 + Tailwind CSS
- **Base de données**: MySQL 8.0
- **Authentification**: JWT (JSON Web Tokens)

---

## 🏗️ Architecture

### Architecture Technique

- **Pattern**: Architecture modulaire en couches (Monolithic Modular Layered Architecture)
- **Backend**: Spring Boot avec séparation par modules (auth, user, compteur, agent, releve, dashboard)
- **Frontend**: React avec architecture par fonctionnalités
- **Base de données**: MySQL 8.0 avec triggers, procédures stockées et vues

### Stack Technologique

#### Backend
- Java 17
- Spring Boot 3.2.1
- Spring Security (JWT)
- Spring Data JPA
- MySQL Connector
- Lombok
- MapStruct
- SpringDoc OpenAPI (Swagger)
- Bean Validation

#### Frontend
- React 18
- Vite 5
- Tailwind CSS 3
- React Router DOM 6
- Zustand (State Management)
- React Query (Server State)
- Axios (HTTP Client)
- React Hook Form + Yup (Form Validation)
- Recharts (Charts)
- React Icons
- date-fns
- jwt-decode

---

## ✅ État du Projet

### 🟢 Complété

#### Backend

1. **Structure du Projet**
   - ✅ Configuration Maven avec toutes les dépendances
   - ✅ Structure de packages modulaire
   - ✅ Configuration Spring Boot (application.yml)
   - ✅ Configuration de sécurité de base

2. **Entités JPA (7 entités)**
   - ✅ `UtilisateurBackoffice` (User)
   - ✅ `Client`
   - ✅ `Quartier`
   - ✅ `Adresse`
   - ✅ `Agent`
   - ✅ `Compteur`
   - ✅ `Releve`
   - ✅ Enums: Role, TypeBien, TypeCompteur, Unite
   - ✅ Relations JPA (@ManyToOne, @OneToMany)
   - ✅ Validations Bean Validation

3. **Repositories Spring Data JPA**
   - ✅ `UserRepository` (findByEmail, findByRole, findByActif)
   - ✅ `QuartierRepository`
   - ✅ `ClientRepository`
   - ✅ `AdresseRepository`
   - ✅ `AgentRepository` (findByQuartier, findByActif)
   - ✅ `CompteurRepository` (findByAdresse, findByType, findByActif, custom queries pour max compteurs)
   - ✅ `ReleveRepository` (findByCompteur, findByAgent, findByDateReleveBetween, custom queries pour KPIs)

4. **Système d'Authentification JWT**
   - ✅ `JwtTokenProvider` (génération et validation de tokens)
   - ✅ `JwtAuthenticationFilter` (interception des requêtes)
   - ✅ `UserDetailsServiceImpl` (chargement des utilisateurs)
   - ✅ `AuthService` (logique de login avec BCrypt)
   - ✅ `AuthController` (endpoint `/api/auth/login`)
   - ✅ DTOs: `LoginRequest`, `LoginResponse`
   - ✅ Configuration de sécurité avec JWT
   - ✅ Token expiration: 30 minutes

5. **Gestion des Exceptions**
   - ✅ `GlobalExceptionHandler` avec `@ControllerAdvice`
   - ✅ `ErrorResponse` DTO
   - ✅ Gestion des erreurs de validation

#### Frontend

1. **Structure du Projet**
   - ✅ Configuration Vite avec React 18
   - ✅ Configuration Tailwind CSS
   - ✅ Structure de dossiers modulaire
   - ✅ Configuration Axios avec interceptors

2. **Authentification**
   - ✅ Page de login (`Login.jsx`) avec React Hook Form + Yup
   - ✅ `authService.js` pour les appels API
   - ✅ `authStore` (Zustand) avec persistence localStorage
   - ✅ `ProtectedRoute` pour protéger les routes
   - ✅ Auto-logout après 10 minutes d'inactivité
   - ✅ Redirection vers `/dashboard` après login
   - ✅ Validation de formulaire complète

3. **Layout et Navigation**
   - ✅ `Layout` component avec Header et Sidebar
   - ✅ `Header` avec info utilisateur et bouton déconnexion
   - ✅ `Sidebar` avec navigation par rôle
   - ✅ Menu conditionnel (Users visible seulement pour Superadmin)

4. **Routing**
   - ✅ Configuration React Router avec routes protégées
   - ✅ Routes pour: dashboard, compteurs, agents, releves, users
   - ✅ Redirection automatique des utilisateurs authentifiés

5. **Utilitaires**
   - ✅ `constants.js` (endpoints API, rôles, formats)
   - ✅ `helpers.js` (formatage dates, nombres, vérification rôles)
   - ✅ Hook `useInactivityLogout` pour auto-logout

---

## ❌ Fonctionnalités Manquantes

### Backend

#### 1. Gestion des Utilisateurs (Module User)
- ❌ `UserController` avec endpoints CRUD
- ❌ `UserService` avec logique métier
- ❌ DTOs: `UserRequest`, `UserResponse`, `CreateUserRequest`, `UpdateUserRequest`
- ❌ Mappers MapStruct pour User
- ❌ Endpoints:
  - `GET /api/users` (liste avec pagination)
  - `GET /api/users/{id}` (détails)
  - `POST /api/users` (création - Superadmin seulement)
  - `PUT /api/users/{id}` (modification)
  - `DELETE /api/users/{id}` (soft delete)
  - `POST /api/users/{id}/reset-password` (réinitialisation mot de passe)
- ❌ Validation des règles métier:
  - Nom en MAJUSCULES
  - Prénom en Proper Case
  - Email unique
  - Génération automatique de mot de passe (8 caractères)
  - Hash BCrypt des mots de passe

#### 2. Gestion des Compteurs (Module Compteur)
- ❌ `CompteurController` avec endpoints CRUD
- ❌ `CompteurService` avec logique métier
- ❌ DTOs: `CompteurRequest`, `CompteurResponse`, `CreateCompteurRequest`
- ❌ Mappers MapStruct pour Compteur
- ❌ Endpoints:
  - `GET /api/compteurs` (liste avec filtres: quartier, type, actif)
  - `GET /api/compteurs/{id}` (détails)
  - `POST /api/compteurs` (création avec génération ID)
  - `PUT /api/compteurs/{id}` (modification)
  - `DELETE /api/compteurs/{id}` (soft delete)
- ❌ Validation des règles métier:
  - ID format: 9 chiffres avec zéros (ex: 000000001)
  - Max 2 compteurs standards par adresse
  - Max 2 compteurs espaces communs pour immeubles
  - Espaces communs uniquement pour immeubles
  - Index initial: 0.00

#### 3. Gestion des Agents (Module Agent)
- ❌ `AgentController` avec endpoints
- ❌ `AgentService` avec logique métier
- ❌ DTOs: `AgentRequest`, `AgentResponse`
- ❌ Mappers MapStruct pour Agent
- ❌ Endpoints:
  - `GET /api/agents` (liste avec filtres: quartier, actif)
  - `GET /api/agents/{id}` (détails)
  - `PUT /api/agents/{id}/affect-quartier` (affectation quartier)

#### 4. Gestion des Relevés (Module Releve)
- ❌ `ReleveController` avec endpoints
- ❌ `ReleveService` avec logique métier
- ❌ DTOs: `ReleveRequest`, `ReleveResponse`, `CreateReleveRequest`
- ❌ Mappers MapStruct pour Releve
- ❌ Endpoints:
  - `GET /api/releves` (liste avec filtres avancés: compteur, agent, date, quartier)
  - `GET /api/releves/{id}` (détails)
  - `POST /api/releves` (création avec calcul automatique consommation)
  - `POST /api/releves/mobile` (endpoint pour app mobile)
- ❌ Validation des règles métier:
  - Calcul automatique: consommation = nouvel_index - ancien_index
  - Unité automatique: Eau → m3, Electricite → kWh
  - Validation: nouvel_index >= ancien_index
  - Mise à jour automatique compteur.index_actuel

#### 5. Dashboard et KPIs (Module Dashboard)
- ❌ `DashboardController` avec endpoints
- ❌ `DashboardService` avec calculs KPIs
- ❌ DTOs: `KpiResponse`, `StatsResponse`
- ❌ Endpoints:
  - `GET /api/dashboard/kpis` (taux de couverture, moyennes)
  - `GET /api/dashboard/stats` (statistiques détaillées)
- ❌ Calculs KPIs:
  - Taux de couverture: (compteurs relevés / total compteurs) × 100
  - Relevés par jour par agent
  - Filtres par quartier et plage de dates

#### 6. Authentification Complète
- ❌ Endpoint `POST /api/auth/change-password` (changement mot de passe)
- ❌ Endpoint `POST /api/auth/logout` (optionnel, JWT stateless)
- ❌ Validation première connexion (forcer changement mot de passe)
- ❌ Refresh token (optionnel)

#### 7. Gestion des Adresses et Clients
- ❌ `AdresseController` et `AdresseService`
- ❌ `ClientController` et `ClientService`
- ❌ Endpoints pour recherche et sélection d'adresses
- ❌ Popup de sélection d'adresse pour création compteur

#### 8. Génération de Rapports
- ❌ Module `report` pour génération PDF
- ❌ `ReportController` et `ReportService`
- ❌ Endpoints:
  - `GET /api/reports/monthly` (rapport mensuel)
  - `GET /api/reports/agent-performance` (rapport performance agent)
- ❌ Bibliothèque de génération PDF (iText, Apache PDFBox, ou autre)

#### 9. Tests
- ❌ Tests unitaires pour services
- ❌ Tests d'intégration pour repositories
- ❌ Tests API avec MockMvc
- ❌ Tests E2E (optionnel)

#### 10. Configuration et Déploiement
- ❌ Dockerfile pour backend
- ❌ Dockerfile pour frontend
- ❌ docker-compose.yml complet
- ❌ Configuration Nginx pour reverse proxy
- ❌ Variables d'environnement pour production

### Frontend

#### 1. Pages Principales

##### Dashboard
- ❌ Affichage des KPIs (cartes)
- ❌ Graphiques avec Recharts:
  - Évolution des relevés (ligne)
  - Répartition par quartier (camembert/barre)
  - Performance agents
- ❌ Filtres: date range, quartier
- ❌ Actualisation automatique des données

##### Gestion des Compteurs
- ❌ `CompteursList.jsx`:
  - Tableau avec pagination
  - Filtres: quartier, type, actif
  - Recherche par ID ou adresse
  - Actions: voir, modifier, désactiver
- ❌ `CompteurForm.jsx`:
  - Formulaire création/modification
  - Sélection adresse (popup)
  - Validation règles métier
  - Génération ID automatique
- ❌ `CompteurDetails.jsx`:
  - Détails complets
  - Historique des relevés
  - Graphique consommation

##### Gestion des Agents
- ❌ `AgentsList.jsx`:
  - Liste avec filtres
  - Affectation quartier
- ❌ `AgentDetails.jsx`:
  - Détails agent
  - Statistiques de performance
  - Liste des relevés effectués

##### Gestion des Relevés
- ❌ `RelevesList.jsx`:
  - Tableau avec filtres avancés
  - Export CSV/Excel
  - Filtres: compteur, agent, date, quartier
- ❌ `ReleveForm.jsx`:
  - Formulaire création
  - Calcul automatique consommation
  - Validation index
- ❌ `ReleveDetails.jsx`:
  - Détails complets
  - Informations compteur et agent

##### Gestion des Utilisateurs (Superadmin)
- ❌ `UsersList.jsx`:
  - Liste avec pagination
  - Filtres: rôle, actif
- ❌ `UserForm.jsx`:
  - Formulaire création/modification
  - Génération mot de passe
  - Validation règles métier
- ❌ `UserDetails.jsx`:
  - Détails utilisateur
  - Historique connexions (si implémenté)

#### 2. Composants Communs
- ❌ `Button.jsx` (composant bouton réutilisable)
- ❌ `Input.jsx` (composant input réutilisable)
- ❌ `Modal.jsx` (modal réutilisable)
- ❌ `Table.jsx` (tableau avec pagination)
- ❌ `Select.jsx` (select avec recherche)
- ❌ `DatePicker.jsx` (sélecteur de dates)
- ❌ `LoadingSpinner.jsx` (indicateur de chargement)
- ❌ `ErrorMessage.jsx` (affichage erreurs)

#### 3. Composants de Graphiques
- ❌ `LineChart.jsx` (graphique ligne)
- ❌ `BarChart.jsx` (graphique barres)
- ❌ `PieChart.jsx` (graphique camembert)
- ❌ `AreaChart.jsx` (graphique aires)

#### 4. Hooks Personnalisés
- ❌ `useDebounce.js` (debounce pour recherche)
- ❌ `usePagination.js` (gestion pagination)
- ❌ `useFilters.js` (gestion filtres)
- ❌ `useApi.js` (hook générique pour appels API)

#### 5. Services API
- ❌ `userService.js` (appels API utilisateurs)
- ❌ `compteurService.js` (appels API compteurs)
- ❌ `agentService.js` (appels API agents)
- ❌ `releveService.js` (appels API relevés)
- ❌ `dashboardService.js` (appels API dashboard)

#### 6. Fonctionnalités Avancées
- ❌ Gestion des erreurs globale (Error Boundary)
- ❌ Notifications toast (succès, erreur, info)
- ❌ Confirmation modals (suppression, actions critiques)
- ❌ Export de données (CSV, Excel, PDF)
- ❌ Impression de rapports
- ❌ Mode sombre (optionnel)
- ❌ Internationalisation i18n (optionnel)

#### 7. Optimisations
- ❌ Lazy loading des routes
- ❌ Code splitting
- ❌ Optimisation des images
- ❌ Cache des requêtes API (React Query)
- ❌ Optimistic updates

---

## 📁 Structure du Projet

```
si-releves/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/ma/ree/sireleves/
│   │   │   │   ├── auth/              ✅ Controller, Service, DTOs
│   │   │   │   ├── user/              ⚠️ Entity, Repository (manque Service, Controller)
│   │   │   │   ├── compteur/          ⚠️ Entity, Repository (manque Service, Controller)
│   │   │   │   ├── agent/             ⚠️ Entity, Repository (manque Service, Controller)
│   │   │   │   ├── releve/            ⚠️ Entity, Repository (manque Service, Controller)
│   │   │   │   ├── dashboard/         ❌ Vide (à implémenter)
│   │   │   │   ├── common/            ✅ Entities, Enums, Repositories
│   │   │   │   ├── config/            ✅ SecurityConfig
│   │   │   │   ├── security/          ✅ JWT, UserDetailsService
│   │   │   │   └── exception/         ✅ GlobalExceptionHandler
│   │   │   └── resources/
│   │   │       └── application.yml    ✅ Configuration
│   │   └── test/                      ❌ Tests à créer
│   ├── pom.xml                        ✅ Configuration Maven
│   └── .gitignore                     ✅
│
├── frontend/
│   ├── src/
│   │   ├── api/                       ✅ axios.js, authService.js
│   │   ├── components/
│   │   │   ├── common/                ❌ Composants à créer
│   │   │   ├── layout/                ✅ Header, Sidebar, Layout
│   │   │   └── charts/                ❌ Composants à créer
│   │   ├── pages/
│   │   │   ├── auth/                  ✅ Login
│   │   │   ├── dashboard/             ⚠️ Structure (manque contenu)
│   │   │   ├── compteurs/             ❌ Pages à créer
│   │   │   ├── agents/                ❌ Pages à créer
│   │   │   └── releves/               ❌ Pages à créer
│   │   ├── hooks/                     ✅ useInactivityLogout
│   │   ├── store/                     ✅ authStore
│   │   ├── utils/                     ✅ constants, helpers
│   │   └── routes/                    ✅ Configuration routes
│   ├── package.json                   ✅
│   ├── vite.config.js                 ✅
│   ├── tailwind.config.js             ✅
│   └── .gitignore                     ✅
│
├── database/
│   └── MLD_Create_Database.sql        ✅ Schéma complet
│
├── docs/                              ✅ Documentation complète
│
└── README.md                          ✅ Ce fichier
```

---

## 🚀 Installation et Configuration

### Prérequis

- Java 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**Variables d'environnement:**
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

**Variables d'environnement:**
- `VITE_API_URL` (défaut: http://localhost:8080)

---

## 📚 Documentation

La documentation complète du projet se trouve dans le dossier `docs/`:

- `docs/phase1/Phase1_Analyse_Besoins.md` - Analyse des besoins
- `docs/phase2/Phase2_1_Specifications_Fonctionnelles_UseCases.md` - 33 cas d'usage détaillés
- `docs/phase2/Phase2_2_MCD.md` - Modèle Conceptuel de Données
- `docs/phase2/Phase2_3_MLD.md` - Modèle Logique de Données
- `docs/phase2/Phase2_4_Cahier_Tests.md` - 157 cas de test
- `docs/phase2/Phase2_5_Architecture_Technique.md` - Architecture technique
- `database/MLD_Create_Database.sql` - Schéma SQL complet

---

## 📝 Notes

- Le projet suit les règles définies dans `.cursorrules`
- Tous les mots de passe sont hashés avec BCrypt
- Les tokens JWT expirent après 30 minutes
- Auto-logout après 10 minutes d'inactivité (frontend)
- Les règles métier sont critiques et doivent être respectées

---

## 👥 Contribution

Ce projet est généré et assisté par IA (Claude - Anthropic).

---

**Dernière mise à jour**: Décembre 2024
