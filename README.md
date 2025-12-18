# SI Relevés - Système d'Information de Gestion des Relevés

## 📋 Vue d'ensemble

SI Relevés est une application web complète pour la gestion des relevés de compteurs, développée avec une architecture microservices moderne utilisant Docker. Le système comprend un frontend React, un backend Spring Boot, une base de données MySQL, et une stack complète de monitoring avec Prometheus et Grafana, tous orchestrés via Docker Compose.

## 🏗️ Architecture

### Architecture Générale

```
┌─────────────────────────────────────────────────────────────────────┐
│                      Docker Compose Stack                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌──────────────┐      ┌──────────────┐      ┌──────────┐          │
│  │   Frontend   │──────│   Backend    │──────│  MySQL   │          │
│  │   (React)    │      │ (Spring Boot)│      │  (8.0)   │          │
│  │   Nginx      │      │   Port 8080  │      │ Port 3306│          │
│  │   Port 80    │      │              │      │          │          │
│  └──────────────┘      └──────────────┘      └──────────┘          │
│         │                      │                    │              │
│         │                      │                    │              │
│  ┌──────┴──────┐      ┌───────┴───────┐    ┌──────┴──────┐       │
│  │   Grafana   │      │  Prometheus   │    │ MySQL       │       │
│  │   Port 3000 │      │  Port 9090    │    │ Exporter    │       │
│  │             │      │               │    │ Port 9104   │       │
│  └─────────────┘      └───────┬───────┘    └─────────────┘       │
│         │                      │                    │              │
│         │              ┌───────┴───────┐            │              │
│         │              │   cAdvisor    │            │              │
│         │              │   Port 8081   │            │              │
│         │              └───────────────┘            │              │
│         │                                            │              │
│         └────────────────────────────────────────────┘              │
│                      backend-network                                │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Services Docker

Le projet utilise **Docker Compose** pour orchestrer **7 services** :

#### Services Principaux

1. **Frontend (React + Nginx)**
   - Build multi-stage avec Node.js 20
   - Serveur web Nginx Alpine pour la production
   - Optimisations de performance (gzip, cache)
   - Health checks intégrés
   - Port : 80

2. **Backend (Spring Boot)**
   - Build multi-stage avec JDK 17
   - Runtime JRE Alpine optimisé
   - Utilisateur non-root pour la sécurité
   - Gestion mémoire avec G1GC
   - Actuator pour le monitoring
   - Port : 8080

3. **MySQL 8.0**
   - Configuration InnoDB optimisée
   - Slow query logging activé
   - Health checks automatiques
   - Volumes persistants pour les données
   - Port : 3307 (mappé depuis 3306)

#### Services de Monitoring

4. **Prometheus**
   - Collecte et stockage des métriques
   - Rétention des données : 15 jours
   - Port : 9090

5. **Grafana**
   - Visualisation des métriques
   - Dashboards pré-configurés
   - Port : 3000

6. **MySQL Exporter**
   - Exportation des métriques MySQL vers Prometheus
   - Port : 9104

7. **cAdvisor**
   - Monitoring des conteneurs Docker
   - Métriques CPU, mémoire, réseau, disque
   - Port : 8081

## 🛠️ Stack Technologique

### Frontend
- **React 19** - Bibliothèque UI
- **Vite 7** - Build tool et dev server
- **React Router 7** - Routage
- **TanStack Query 5** - Gestion des données serveur
- **Zustand 5** - State management
- **React Hook Form 7** - Gestion des formulaires
- **Axios** - Client HTTP
- **Tailwind CSS 3** - Framework CSS
- **Vitest** - Framework de tests

### Backend
- **Spring Boot 4.0.0** - Framework Java
- **Java 17** - Version JDK
- **Spring Security** - Authentification et autorisation
- **Spring Data JPA** - Accès aux données
- **MySQL Connector** - Driver MySQL
- **JWT** - Authentification token-based
- **Spring Boot Actuator** - Monitoring et métriques

### Infrastructure
- **Docker & Docker Compose** - Containerisation et orchestration
- **MySQL 8.0** - Base de données relationnelle
- **Nginx** - Serveur web et reverse proxy
- **Prometheus** - Collecte de métriques
- **Grafana** - Visualisation de métriques
- **cAdvisor** - Monitoring de conteneurs

## 📦 Installation et Déploiement

### Prérequis

- **Docker Engine** 20.10 ou supérieur
- **Docker Compose** 2.0 ou supérieur
- **RAM** : 4 GB minimum (8 GB recommandé pour le monitoring)
- **Espace disque** : 10 GB disponible
- **OS** : Windows, Linux, ou macOS

### Configuration Initiale

1. **Créer le fichier `.env`** à la racine du projet :

```env
# Base de données MySQL
MYSQL_ROOT_PASSWORD=votre_mot_de_passe_root_securise
MYSQL_DATABASE=si_releves
MYSQL_USER=app_user
MYSQL_PASSWORD=votre_mot_de_passe_app_securise

