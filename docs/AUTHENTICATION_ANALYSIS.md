# 🔐 Analyse Complète du Système d'Authentification

## 📋 Vue d'ensemble

Ce document explique en détail le système d'authentification du projet SI Relevés, comment il fonctionne dans le backend, et ce qui doit être implémenté dans le frontend.

---

## 🏗️ Architecture Backend

### 1. Routes d'Authentification

Le backend expose les endpoints suivants sous `/api/auth` :

#### **POST /api/auth/login**
- **Description** : Connexion d'un utilisateur
- **Body** :
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Réponse (200 OK)** :
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "idUtilisateur": 1,
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "user@example.com",
    "role": "Superadmin",
    "premiereConnexion": true
  }
  ```
- **Erreurs possibles** :
  - `400 Bad Request` : Email ou mot de passe manquant
  - `401 Unauthorized` : Email ou mot de passe incorrect
  - `403 Forbidden` : Compte désactivé

#### **POST /api/auth/change-password/{userId}**
- **Description** : Changer le mot de passe d'un utilisateur
- **Headers** : `Authorization: Bearer <token>` (requis)
- **Body** :
  ```json
  {
    "oldPassword": "ancienMotDePasse",
    "newPassword": "nouveauMotDePasse"
  }
  ```
- **Réponse (200 OK)** : `"Mot de passe modifié avec succès"`

---

### 2. Configuration de Sécurité (SecurityConfig)

**Fichier** : `backend/src/main/java/ma/ree/sireleves/config/SecurityConfig.java`

**État actuel** : ⚠️ **PUBLIC - Tous les endpoints sont accessibles sans authentification**

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**").permitAll()
    .requestMatchers("/api/**").permitAll() // ⚠️ PROBLÈME : Tout est public !
    .anyRequest().authenticated()
)
```

**Problème identifié** :
- La ligne `.requestMatchers("/api/**").permitAll()` rend **TOUS** les endpoints API publics
- Cela signifie que n'importe qui peut accéder aux données sans authentification
- Le JWT filter existe mais n'est pas utilisé efficacement

**Solution nécessaire** :
- Retirer `.requestMatchers("/api/**").permitAll()`
- Garder seulement `/api/auth/**` en public
- Ajouter le `JwtAuthenticationFilter` dans la chaîne de sécurité
- Protéger les endpoints selon les rôles

---

### 3. Filtre JWT (JwtAuthenticationFilter)

**Fichier** : `backend/src/main/java/ma/ree/sireleves/security/JwtAuthenticationFilter.java`

**Fonctionnement** :
1. Intercepte toutes les requêtes HTTP
2. Vérifie la présence du header `Authorization: Bearer <token>`
3. Extrait et valide le token JWT
4. Extrait l'email, le rôle et l'ID utilisateur du token
5. Crée une authentification Spring Security
6. Place l'authentification dans le contexte de sécurité

**Token JWT contient** :
- `subject` : Email de l'utilisateur
- `role` : Rôle (Superadmin ou Utilisateur)
- `userId` : ID de l'utilisateur
- `expiration` : Date d'expiration (30 minutes par défaut)

---

### 4. Configuration JWT

**Fichier** : `backend/src/main/resources/application.properties`

```properties
jwt.secret=VotreCleSecreteSuperLongueEtComplexePourJWT2024
jwt.expiration=1800000  # 30 minutes en millisecondes
```

**En production (Docker)** :
- `jwt.secret` : Variable d'environnement `JWT_SECRET`
- `jwt.expiration` : Variable d'environnement `JWT_EXPIRATION` (défaut: 1800000)

---

### 5. Rôles Utilisateurs

**Fichier** : `backend/src/main/java/ma/ree/sireleves/entity/UtilisateurBackoffice.java`

**Rôles disponibles** :
- `Superadmin` : Administrateur avec tous les droits
- `Utilisateur` : Utilisateur standard avec droits limités

**Note** : ✅ **CORRIGÉ** - Le backend différencie maintenant les permissions selon les rôles :
- **Superadmin** : Accès complet, peut gérer les utilisateurs (créer, modifier, réinitialiser mot de passe)
- **Utilisateur** : Accès aux fonctionnalités métier (compteurs, agents, quartiers, relevés, clients, adresses)
- Les endpoints `/api/utilisateurs/**` sont protégés et accessibles uniquement aux Superadmin via `@PreAuthorize("hasRole('Superadmin')")`
- Le changement de mot de passe est sécurisé : un utilisateur ne peut modifier que son propre mot de passe

---

## 🌐 Architecture Frontend (À Implémenter)

### 1. Appels API dans Docker

**Configuration Nginx** : `frontend/nginx.conf`

