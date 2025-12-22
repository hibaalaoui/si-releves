# 🚀 Guide d'Implémentation - Authentification Frontend

## 📋 Prompt pour Cursor AI

```
Je dois implémenter un système d'authentification complet dans mon application React frontend qui communique avec un backend Spring Boot via Docker.

CONTEXTE BACKEND:
- Endpoint de login: POST /api/auth/login
  Body: { email: string, password: string }
  Response: { token: string, type: "Bearer", idUtilisateur: number, nom: string, prenom: string, email: string, role: "Superadmin" | "Utilisateur", premiereConnexion: boolean }
  
- Endpoint de changement de mot de passe: POST /api/auth/change-password/{userId}
  Headers: Authorization: Bearer <token>
  Body: { oldPassword: string, newPassword: string }
  
- Le backend utilise JWT avec expiration de 30 minutes
- Les tokens doivent être envoyés dans le header Authorization: Bearer <token>
- Les endpoints API sont proxifiés via Nginx: /api → http://backend:8080/api/

STACK FRONTEND:
- React 19 avec Vite
- React Router 7 pour le routage
- Zustand 5 pour le state management (déjà installé)
- React Hook Form 7 pour les formulaires
- Axios pour les appels API
- TanStack Query 5 pour la gestion des données

BESOINS:
1. Créer un service d'authentification (authService.js) qui appelle les endpoints backend
2. Créer un store Zustand (authStore.js) pour gérer l'état d'authentification avec persistence
3. Modifier api.js pour ajouter automatiquement le token JWT dans les headers et gérer les erreurs 401
4. Créer une page de login (LoginPage.jsx) avec validation et gestion d'erreurs
5. Créer une page de changement de mot de passe (ChangePasswordPage.jsx)
6. Créer un composant ProtectedRoute pour protéger les routes
7. Créer un composant RoleProtectedRoute pour la protection par rôle
8. Modifier App.jsx pour intégrer les routes d'authentification
9. Modifier Layout.jsx pour afficher les infos utilisateur et un bouton de déconnexion
10. Gérer la redirection automatique vers /change-password si premiereConnexion === true

CONTRAINTES:
- Utiliser Tailwind CSS pour le styling (déjà configuré)
- Le design doit être moderne et cohérent avec le reste de l'application
- Gérer les erreurs de manière user-friendly
- Le token doit être stocké de manière sécurisée
- Gérer l'expiration du token (redirection vers login si 401)
- Si l'utilisateur est déjà connecté et essaie d'accéder à /login, rediriger vers la page d'accueil

IMPLÉMENTER TOUT LE CODE NÉCESSAIRE.
```

---

## 📁 Structure des Fichiers à Créer/Modifier

```
frontend/src/
├── services/
│   ├── api.js (MODIFIER - ajouter intercepteurs JWT)
│   └── authService.js (CRÉER - service d'authentification)
├── store/
│   └── authStore.js (CRÉER - store Zustand pour auth)
├── pages/
│   ├── LoginPage.jsx (CRÉER - page de connexion)
│   └── ChangePasswordPage.jsx (CRÉER - changement de mot de passe)
├── components/
│   ├── ProtectedRoute.jsx (CRÉER - protection des routes)
│   ├── RoleProtectedRoute.jsx (CRÉER - protection par rôle)
│   └── Layout.jsx (MODIFIER - ajouter infos utilisateur et logout)
└── App.jsx (MODIFIER - ajouter routes d'authentification)
```

---

## 🔧 Détails d'Implémentation

### 1. authService.js

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
  }
};
```

### 2. authStore.js

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
      
      logout: () => {
        set({ 
          token: null, 
          user: null, 
          isAuthenticated: false 
        });
        // Optionnel: nettoyer localStorage
      },
      
      updateUser: (user) => set({ user }),
    }),
    { 
      name: 'auth-storage',
      // Ne persister que le token et les données essentielles
      partialize: (state) => ({ 
        token: state.token, 
        user: state.user 
      })
    }
  )
);
```

**Note** : Si `persist` n'est pas disponible, installer :
```bash
npm install zustand
```

