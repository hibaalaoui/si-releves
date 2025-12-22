import axios from 'axios';

// URL de base de l'API backend
// Utilise une URL relative car Nginx proxy déjà /api vers le backend
const BASE_URL = '/api';

// Créer une instance Axios configurée
const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 secondes
});

// Intercepteur pour les requêtes - Ajouter le token JWT automatiquement
api.interceptors.request.use(
  (config) => {
    // Récupérer le token depuis localStorage (stocké par Zustand persist)
    try {
      const authStorage = localStorage.getItem('auth-storage');
      if (authStorage) {
        const { state } = JSON.parse(authStorage);
        const token = state?.token;
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
      }
    } catch (error) {
      console.error('Erreur lors de la récupération du token:', error);
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Intercepteur pour les réponses - Gérer les erreurs et l'expiration du token
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Gestion globale des erreurs
    if (error.response) {
      // Le serveur a répondu avec un code d'erreur
      const status = error.response.status;
      
      // Si 401 (Unauthorized), le token est invalide ou expiré
      if (status === 401) {
        // Nettoyer le storage d'authentification
        localStorage.removeItem('auth-storage');
        
        // Rediriger vers login seulement si on n'est pas déjà sur la page de login
        if (window.location.pathname !== '/login') {
          window.location.href = '/login';
        }
      }
      
      console.error('Erreur API:', error.response.data);
    } else if (error.request) {
      // La requête a été envoyée mais pas de réponse
      console.error('Pas de réponse du serveur');
    } else {
      // Erreur lors de la configuration de la requête
      console.error('Erreur:', error.message);
    }
    return Promise.reject(error);
  }
);

export default api;