```nginx
location /api/ {
    proxy_pass http://backend:8080/api/;
    # Le frontend appelle /api/ qui est proxifié vers backend:8080/api/
}
```

**Dans le code frontend** :
- URL de base : `/api` (URL relative)
- Nginx proxy automatiquement vers `http://backend:8080/api/`
- Pas besoin de spécifier l'URL complète du backend

**Fichier** : `frontend/src/services/api.js`

**État actuel** :
- ✅ Instance Axios créée avec baseURL `/api`
- ⚠️ Intercepteur pour token JWT commenté (non implémenté)
- ⚠️ Pas de gestion des erreurs 401 (token expiré)

---

### 2. Ce qui manque dans le Frontend

#### ❌ Service d'Authentification
- Pas de service pour appeler `/api/auth/login`
- Pas de gestion du token JWT
- Pas de stockage du token (localStorage/sessionStorage)

#### ❌ Pages d'Authentification
- Pas de page de login
- Pas de page d'inscription (si nécessaire)
- Pas de page de changement de mot de passe

#### ❌ Gestion d'État d'Authentification
- Pas de contexte/store pour l'état d'authentification
- Pas de vérification si l'utilisateur est connecté
- Pas de gestion du rôle utilisateur

#### ❌ Routes Protégées
- Toutes les routes sont publiques
- Pas de redirection vers login si non authentifié
- Pas de protection selon le rôle

#### ❌ Intercepteur Axios
- Token JWT non ajouté aux requêtes
- Pas de gestion de l'expiration du token
- Pas de refresh automatique

---

## 🔧 Implémentation Nécessaire

### 1. Service d'Authentification

**Fichier à créer** : `frontend/src/services/authService.js`

```javascript
import api from './api';

export const authService = {
  login: async (email, password) => {
    const response = await api.post('/auth/login', { email, password });
    return response.data;
  },
  
  changePassword: async (userId, oldPassword, newPassword) => {
    const response = await api.post(
      `/auth/change-password/${userId}`,
      { oldPassword, newPassword }
    );
    return response.data;
  },
  
  logout: () => {
    // Nettoyer le token et les données utilisateur
  }
};
```

---

### 2. Store/Context d'Authentification

**Option A : Zustand Store** (recommandé, déjà dans les dépendances)

**Fichier à créer** : `frontend/src/store/authStore.js`

```javascript
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useAuthStore = create(
  persist(
    (set) => ({
      token: null,
      user: null,
      isAuthenticated: false,
      
      login: (token, user) => set({ 
        token, 
        user, 
        isAuthenticated: true 
      }),
      
      logout: () => set({ 
        token: null, 
        user: null, 
        isAuthenticated: false 
      }),
    }),
    { name: 'auth-storage' }
  )
);
```

**Option B : React Context**

---

### 3. Intercepteur Axios pour JWT

**Fichier à modifier** : `frontend/src/services/api.js`

```javascript
// Ajouter le token à toutes les requêtes
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token'); // ou depuis le store
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Gérer les erreurs 401 (token expiré)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Rediriger vers login
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

---

### 4. Page de Login

**Fichier à créer** : `frontend/src/pages/LoginPage.jsx`

**Fonctionnalités** :
- Formulaire email/password
- Validation avec react-hook-form
- Appel à `authService.login()`
- Stockage du token et des données utilisateur
- Redirection vers la page d'accueil après login
- Gestion des erreurs (email/password incorrect, compte désactivé)
- Si `premiereConnexion === true`, rediriger vers changement de mot de passe

---

### 5. Page de Changement de Mot de Passe

**Fichier à créer** : `frontend/src/pages/ChangePasswordPage.jsx`

**Fonctionnalités** :
- Formulaire ancien mot de passe / nouveau mot de passe
- Validation (nouveau mot de passe doit être différent)
- Appel à `authService.changePassword()`
- Redirection après succès

---

### 6. Routes Protégées

**Fichier à modifier** : `frontend/src/App.jsx`

```javascript
import { ProtectedRoute } from './components/ProtectedRoute';

<Route path="/login" element={<LoginPage />} />
<Route path="/change-password" element={<ChangePasswordPage />} />

<Route element={<ProtectedRoute />}>
  <Route path="/" element={<Layout><Navigate to="/utilisateurs" /></Layout>} />
  <Route path="/utilisateurs" element={<Layout><UtilisateursPage /></Layout>} />
  {/* ... autres routes protégées ... */}
</Route>
```

**Fichier à créer** : `frontend/src/components/ProtectedRoute.jsx`

```javascript
import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

export function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuthStore();
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
}
```

---

### 7. Protection par Rôle (Optionnel)

**Fichier à créer** : `frontend/src/components/RoleProtectedRoute.jsx`

```javascript
import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