### 3. Modification de api.js

```javascript
import axios from 'axios';
import { useAuthStore } from '../store/authStore';

const BASE_URL = '/api';

const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Intercepteur pour ajouter le token JWT
api.interceptors.request.use(
  (config) => {
    // Récupérer le token depuis le store ou localStorage
    const token = localStorage.getItem('auth-storage') 
      ? JSON.parse(localStorage.getItem('auth-storage')).state?.token 
      : null;
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Intercepteur pour gérer les erreurs 401
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expiré ou invalide
      localStorage.removeItem('auth-storage');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
```

### 4. LoginPage.jsx

**Fonctionnalités clés** :
- Formulaire avec validation (email requis, password requis)
- Gestion des erreurs (affichage des messages d'erreur)
- Loading state pendant la requête
- Redirection après login réussi
- Si `premiereConnexion === true`, rediriger vers `/change-password`
- Si déjà connecté, rediriger vers `/`

### 5. ChangePasswordPage.jsx

**Fonctionnalités clés** :
- Formulaire avec validation
- Vérifier que nouveau mot de passe ≠ ancien
- Confirmation du nouveau mot de passe
- Après succès, mettre à jour `premiereConnexion` dans le store
- Rediriger vers la page d'accueil

### 6. ProtectedRoute.jsx

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

### 7. RoleProtectedRoute.jsx

```javascript
import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

export function RoleProtectedRoute({ children, allowedRoles = [] }) {
  const { user, isAuthenticated } = useAuthStore();
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  if (allowedRoles.length > 0 && !allowedRoles.includes(user?.role)) {
    return <Navigate to="/unauthorized" replace />;
  }
  
  return children;
}
```

### 8. Modification de App.jsx

```javascript
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ProtectedRoute } from './components/ProtectedRoute';
import LoginPage from './pages/LoginPage';
import ChangePasswordPage from './pages/ChangePasswordPage';
// ... autres imports

function App() {
  return (
    <Router>
      <Routes>
        {/* Routes publiques */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/change-password" element={<ChangePasswordPage />} />
        
        {/* Routes protégées */}
        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<Layout><Navigate to="/utilisateurs" replace /></Layout>} />
          <Route path="/utilisateurs" element={<Layout><UtilisateursPage /></Layout>} />
          {/* ... autres routes ... */}
        </Route>
        
        {/* 404 */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
}
```

### 9. Modification de Layout.jsx

Ajouter dans le header :
- Nom et prénom de l'utilisateur
- Rôle
- Bouton de déconnexion

---

## ✅ Checklist d'Implémentation

- [ ] Créer `authService.js`
- [ ] Créer `authStore.js` avec Zustand persist
- [ ] Modifier `api.js` pour ajouter token JWT et gérer 401
- [ ] Créer `LoginPage.jsx`
- [ ] Créer `ChangePasswordPage.jsx`
- [ ] Créer `ProtectedRoute.jsx`
- [ ] Créer `RoleProtectedRoute.jsx` (optionnel)
- [ ] Modifier `App.jsx` pour les routes
- [ ] Modifier `Layout.jsx` pour afficher utilisateur et logout
- [ ] Tester le flow complet
- [ ] Vérifier la gestion des erreurs
- [ ] Vérifier la redirection après login
- [ ] Vérifier la protection des routes

---

## 🧪 Tests Manuels

1. **Test de Login** :
   - Aller sur `/login`
   - Entrer email/password incorrect → doit afficher erreur
   - Entrer email/password correct → doit rediriger vers `/` ou `/change-password`

2. **Test de Protection** :
   - Se déconnecter
   - Essayer d'accéder à `/utilisateurs` → doit rediriger vers `/login`

3. **Test de Token** :
   - Se connecter
   - Ouvrir DevTools → Network
   - Faire une requête → vérifier que le header `Authorization: Bearer <token>` est présent

4. **Test d'Expiration** :
   - Se connecter
   - Modifier le token dans localStorage pour le rendre invalide
   - Faire une requête → doit rediriger vers `/login`

---

**Ce guide doit être utilisé avec Cursor AI pour générer tout le code nécessaire.**