# Backend Spring Boot
BACKEND_PORT=8080
JWT_SECRET=votre_secret_jwt_super_long_et_securise_minimum_256_bits
JWT_EXPIRATION=1800000
CORS_ALLOWED_ORIGINS=http://localhost

# Frontend
FRONTEND_PORT=80

# Monitoring - Prometheus
PROMETHEUS_PORT=9090

# Monitoring - Grafana
GRAFANA_PORT=3000
GRAFANA_ADMIN_USER=admin
GRAFANA_ADMIN_PASSWORD=admin
GRAFANA_ROOT_URL=http://localhost:3000
```

2. **Construire et démarrer tous les services** :

```bash
# Construire les images
docker-compose build

# Démarrer tous les services en arrière-plan
docker-compose up -d

# Voir les logs de tous les services
docker-compose logs -f

# Voir les logs d'un service spécifique
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql

# Vérifier le statut de tous les services
docker-compose ps
```

3. **Vérifier que tous les services sont en cours d'exécution** :

```bash
# Vérifier les health checks
docker-compose ps
```

Tous les services devraient afficher un statut `healthy` après quelques secondes.

4. **Initialiser les données** (optionnel) :

**Option A : Script avec de vraies données (recommandé)**
```bash
# Windows (PowerShell)
.\scripts\seed-real-data.ps1

# Linux/macOS (Bash)
./scripts/seed-real-data.sh
```

Ce script crée :
- 10 quartiers à Rabat
- 50 clients avec de vrais noms marocains
- 60+ adresses réalistes dans différents quartiers

**Option B : Script via API (données de test minimales)**
```bash
# Windows (PowerShell)
.\scripts\seed-data-api.ps1

# Linux/macOS (Bash)
./scripts/seed-data-api.sh
```

### Accès aux Services

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost | Interface utilisateur principale |
| **Backend API** | http://localhost:8080/api | API REST |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentation interactive de l'API |
| **API Docs** | http://localhost:8080/api-docs | Documentation de l'API |
| **Health Check** | http://localhost:8080/actuator/health | État de santé du backend |
| **Prometheus** | http://localhost:9090 | Interface Prometheus |
| **Grafana** | http://localhost:3000 | Dashboards de monitoring |
| **cAdvisor** | http://localhost:8081 | Métriques des conteneurs |
| **MySQL** | localhost:3307 | Base de données (client externe) |

**Identifiants Grafana par défaut :**
- Username : `admin`
- Password : `admin` (à changer après la première connexion)

## 🗄️ Accès à la Base de Données MySQL

### Méthode 1 : Ligne de Commande (via Docker)

```bash
# Se connecter avec l'utilisateur root
docker-compose exec mysql mysql -u root -p

# Se connecter avec l'utilisateur applicatif
docker-compose exec mysql mysql -u app_user -p si_releves

# Commande directe (sans prompt interactif)
docker-compose exec mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} si_releves -e "SHOW TABLES;"
```

### Méthode 2 : Client MySQL Externe

Utilisez un client graphique comme **MySQL Workbench**, **DBeaver**, **phpMyAdmin**, ou **TablePlus**.

**Paramètres de connexion :**
- **Host** : `localhost` ou `127.0.0.1`
- **Port** : `3307` ⚠️ (pas 3306, car le port est mappé)
- **Username** : `root` ou `app_user`
- **Password** : Le mot de passe défini dans votre fichier `.env`
- **Database** : `si_releves`

### Méthode 3 : Commandes Utiles

```bash
# Voir les logs MySQL
docker-compose logs mysql

# Exécuter une requête SQL directement
docker-compose exec mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} si_releves -e "SELECT * FROM quartiers;"

# Exporter la base de données (sauvegarde)
docker-compose exec mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} si_releves > backup.sql

# Importer une base de données
docker-compose exec -T mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} si_releves < backup.sql

