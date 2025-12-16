# PHASE 2 : CONCEPTION - ARCHITECTURE TECHNIQUE

**Projet :** Système de Gestion des Relevés de Compteurs d'Eau et d'Électricité  
**Client :** RABAT ENERGIE & EAU (REE)  
**Application concernée :** SI Relevés (Backoffice Web)  
**Date de conception :** 16 Décembre 2024

---

## INTRODUCTION

Ce document présente l'architecture technique du système "SI Relevés". Il a été généré automatiquement par **IA générative** en se basant sur :
- Les besoins fonctionnels (Phase 1)
- Les spécifications détaillées (Phase 2.1 à 2.4)
- Les choix technologiques validés

L'architecture est conçue pour être :
- **Moderne** : Technologies récentes et éprouvées
- **Scalable** : Capacité à monter en charge
- **Maintenable** : Code propre et bien structuré
- **Sécurisée** : Bonnes pratiques de sécurité
- **Conteneurisée** : Déploiement Docker Compose

---

## TABLE DES MATIÈRES

1. [Vue d'ensemble de l'architecture](#1-vue-densemble-de-larchitecture)
2. [Stack technologique](#2-stack-technologique)
3. [Architecture applicative](#3-architecture-applicative)
4. [Architecture de déploiement](#4-architecture-de-déploiement)
5. [Sécurité](#5-sécurité)
6. [Structure des projets](#6-structure-des-projets)
7. [Configuration Docker](#7-configuration-docker)
8. [CI/CD avec GitHub Actions](#8-cicd-avec-github-actions)
9. [Monitoring et logs](#9-monitoring-et-logs)
10. [Documentation API](#10-documentation-api)

---

## 1. VUE D'ENSEMBLE DE L'ARCHITECTURE

### 1.1. Architecture 3-tiers

```
┌─────────────────────────────────────────────────────────────┐
│                    COUCHE PRÉSENTATION                       │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │          Frontend React (SPA)                         │  │
│  │  - Interface utilisateur responsive                   │  │
│  │  - Communication API REST                             │  │
│  │  - Gestion d'état (Redux/Context API)                │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↕ HTTPS/REST
┌─────────────────────────────────────────────────────────────┐
│                     COUCHE MÉTIER (API)                      │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │        Backend Spring Boot (Java)                     │  │
│  │  - API REST                                           │  │
│  │  - Logique métier                                     │  │
│  │  - Sécurité JWT                                       │  │
│  │  - Validation des données                             │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↕ JDBC
┌─────────────────────────────────────────────────────────────┐
│                    COUCHE DONNÉES                            │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              MySQL 8.0                                │  │
│  │  - Base de données relationnelle                      │  │
│  │  - Triggers et procédures stockées                    │  │
│  │  - Sauvegarde automatique                             │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### 1.2. Composants principaux

| Composant | Technologie | Port | Rôle |
|-----------|-------------|------|------|
| **Frontend** | React 18 | 3000 | Interface utilisateur web |
| **Backend API** | Spring Boot 3.2 | 8080 | API REST + logique métier |
| **Base de données** | MySQL 8.0 | 3306 | Stockage des données |
| **Reverse Proxy** | Nginx | 80/443 | Routage HTTP/HTTPS |
| **Mail (dev)** | MailHog | 8025 | Test des emails |
| **Documentation** | Swagger UI | 8080/swagger-ui | Documentation API interactive |

---

## 2. STACK TECHNOLOGIQUE

### 2.1. Technologies et versions

#### **Backend - Spring Boot**

| Technologie | Version | Rôle |
|-------------|---------|------|
| **Java** | 17 LTS | Langage de programmation |
| **Spring Boot** | 3.2.1 | Framework principal |
| **Spring Web** | 3.2.1 | API REST |
| **Spring Data JPA** | 3.2.1 | ORM / accès données |
| **Spring Security** | 6.2.1 | Sécurité et authentification |
| **Spring Validation** | 3.2.1 | Validation des données |
| **Hibernate** | 6.4.1 | Implémentation JPA |
| **MySQL Connector/J** | 8.2.0 | Driver JDBC MySQL |
| **JWT (jjwt)** | 0.12.3 | Gestion des tokens JWT |
| **Lombok** | 1.18.30 | Réduction du code boilerplate |
| **MapStruct** | 1.5.5 | Mapping objet-objet |
| **SpringDoc OpenAPI** | 2.3.0 | Documentation Swagger |
| **JUnit 5** | 5.10.1 | Tests unitaires |
| **Mockito** | 5.7.0 | Mocking pour tests |
| **Maven** | 3.9+ | Gestion de build |

**Fichier de configuration : `pom.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
        <relativePath/>
    </parent>
    
    <groupId>ma.ree</groupId>
    <artifactId>si-releves-backend</artifactId>
    <version>1.0.0</version>
    <name>SI Relevés Backend</name>
    <description>Backend API pour SI Relevés - REE</description>
    
    <properties>
        <java.version>17</java.version>
        <jwt.version>0.12.3</jwt.version>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        <springdoc.version>2.3.0</springdoc.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- MySQL -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- MapStruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>
        
        <!-- SpringDoc OpenAPI (Swagger) -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc.version}</version>
        </dependency>
        
        <!-- Tests -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

#### **Frontend - React**

| Technologie | Version | Rôle |
|-------------|---------|------|
| **Node.js** | 20 LTS | Runtime JavaScript |
| **React** | 18.2.0 | Framework UI |
| **React Router** | 6.21.0 | Routing SPA |
| **Axios** | 1.6.2 | Client HTTP |
| **React Query** | 5.14.0 | Gestion état serveur / cache |
| **Zustand** | 4.4.7 | Gestion état global |
| **React Hook Form** | 7.49.2 | Gestion des formulaires |
| **Yup** | 1.3.3 | Validation des schémas |
| **Tailwind CSS** | 3.4.0 | Framework CSS utility-first |
| **Recharts** | 2.10.3 | Bibliothèque de graphiques |
| **React Icons** | 4.12.0 | Icônes |
| **date-fns** | 3.0.6 | Manipulation des dates |
| **jwt-decode** | 4.0.0 | Décodage JWT côté client |
| **Vite** | 5.0.8 | Bundler rapide |
| **Vitest** | 1.0.4 | Framework de tests |
| **Testing Library** | 14.1.2 | Tests de composants React |

**Fichier de configuration : `package.json`**

```json
{
  "name": "si-releves-frontend",
  "version": "1.0.0",
  "private": true,
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "test": "vitest",
    "test:ui": "vitest --ui",
    "lint": "eslint . --ext js,jsx --report-unused-disable-directives --max-warnings 0"
  },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.21.0",
    "axios": "^1.6.2",
    "@tanstack/react-query": "^5.14.0",
    "zustand": "^4.4.7",
    "react-hook-form": "^7.49.2",
    "yup": "^1.3.3",
    "recharts": "^2.10.3",
    "react-icons": "^4.12.0",
    "date-fns": "^3.0.6",
    "jwt-decode": "^4.0.0"
  },
  "devDependencies": {
    "@vitejs/plugin-react": "^4.2.1",
    "vite": "^5.0.8",
    "tailwindcss": "^3.4.0",
    "autoprefixer": "^10.4.16",
    "postcss": "^8.4.32",
    "vitest": "^1.0.4",
    "@testing-library/react": "^14.1.2",
    "@testing-library/jest-dom": "^6.1.5",
    "eslint": "^8.55.0",
    "eslint-plugin-react": "^7.33.2"
  }
}
```

---

#### **Base de données**

| Technologie | Version | Rôle |
|-------------|---------|------|
| **MySQL** | 8.0.35 | SGBD relationnel |

---

#### **Infrastructure et DevOps**

| Technologie | Version | Rôle |
|-------------|---------|------|
| **Docker** | 24.0+ | Conteneurisation |
| **Docker Compose** | 2.23+ | Orchestration multi-conteneurs |
| **Nginx** | 1.25-alpine | Reverse proxy / serveur web |
| **MailHog** | 1.0.1 | Serveur SMTP de test |

---

### 2.2. Outils de développement

| Outil | Version | Usage |
|-------|---------|-------|
| **Git** | 2.40+ | Gestion de versions |
| **GitHub** | - | Hébergement du code |
| **GitHub Actions** | - | CI/CD |
| **IntelliJ IDEA** | 2023.3+ | IDE Java (recommandé) |
| **VS Code** | 1.85+ | IDE Frontend (recommandé) |
| **Postman** | 10.20+ | Tests API manuels |
| **Maven** | 3.9+ | Build Java |
| **npm** | 10+ | Gestionnaire de paquets Node.js |

---

## 3. ARCHITECTURE APPLICATIVE

### 3.1. Backend - Architecture en couches (Spring Boot)

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌───────────────────────────────────────────────────┐  │
│  │  Controllers (REST Endpoints)                      │  │
│  │  - UserController                                  │  │
│  │  - CompteurController                              │  │
│  │  - AgentController                                 │  │
│  │  - ReleveController                                │  │
│  │  - DashboardController                             │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│                    SERVICE LAYER                         │
│  ┌───────────────────────────────────────────────────┐  │
│  │  Services (Business Logic)                         │  │
│  │  - UserService                                     │  │
│  │  - CompteurService                                 │  │
│  │  - AgentService                                    │  │
│  │  - ReleveService                                   │  │
│  │  - DashboardService                                │  │
│  │  - AuthService                                     │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│                  PERSISTENCE LAYER                       │
│  ┌───────────────────────────────────────────────────┐  │
│  │  Repositories (Data Access)                        │  │
│  │  - UserRepository (JPA)                            │  │
│  │  - CompteurRepository                              │  │
│  │  - AgentRepository                                 │  │
│  │  - ReleveRepository                                │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                        │
│                      MySQL 8.0                           │
└─────────────────────────────────────────────────────────┘

         CROSS-CUTTING CONCERNS (Aspects)
┌─────────────────────────────────────────────────────────┐
│  - Security (JWT, Spring Security)                       │
│  - Exception Handling (Global Exception Handler)        │
│  - Logging (SLF4J + Logback)                            │
│  - Validation (Bean Validation)                          │
│  - DTO Mapping (MapStruct)                               │
└─────────────────────────────────────────────────────────┘
```

#### **Modèle de données (Entities JPA)**

```java
// Exemple d'entité
@Entity
@Table(name = "compteur")
@Getter @Setter
@NoArgsConstructor
public class Compteur {
    @Id
    @Column(length = 9)
    private String idCompteur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adresse", nullable = false)
    private Adresse adresse;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCompteur type;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal indexActuel = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private LocalDate dateInstallation;
    
    @Column
    private LocalDateTime dateDerniereReleve;
    
    @Column(nullable = false)
    private Boolean pourEspacesCommuns = false;
    
    @Column(nullable = false)
    private Boolean actif = true;
    
    @OneToMany(mappedBy = "compteur", cascade = CascadeType.ALL)
    private List<Releve> releves;
}
```

---

### 3.2. Frontend - Architecture React

```
┌─────────────────────────────────────────────────────────┐
│                     COMPONENTS                           │
│  ┌───────────────────────────────────────────────────┐  │
│  │  Pages (Route Components)                          │  │
│  │  - LoginPage                                       │  │
│  │  - DashboardPage                                   │  │
│  │  │  CompteurListPage                               │  │
│  │  - CompteurCreatePage                              │  │
│  │  - ReleveListPage                                  │  │
│  │  - AgentListPage                                   │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  UI Components (Reusable)                          │  │
│  │  - Button, Input, Modal                            │  │
│  │  - Table, Card, Badge                              │  │
│  │  - Chart (Recharts)                                │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│                  STATE MANAGEMENT                        │
│  ┌───────────────────────────────────────────────────┐  │
│  │  Zustand (Global State)                            │  │
│  │  - authStore (user, token)                         │  │
│  │  - uiStore (modals, notifications)                 │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  React Query (Server State)                       │  │
│  │  - useCompteurs, useReleves                        │  │
│  │  - Cache, Refetch, Mutations                       │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│                    API LAYER                             │
│  ┌───────────────────────────────────────────────────┐  │
│  │  Axios Instances                                   │  │
│  │  - apiClient (with interceptors)                   │  │
│  │  - JWT token injection                             │  │
│  │  - Error handling                                  │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │  API Services                                      │  │
│  │  - authService, compteurService                    │  │
│  │  - agentService, releveService                     │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                         ↓
                 Backend API (Spring Boot)
```

#### **Structure de dossiers React recommandée**

```
frontend/
├── public/
│   └── favicon.ico
├── src/
│   ├── api/                    # Services API
│   │   ├── axios.config.js
│   │   ├── authService.js
│   │   ├── compteurService.js
│   │   ├── agentService.js
│   │   └── releveService.js
│   ├── components/             # Composants réutilisables
│   │   ├── common/
│   │   │   ├── Button.jsx
│   │   │   ├── Input.jsx
│   │   │   ├── Modal.jsx
│   │   │   └── Table.jsx
│   │   ├── layout/
│   │   │   ├── Header.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   └── Layout.jsx
│   │   └── charts/
│   │       └── CoverageChart.jsx
│   ├── pages/                  # Pages (routes)
│   │   ├── auth/
│   │   │   ├── LoginPage.jsx
│   │   │   └── ChangePasswordPage.jsx
│   │   ├── dashboard/
│   │   │   └── DashboardPage.jsx
│   │   ├── compteurs/
│   │   │   ├── CompteurListPage.jsx
│   │   │   ├── CompteurDetailPage.jsx
│   │   │   └── CompteurCreatePage.jsx
│   │   ├── agents/
│   │   │   ├── AgentListPage.jsx
│   │   │   └── AgentDetailPage.jsx
│   │   └── releves/
│   │       ├── ReleveListPage.jsx
│   │       └── ReleveDetailPage.jsx
│   ├── hooks/                  # Custom React hooks
│   │   ├── useAuth.js
│   │   └── useDebounce.js
│   ├── store/                  # Zustand stores
│   │   ├── authStore.js
│   │   └── uiStore.js
│   ├── utils/                  # Utilitaires
│   │   ├── formatters.js
│   │   ├── validators.js
│   │   └── constants.js
│   ├── routes/                 # Configuration des routes
│   │   ├── AppRoutes.jsx
│   │   └── PrivateRoute.jsx
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── .env.example
├── package.json
├── vite.config.js
└── tailwind.config.js
```

---

## 4. ARCHITECTURE DE DÉPLOIEMENT

### 4.1. Schéma d'architecture Docker Compose

```
┌─────────────────────────────────────────────────────────────────┐
│                         HOST MACHINE                             │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │              DOCKER COMPOSE NETWORK                         │ │
│  │                   (si-releves-network)                      │ │
│  │                                                             │ │
│  │  ┌──────────────┐    ┌──────────────┐    ┌─────────────┐ │ │
│  │  │              │    │              │    │             │ │ │
│  │  │    Nginx     │───▶│   Frontend   │    │  MySQL 8.0  │ │ │
│  │  │   (Proxy)    │    │    React     │    │             │ │ │
│  │  │              │    │              │    │  Port: 3306 │ │ │
│  │  │ Port 80/443  │    │  Port: 3000  │    │             │ │ │
│  │  │              │    │              │    │  Volume:    │ │ │
│  │  └──────┬───────┘    └──────────────┘    │  mysql_data │ │ │
│  │         │                                 └──────▲──────┘ │ │
│  │         │                                        │        │ │
│  │         │            ┌──────────────┐           │        │ │
│  │         │            │              │           │        │ │
│  │         └───────────▶│   Backend    │───────────┘        │ │
│  │                      │  Spring Boot │                    │ │
│  │                      │              │                    │ │
│  │                      │  Port: 8080  │                    │ │
│  │                      │              │                    │ │
│  │                      └──────┬───────┘                    │ │
│  │                             │                            │ │
│  │                             ▼                            │ │
│  │                      ┌──────────────┐                    │ │
│  │                      │   MailHog    │                    │ │
│  │                      │  SMTP Test   │                    │ │
│  │                      │              │                    │ │
│  │                      │ SMTP: 1025   │                    │ │
│  │                      │ Web:  8025   │                    │ │
│  │                      └──────────────┘                    │ │
│  │                                                           │ │
│  └───────────────────────────────────────────────────────────┘ │
│                                                                  │
│  Volumes persistants:                                            │
│  - mysql_data     (données MySQL)                                │
│  - logs           (logs applicatifs)                             │
└─────────────────────────────────────────────────────────────────┘

Accès externes:
- http://localhost        → Nginx (reverse proxy)
- http://localhost/api    → Backend API
- http://localhost:8080   → Backend direct (dev)
- http://localhost:3000   → Frontend direct (dev)
- http://localhost:8025   → MailHog UI
- http://localhost:8080/swagger-ui → Swagger API docs
```

---

### 4.2. Fichier docker-compose.yml

```yaml
version: '3.8'

services:
  # Base de données MySQL
  mysql:
    image: mysql:8.0.35
    container_name: si-releves-mysql
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: root_password_change_me
      MYSQL_DATABASE: si_releves
      MYSQL_USER: si_releves_user
      MYSQL_PASSWORD: si_releves_password_change_me
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./database/init.sql:/docker-entrypoint-initdb.d/init.sql:ro
    networks:
      - si-releves-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Backend Spring Boot
  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: si-releves-backend
    restart: unless-stopped
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/si_releves?useSSL=false&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: si_releves_user
      SPRING_DATASOURCE_PASSWORD: si_releves_password_change_me
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_MAIL_HOST: mailhog
      SPRING_MAIL_PORT: 1025
      JWT_SECRET: your_jwt_secret_key_change_me_in_production
      JWT_EXPIRATION: 1800000
    ports:
      - "8080:8080"
    depends_on:
      mysql:
        condition: service_healthy
      mailhog:
        condition: service_started
    volumes:
      - ./logs:/app/logs
    networks:
      - si-releves-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Frontend React
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: si-releves-frontend
    restart: unless-stopped
    environment:
      VITE_API_URL: http://localhost/api
    ports:
      - "3000:80"
    depends_on:
      - backend
    networks:
      - si-releves-network

  # Nginx Reverse Proxy
  nginx:
    image: nginx:1.25-alpine
    container_name: si-releves-nginx
    restart: unless-stopped
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/ssl:/etc/nginx/ssl:ro
    depends_on:
      - backend
      - frontend
    networks:
      - si-releves-network

  # MailHog (serveur SMTP de test)
  mailhog:
    image: mailhog/mailhog:v1.0.1
    container_name: si-releves-mailhog
    restart: unless-stopped
    ports:
      - "1025:1025"  # SMTP
      - "8025:8025"  # Web UI
    networks:
      - si-releves-network

networks:
  si-releves-network:
    driver: bridge

volumes:
  mysql_data:
    driver: local
```

---

### 4.3. Dockerfile Backend (Spring Boot)

```dockerfile
# backend/Dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY src ./src

# Build de l'application
RUN mvn clean package -DskipTests

# Image de production
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Point d'entrée
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

### 4.4. Dockerfile Frontend (React)

```dockerfile
# frontend/Dockerfile

# Étape 1: Build
FROM node:20-alpine AS build
WORKDIR /app

# Copier package.json et installer les dépendances
COPY package*.json ./
RUN npm ci

# Copier le code source et build
COPY . .
RUN npm run build

# Étape 2: Production avec Nginx
FROM nginx:1.25-alpine
WORKDIR /usr/share/nginx/html

# Supprimer les fichiers par défaut
RUN rm -rf ./*

# Copier les fichiers buildés
COPY --from=build /app/dist .

# Copier la configuration Nginx custom
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

**Configuration Nginx pour le frontend :**

```nginx
# frontend/nginx.conf
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # Gzip compression
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css text/xml text/javascript application/javascript application/xml+rss application/json;

    # SPA routing
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Cache statique
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

---

### 4.5. Configuration Nginx (Reverse Proxy)

```nginx
# nginx/nginx.conf
events {
    worker_connections 1024;
}

http {
    upstream backend {
        server backend:8080;
    }

    upstream frontend {
        server frontend:80;
    }

    server {
        listen 80;
        server_name localhost;

        # Logs
        access_log /var/log/nginx/access.log;
        error_log /var/log/nginx/error.log;

        # Frontend (SPA React)
        location / {
            proxy_pass http://frontend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }

        # Backend API
        location /api/ {
            proxy_pass http://backend/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            # Timeout pour les requêtes longues
            proxy_connect_timeout 60s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;
        }

        # Swagger UI
        location /swagger-ui {
            proxy_pass http://backend/swagger-ui;
            proxy_set_header Host $host;
        }

        # Actuator (optionnel, pour monitoring)
        location /actuator {
            proxy_pass http://backend/actuator;
            proxy_set_header Host $host;
        }
    }

    # Configuration HTTPS (optionnel, certificats auto-signés)
    # server {
    #     listen 443 ssl;
    #     server_name localhost;
    #
    #     ssl_certificate /etc/nginx/ssl/cert.pem;
    #     ssl_certificate_key /etc/nginx/ssl/key.pem;
    #
    #     # ... même configuration que ci-dessus
    # }
}
```

---

## 5. SÉCURITÉ

### 5.1. Authentification JWT

**Flux d'authentification :**

```
1. Client → POST /api/auth/login (email, password)
2. Backend vérifie credentials
3. Backend génère token JWT (expire 30 min)
4. Backend → Client : { token, user }
5. Client stocke token (localStorage/sessionStorage)
6. Client → Toutes requêtes : Header "Authorization: Bearer {token}"
7. Backend valide token JWT à chaque requête
```

**Configuration Spring Security (exemple) :**

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors()
            .and()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/users/**").hasRole("SUPERADMIN")
                .requestMatchers("/api/**").hasAnyRole("SUPERADMIN", "UTILISATEUR")
                .anyRequest().authenticated()
            )
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

---

### 5.2. Chiffrement des mots de passe

**BCrypt avec Spring Security :**

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10); // cost factor = 10
}
```

---

### 5.3. Protection CORS

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

### 5.4. Variables d'environnement sensibles

**Fichier `.env` (NE PAS COMMITTER) :**

```env
# Base de données
DB_HOST=mysql
DB_PORT=3306
DB_NAME=si_releves
DB_USER=si_releves_user
DB_PASSWORD=VotreMotDePasseSecurise123!

# JWT
JWT_SECRET=VotreCleSecreteJWTLongueEtSecurisee456!
JWT_EXPIRATION=1800000

# Email
MAIL_HOST=mailhog
MAIL_PORT=1025
MAIL_USERNAME=
MAIL_PASSWORD=
```

---

## 6. STRUCTURE DES PROJETS

### 6.1. Structure Backend Spring Boot

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ma/
│   │   │       └── ree/
│   │   │           └── sireleves/
│   │   │               ├── SiRelevesApplication.java
│   │   │               ├── config/
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── CorsConfig.java
│   │   │               │   ├── JwtConfig.java
│   │   │               │   └── OpenApiConfig.java
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── UserController.java
│   │   │               │   ├── CompteurController.java
│   │   │               │   ├── AgentController.java
│   │   │               │   ├── ReleveController.java
│   │   │               │   └── DashboardController.java
│   │   │               ├── dto/
│   │   │               │   ├── request/
│   │   │               │   │   ├── LoginRequest.java
│   │   │               │   │   ├── CreateUserRequest.java
│   │   │               │   │   └── CreateCompteurRequest.java
│   │   │               │   └── response/
│   │   │               │       ├── LoginResponse.java
│   │   │               │       ├── UserResponse.java
│   │   │               │       └── CompteurResponse.java
│   │   │               ├── entity/
│   │   │               │   ├── User.java
│   │   │               │   ├── Client.java
│   │   │               │   ├── Quartier.java
│   │   │               │   ├── Adresse.java
│   │   │               │   ├── Agent.java
│   │   │               │   ├── Compteur.java
│   │   │               │   └── Releve.java
│   │   │               ├── repository/
│   │   │               │   ├── UserRepository.java
│   │   │               │   ├── CompteurRepository.java
│   │   │               │   ├── AgentRepository.java
│   │   │               │   └── ReleveRepository.java
│   │   │               ├── service/
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── UserService.java
│   │   │               │   ├── CompteurService.java
│   │   │               │   ├── AgentService.java
│   │   │               │   ├── ReleveService.java
│   │   │               │   ├── EmailService.java
│   │   │               │   └── DashboardService.java
│   │   │               ├── security/
│   │   │               │   ├── JwtTokenProvider.java
│   │   │               │   ├── JwtAuthenticationFilter.java
│   │   │               │   └── UserDetailsServiceImpl.java
│   │   │               ├── exception/
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── ResourceNotFoundException.java
│   │   │               │   └── BadRequestException.java
│   │   │               ├── mapper/
│   │   │               │   ├── UserMapper.java
│   │   │               │   ├── CompteurMapper.java
│   │   │               │   └── ReleveMapper.java
│   │   │               └── util/
│   │   │                   ├── PasswordGenerator.java
│   │   │                   └── CompteurIdGenerator.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── logback-spring.xml
│   └── test/
│       └── java/
│           └── ma/
│               └── ree/
│                   └── sireleves/
│                       ├── controller/
│                       ├── service/
│                       └── repository/
├── Dockerfile
├── pom.xml
└── README.md
```

---

### 6.2. Fichier application.yml (Backend)

```yaml
spring:
  application:
    name: si-releves-backend
  
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/si_releves}
    username: ${SPRING_DATASOURCE_USERNAME:root}
    password: ${SPRING_DATASOURCE_PASSWORD:root}
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
  
  mail:
    host: ${SPRING_MAIL_HOST:localhost}
    port: ${SPRING_MAIL_PORT:1025}
    username: ${SPRING_MAIL_USERNAME:}
    password: ${SPRING_MAIL_PASSWORD:}
    properties:
      mail:
        smtp:
          auth: false
          starttls:
            enable: false

jwt:
  secret: ${JWT_SECRET:default-secret-key-change-in-production}
  expiration: ${JWT_EXPIRATION:1800000}

# Actuator (monitoring)
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

# Swagger/OpenAPI
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: method

# Logging
logging:
  level:
    root: INFO
    ma.ree.sireleves: DEBUG
  file:
    name: logs/si-releves.log
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

---

## 7. CONFIGURATION DOCKER

### 7.1. Commandes Docker Compose

**Démarrer tous les services :**
```bash
docker-compose up -d
```

**Voir les logs :**
```bash
docker-compose logs -f
docker-compose logs -f backend
```

**Arrêter les services :**
```bash
docker-compose down
```

**Rebuild après modification :**
```bash
docker-compose up -d --build
```

**Accéder à MySQL :**
```bash
docker exec -it si-releves-mysql mysql -u si_releves_user -p
```

---

### 7.2. Script d'initialisation base de données

**Fichier : `database/init.sql`**

```sql
-- Ce fichier est exécuté automatiquement au premier démarrage de MySQL
-- Il contient le script MLD_Create_Database.sql généré en Phase 2.3

-- (Copier le contenu du fichier MLD_Create_Database.sql ici)
```

---

## 8. CI/CD AVEC GITHUB ACTIONS

### 8.1. Workflow CI/CD

**Fichier : `.github/workflows/ci-cd.yml`**

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  # Tests Backend
  backend-tests:
    runs-on: ubuntu-latest
    
    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: si_releves_test
        ports:
          - 3306:3306
        options: >-
          --health-cmd="mysqladmin ping"
          --health-interval=10s
          --health-timeout=5s
          --health-retries=3
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      
      - name: Run tests
        run: |
          cd backend
          mvn clean test
      
      - name: Build JAR
        run: |
          cd backend
          mvn clean package -DskipTests
      
      - name: Upload artifacts
        uses: actions/upload-artifact@v3
        with:
          name: backend-jar
          path: backend/target/*.jar

  # Tests Frontend
  frontend-tests:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up Node.js
        uses: actions/setup-node@v4
        with:
          node-version: '20'
          cache: 'npm'
          cache-dependency-path: frontend/package-lock.json
      
      - name: Install dependencies
        run: |
          cd frontend
          npm ci
      
      - name: Run tests
        run: |
          cd frontend
          npm run test
      
      - name: Build
        run: |
          cd frontend
          npm run build
      
      - name: Upload artifacts
        uses: actions/upload-artifact@v3
        with:
          name: frontend-dist
          path: frontend/dist

  # Build Docker Images
  build-docker:
    needs: [backend-tests, frontend-tests]
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3
      
      - name: Build Backend Image
        run: |
          cd backend
          docker build -t si-releves-backend:latest .
      
      - name: Build Frontend Image
        run: |
          cd frontend
          docker build -t si-releves-frontend:latest .

  # Code Quality (SonarQube - optionnel)
  code-quality:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      
      # Configuration SonarQube ici si nécessaire
```

---

### 8.2. Workflow Tests uniquement

**Fichier : `.github/workflows/tests.yml`**

```yaml
name: Tests

on:
  pull_request:
    branches: [ main, develop ]

jobs:
  test-backend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run Backend Tests
        run: |
          cd backend
          mvn test
  
  test-frontend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up Node
        uses: actions/setup-node@v4
        with:
          node-version: '20'
      - name: Run Frontend Tests
        run: |
          cd frontend
          npm ci
          npm test
```

---

## 9. MONITORING ET LOGS

### 9.1. Logging Backend (Logback)

**Fichier : `backend/src/main/resources/logback-spring.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- File Appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/si-releves.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/si-releves.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>
    
    <!-- Loggers -->
    <logger name="ma.ree.sireleves" level="DEBUG"/>
    <logger name="org.springframework" level="INFO"/>
    <logger name="org.hibernate" level="INFO"/>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

---

### 9.2. Logging Frontend (Console)

**Configuration simple dans `main.jsx` :**

```javascript
// Logging simple en développement
if (import.meta.env.DEV) {
  console.log('🚀 SI Relevés Frontend - Mode Développement');
}

// Capturer les erreurs React
window.addEventListener('error', (event) => {
  console.error('❌ Erreur capturée:', event.error);
});
```

---

### 9.3. Actuator (Spring Boot)

**Endpoints de monitoring disponibles :**

- `GET /actuator/health` : État de santé de l'application
- `GET /actuator/info` : Informations sur l'application
- `GET /actuator/metrics` : Métriques de performance

**Exemple de réponse `/actuator/health` :**

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

---

## 10. DOCUMENTATION API

### 10.1. Swagger/OpenAPI

**Configuration Swagger (Backend) :**

```java
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SI Relevés API")
                .version("1.0.0")
                .description("API REST pour le système de gestion des relevés de compteurs - REE")
                .contact(new Contact()
                    .name("REE Support")
                    .email("support@ree.ma")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication", 
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
```

**Accès à Swagger UI :**
- URL : `http://localhost:8080/swagger-ui.html`
- Documentation JSON : `http://localhost:8080/v3/api-docs`

---

### 10.2. Exemple d'endpoint documenté

```java
@RestController
@RequestMapping("/api/compteurs")
@Tag(name = "Compteurs", description = "Gestion des compteurs d'eau et d'électricité")
public class CompteurController {
    
    @Operation(summary = "Créer un nouveau compteur",
               description = "Crée un nouveau compteur et l'associe à une adresse")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compteur créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Compteur déjà existant")
    })
    @PostMapping
    public ResponseEntity<CompteurResponse> createCompteur(
            @Valid @RequestBody CreateCompteurRequest request) {
        CompteurResponse response = compteurService.createCompteur(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

---

## CONCLUSION

Ce document d'architecture technique a été généré automatiquement par **IA générative** et fournit :

✅ **Architecture complète :**
- Architecture 3-tiers
- Architecture Docker Compose
- Schémas détaillés

✅ **Stack technologique complète :**
- Backend : Spring Boot 3.2 + Java 17
- Frontend : React 18 + Vite
- Base de données : MySQL 8.0
- DevOps : Docker Compose + GitHub Actions

✅ **Configurations prêtes à l'emploi :**
- Fichiers Docker (Dockerfile, docker-compose.yml)
- Configuration Spring Boot (application.yml)
- Configuration React (package.json, vite.config.js)
- CI/CD GitHub Actions

✅ **Sécurité :**
- JWT Authentication
- BCrypt password hashing
- CORS configuration
- HTTPS (certificats auto-signés)

✅ **Documentation :**
- Swagger/OpenAPI intégré
- Structure de projets détaillée
- Guides de déploiement

**L'application est prête à être développée avec cette architecture !** 🚀

---

**Document généré automatiquement par IA - Claude (Anthropic)**  
**Technique utilisée :** IA générative pour architecture logicielle et configuration DevOps  
**Stack : Spring Boot + React + MySQL + Docker**
