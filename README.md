# SI Relevés - Système de Gestion des Relevés de Compteurs

Application web backoffice pour la gestion des relevés de compteurs d'eau et d'électricité pour RABAT ENERGIE & EAU (REE).

## 🏗️ Architecture

- **Backend:** Spring Boot 3.2 (Java 17)
- **Frontend:** React 18 + Vite + Tailwind CSS
- **Database:** MySQL 8.0
- **Déploiement:** Docker Compose (local)
- **Architecture:** Monolithe modulaire en couches

## 📚 Documentation

Toute la documentation du projet est dans le dossier `docs/` :

### Phase 1 : Analyse des besoins
- `docs/phase1/Phase1_Analyse_Besoins.md` - Analyse complète des besoins fonctionnels et techniques

### Phase 2 : Conception
- `docs/phase2/Phase2_1_Specifications_Fonctionnelles_UseCases.md` - 33 cas d'utilisation détaillés
- `docs/phase2/Phase2_2_MCD.md` - Modèle Conceptuel de Données (7 entités)
- `docs/phase2/Phase2_3_MLD.md` - Modèle Logique de Données + mapping MCD→MLD
- `docs/phase2/Phase2_4_Cahier_Tests.md` - 157 cas de test (unitaires, intégration, E2E)
- `docs/phase2/Phase2_5_Architecture_Technique.md` - Stack technique et configuration Docker
- `docs/phase2/Phase2_5_Complement_Architecture_Logicielle.md` - Justification architecture monolithique

### Diagrammes
- `docs/diagrams/UseCase_*.puml` - Diagrammes de cas d'utilisation (PlantUML)
- `docs/diagrams/MCD_SI_Releves.puml` - Diagramme MCD (PlantUML)
- `docs/diagrams/architecture_diagram.png` - Architecture Docker Compose (Eraser.io)

### Cahier des charges
- `docs/cahier_des_charges/cahier_des_charges.pdf` - CDC original
- `docs/cahier_des_charges/consignes_projet.pdf` - Consignes du projet

## 🗄️ Base de données

Le script SQL complet de création de la base de données est disponible dans :
- `database/MLD_Create_Database.sql` (500+ lignes)
    - 7 tables avec contraintes
    - 4 triggers métier
    - 3 procédures stockées
    - 3 vues métier
    - Données de test (quartiers + superadmin)

## 🚀 Démarrage rapide

### Prérequis
- Docker Desktop installé et démarré
- Git
- Java 17+ (pour développement local backend)
- Node.js 20+ (pour développement local frontend)

### Lancer l'application avec Docker Compose
```bash
# Cloner le repository
git clone <url>
cd si-releves

# Démarrer tous les services
docker-compose up -d

# Voir les logs
docker-compose logs -f

# Arrêter les services
docker-compose down
```

### Accès aux services

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost:3000 | Interface React |
| **Backend API** | http://localhost:8080 | API REST Spring Boot |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentation API interactive |
| **MySQL** | localhost:3306 | Base de données (user: `si_releves_user`) |
| **MailHog UI** | http://localhost:8025 | Interface de test des emails |
| **Nginx** | http://localhost | Reverse proxy |

### Développement local (sans Docker)

#### Backend
```bash
cd backend
mvn spring-boot:run
```

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

## 📦 Structure du projet
```
si-releves/
├── backend/           # Backend Spring Boot (Java 17)
├── frontend/          # Frontend React (Vite + Tailwind)
├── database/          # Scripts SQL (init DB)
├── nginx/             # Configuration Nginx
├── docs/              # Documentation complète (Phases 1 & 2)
├── docker-compose.yml # Orchestration Docker
└── README.md          # Ce fichier
```

## 🔐 Comptes par défaut

**Superadmin** (créé automatiquement) :
- Email : `admin@ree.ma`
- Mot de passe : `Admin@123` ⚠️ **À changer en production !**

## 🧪 Tests

Les spécifications de tests sont dans `docs/phase2/Phase2_4_Cahier_Tests.md` :
- 157 cas de test documentés
- Tests unitaires, intégration, fonctionnels, sécurité, performance

### Exécuter les tests
```bash
# Backend
cd backend
mvn test

# Frontend
cd frontend
npm test
```

## 🛠️ Technologies utilisées

### Backend
- Java 17
- Spring Boot 3.2.1
- Spring Data JPA (Hibernate)
- Spring Security + JWT
- MySQL Connector
- Lombok
- MapStruct
- SpringDoc OpenAPI (Swagger)
- Maven

### Frontend
- React 18.2
- Vite 5.0
- Tailwind CSS 3.4
- Zustand (state management)
- React Query (server state)
- React Hook Form + Yup
- Axios
- Recharts

### DevOps
- Docker + Docker Compose
- Nginx (reverse proxy)
- MySQL 8.0
- MailHog (test SMTP)

## 📊 Fonctionnalités principales

### Module Superadmin
- ✅ Gestion des utilisateurs backoffice (CRUD)
- ✅ Attribution des rôles (Superadmin / Utilisateur)
- ✅ Réinitialisation de mots de passe

### Module Utilisateur (Admin Backoffice)
- ✅ Gestion des compteurs (CRUD, génération ID automatique)
- ✅ Gestion des agents (affectation quartiers)
- ✅ Consultation des relevés (filtres avancés)
- ✅ Tableaux de bord avec KPIs
- ✅ Génération de rapports PDF

### Intégrations externes
- ✅ Réception relevés depuis application mobile agents
- ✅ Synchronisation clients depuis SI Commercial
- ✅ Synchronisation agents depuis SI RH
- ✅ Envoi consommations vers SI Facturation

## 👥 Contributeurs

Projet académique - Génération assistée par IA (Claude - Anthropic)

## 📄 Licence

Projet académique - RABAT ENERGIE & EAU (REE)

---

**Note :** Ce projet a été conçu en utilisant l'IA générative pour toutes les phases (Analyse, Conception, Documentation).