# Voir la taille de la base de données
docker-compose exec mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} -e "SELECT table_schema AS 'Database', ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)' FROM information_schema.TABLES WHERE table_schema = 'si_releves' GROUP BY table_schema;"
```

## 📊 Monitoring et Observabilité

### Prometheus

Prometheus collecte les métriques de :
- **Backend Spring Boot** (via Actuator)
- **MySQL** (via MySQL Exporter)
- **Conteneurs Docker** (via cAdvisor)

**Métriques disponibles :**
- CPU, mémoire, disque
- Temps de réponse des requêtes
- Nombre de requêtes par seconde
- État de santé des services
- Métriques MySQL (connexions, requêtes, etc.)

### Grafana

Grafana fournit des dashboards pré-configurés pour visualiser :
- Performance de l'application
- Utilisation des ressources
- Métriques de la base de données
- Santé des services

**Configuration :**
- Les datasources et dashboards sont automatiquement provisionnés depuis `grafana/provisioning/`
- Les dashboards sont disponibles après la première connexion

### Spring Boot Actuator

Le backend expose des endpoints de monitoring :

- `/actuator/health` - État de santé
- `/actuator/info` - Informations sur l'application
- `/actuator/metrics` - Liste des métriques disponibles
- `/actuator/metrics/{metric.name}` - Détails d'une métrique spécifique

### Health Checks Docker

Tous les services incluent des health checks automatiques :
- **MySQL** : Vérification via `mysqladmin ping` (intervalle 30s)
- **Backend** : Endpoint `/actuator/health` (intervalle 30s)
- **Frontend** : Endpoint `/health` (intervalle 30s)
- **Prometheus** : Endpoint `/-/healthy` (intervalle 30s)
- **Grafana** : Endpoint `/api/health` (intervalle 30s)
- **MySQL Exporter** : Endpoint `/metrics` (intervalle 30s)
- **cAdvisor** : Endpoint `/healthz` (intervalle 30s)

## 🔧 Configuration Avancée

### Optimisations MySQL

Le fichier `scripts/init.sql` configure automatiquement :
- Buffer pool InnoDB : 512 MB
- Log file size : 128 MB
- Max connections : 200
- Slow query log activé (seuil : 2 secondes)
- Charset UTF8MB4 avec collation unicode

### Optimisations Backend

- **JVM** : G1 Garbage Collector avec pause max 200ms
- **Mémoire** : Heap initial 256 MB, max 512 MB
- **Connection Pool** : HikariCP avec 20 connexions max
- **Batch Processing** : Optimisé pour les insertions/updates en masse

### Optimisations Frontend

- **Nginx** :
  - Gzip compression niveau 6
  - Cache des assets statiques (30 jours)
  - Cache-Control optimisé
  - Headers de sécurité (X-Frame-Options, X-XSS-Protection)

### Limites de Ressources

| Service | CPU Limit | Memory Limit | CPU Reservation | Memory Reservation |
|---------|-----------|--------------|-----------------|-------------------|
| MySQL | 2 cores | 1 GB | 0.5 cores | 512 MB |
| Backend | 2 cores | 768 MB | 0.5 cores | 256 MB |
| Frontend | 2 cores | 512 MB | 0.5 cores | 256 MB |
| Prometheus | 1 core | 512 MB | 0.25 cores | 256 MB |
| Grafana | 1 core | 512 MB | 0.25 cores | 256 MB |
| MySQL Exporter | 0.5 cores | 128 MB | 0.1 cores | 64 MB |
| cAdvisor | 1 core | 512 MB | 0.25 cores | 128 MB |

## 🧪 Tests

### Tests Backend

```bash
# Exécuter les tests dans le conteneur
docker-compose exec backend ./mvnw test

# Tests avec couverture
docker-compose exec backend ./mvnw test jacoco:report
```

### Tests Frontend

```bash
# Entrer dans le conteneur frontend
docker-compose exec frontend sh

# Ou depuis le répertoire frontend local
cd frontend
npm test

# Tests avec couverture
npm run test:coverage
```

## 🚀 Commandes Utiles

### Gestion des Services

```bash
# Arrêter tous les services
docker-compose down

# Arrêter et supprimer les volumes (⚠️ supprime les données)
docker-compose down -v

# Redémarrer un service spécifique
docker-compose restart backend

# Reconstruire un service spécifique
docker-compose build --no-cache frontend

# Voir les logs en temps réel
docker-compose logs -f backend

# Exécuter une commande dans un conteneur
docker-compose exec backend sh
docker-compose exec mysql mysql -u app_user -p si_releves
```

### Maintenance

```bash
# Sauvegarder la base de données
docker-compose exec mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} si_releves > backup_$(date +%Y%m%d_%H%M%S).sql

# Restaurer une sauvegarde
docker-compose exec -T mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} si_releves < backup.sql

# Mettre à jour les images
docker-compose pull

# Reconstruire après modifications
docker-compose build --no-cache

# Redéployer
docker-compose up -d
```

### Nettoyage

```bash
# Supprimer les images non utilisées
docker image prune -a

# Supprimer les volumes non utilisés
docker volume prune