export function RoleProtectedRoute({ children, allowedRoles }) {
  const { user, isAuthenticated } = useAuthStore();
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  if (!allowedRoles.includes(user.role)) {
    return <Navigate to="/unauthorized" replace />;
  }
  
  return children;
}
```

**Utilisation** :
```javascript
<Route 
  path="/utilisateurs" 
  element={
    <RoleProtectedRoute allowedRoles={['Superadmin']}>
      <Layout><UtilisateursPage /></Layout>
    </RoleProtectedRoute>
  } 
/>
```

---

### 8. Header avec Informations Utilisateur

**Fichier à modifier** : `frontend/src/components/Layout.jsx`

**Ajouter** :
- Nom et prénom de l'utilisateur connecté
- Rôle de l'utilisateur
- Bouton de déconnexion
- Menu déroulant (optionnel)

---

## 🔒 Configuration de Sécurité Backend (À Corriger)

### Problème Actuel

Le `SecurityConfig` permet l'accès public à tous les endpoints :

```java
.requestMatchers("/api/**").permitAll() // ⚠️ DANGEREUX
```

### Solution

**Fichier à modifier** : `backend/src/main/java/ma/ree/sireleves/config/SecurityConfig.java`

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll() // Login public
            .requestMatchers("/actuator/**").permitAll() // Health checks
            .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll() // Swagger
            .requestMatchers("/api/**").authenticated() // ✅ Tous les autres endpoints nécessitent auth
            .anyRequest().authenticated()
        );
    
    return http.build();
}
```

**Important** : Ajouter le `JwtAuthenticationFilter` dans la chaîne :

```java
private final JwtAuthenticationFilter jwtAuthenticationFilter;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    // ...
}
```

---

## 📝 Résumé des Actions Nécessaires

### Backend (Corrections)
- ✅ [ ] Retirer `.requestMatchers("/api/**").permitAll()` de `SecurityConfig`
- ✅ [ ] Ajouter `JwtAuthenticationFilter` dans la chaîne de sécurité
- ✅ [ ] Tester que les endpoints sont bien protégés

### Frontend (Nouveau)
- ✅ [ ] Créer `authService.js` pour les appels API d'authentification
- ✅ [ ] Créer `authStore.js` (Zustand) pour gérer l'état d'authentification
- ✅ [ ] Modifier `api.js` pour ajouter le token JWT aux requêtes
- ✅ [ ] Créer `LoginPage.jsx` avec formulaire de connexion
- ✅ [ ] Créer `ChangePasswordPage.jsx` pour le changement de mot de passe
- ✅ [ ] Créer `ProtectedRoute.jsx` pour protéger les routes
- ✅ [ ] Créer `RoleProtectedRoute.jsx` pour la protection par rôle (optionnel)
- ✅ [ ] Modifier `App.jsx` pour ajouter les routes de login et protection
- ✅ [ ] Modifier `Layout.jsx` pour afficher les infos utilisateur et bouton logout
- ✅ [ ] Gérer la redirection après login selon `premiereConnexion`

---

## 🧪 Tests à Effectuer

1. **Test de Login** :
   - Login avec email/password correct → doit retourner token
   - Login avec email/password incorrect → doit afficher erreur
   - Login avec compte désactivé → doit afficher erreur

2. **Test de Protection** :
   - Accès à `/utilisateurs` sans token → doit rediriger vers `/login`
   - Accès à `/utilisateurs` avec token valide → doit afficher la page
   - Accès à `/utilisateurs` avec token expiré → doit rediriger vers `/login`

3. **Test de Rôles** :
   - Utilisateur avec rôle `Utilisateur` accède à route `Superadmin` → doit rediriger vers `/unauthorized`

4. **Test de Changement de Mot de Passe** :
   - Premier login avec `premiereConnexion: true` → doit rediriger vers `/change-password`
   - Changement de mot de passe réussi → doit permettre l'accès normal

---

## 📚 Ressources

- **Backend Auth Controller** : `backend/src/main/java/ma/ree/sireleves/controller/AuthController.java`
- **Backend Auth Service** : `backend/src/main/java/ma/ree/sireleves/service/AuthService.java`
- **Backend Security Config** : `backend/src/main/java/ma/ree/sireleves/config/SecurityConfig.java`
- **Backend JWT Filter** : `backend/src/main/java/ma/ree/sireleves/security/JwtAuthenticationFilter.java`
- **Backend JWT Util** : `backend/src/main/java/ma/ree/sireleves/util/JwtUtil.java`
- **Frontend API Service** : `frontend/src/services/api.js`

---

**Date de création** : 2024
**Dernière mise à jour** : 2024