# Nettoyage complet (⚠️ supprime tout)
docker system prune -a --volumes
```

## 📈 Évolutivité

### Scaling Horizontal

Pour augmenter la capacité :

```bash
# Scale le backend (exemple : 3 instances)
docker-compose up -d --scale backend=3

# Scale le frontend (exemple : 2 instances)
docker-compose up -d --scale frontend=2
```

**Note** : Pour un scaling complet en production, considérez :
- Load balancer (Nginx, Traefik)
- Base de données en cluster (MySQL Master-Slave)
- Cache distribué (Redis)
- File storage partagé

### Scaling Vertical

Ajustez les limites dans `docker-compose.yml` :

```yaml
deploy:
  resources:
    limits:
      cpus: '4'      # Augmenter selon les besoins
      memory: 2G     # Augmenter selon les besoins
```

## 🔒 Sécurité

### Bonnes Pratiques Implémentées

1. **Conteneurs non-root** : Backend exécuté avec utilisateur dédié
2. **Secrets dans .env** : Variables sensibles non versionnées (ajoutez `.env` au `.gitignore`)
3. **Réseau isolé** : Communication inter-services via réseau privé Docker
4. **Health checks** : Détection rapide des problèmes
5. **Limites de ressources** : Protection contre le DoS
6. **Headers de sécurité** : XSS, clickjacking protection
7. **JWT sécurisé** : Authentification token-based
8. **MySQL sécurisé** : Utilisateur applicatif avec privilèges limités

### Recommandations de Sécurité

- Changez tous les mots de passe par défaut
- Utilisez des secrets forts (minimum 16 caractères)
- Ne commitez jamais le fichier `.env`
- Activez HTTPS en production
- Configurez un firewall
- Mettez à jour régulièrement les images Docker

## 📚 Structure du Projet

```
Projet-SI-Releves-final/
├── backend/                    # Application Spring Boot
│   ├── Dockerfile             # Build multi-stage backend
│   ├── pom.xml                # Dépendances Maven
│   └── src/                   # Code source Java
│       ├── main/
│       └── test/
│
├── frontend/                  # Application React
│   ├── Dockerfile             # Build multi-stage frontend
│   ├── nginx.conf             # Configuration Nginx
│   ├── package.json           # Dépendances npm
│   ├── vite.config.js         # Configuration Vite
│   ├── tailwind.config.js     # Configuration Tailwind
│   └── src/                   # Code source React
│       ├── components/        # Composants React
│       ├── pages/             # Pages de l'application
│       ├── services/          # Services API
│       └── test/              # Tests
│
├── scripts/                   # Scripts utilitaires
│   ├── init.sql               # Initialisation MySQL
│   └── seed-data-api.ps1      # Script de données de test
│
├── prometheus/                # Configuration Prometheus
│   ├── prometheus.yml         # Configuration principale
│   └── alert_rules.yml        # Règles d'alerte
│
├── grafana/                   # Configuration Grafana
│   └── provisioning/          # Provisioning automatique
│       ├── datasources/       # Sources de données
│       └── dashboards/        # Dashboards
│
├── docker-compose.yml         # Orchestration Docker
├── .env                       # Variables d'environnement (non versionné)
└── README.md                  # Ce fichier
```

## 🐛 Dépannage

### Problèmes Courants

**Les services ne démarrent pas :**
```bash
# Vérifier les logs
docker-compose logs

# Vérifier l'utilisation des ports
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Linux/macOS

# Reconstruire les images
docker-compose build --no-cache
```

**Erreur de connexion à la base de données :**
```bash
# Vérifier que MySQL est en cours d'exécution
docker-compose ps mysql

# Vérifier les logs MySQL
docker-compose logs mysql

# Vérifier les variables d'environnement
docker-compose exec backend env | grep MYSQL
```

**Frontend ne se connecte pas au backend :**
- Vérifiez que `CORS_ALLOWED_ORIGINS` dans `.env` correspond à l'URL du frontend
- Vérifiez que le backend est accessible : `curl http://localhost:8080/actuator/health`

**Problèmes de mémoire :**
- Augmentez les limites dans `docker-compose.yml`
- Vérifiez l'utilisation avec `docker stats`

## 🤝 Contribution

Pour contribuer au projet :

1. Créer une branche depuis `main`
2. Effectuer vos modifications
3. Tester localement avec Docker
4. Vérifier que tous les tests passent
5. Soumettre une pull request

## 📝 Licence

[À compléter selon votre licence]

## 👥 Équipe

[À compléter avec les informations de l'équipe]

---

**Note** : Ce projet utilise Docker Compose pour le développement et le déploiement. Pour la production, considérez l'utilisation d'un orchestrateur plus robuste comme Kubernetes, ainsi que des services cloud managés pour la base de données et le monitoring.
