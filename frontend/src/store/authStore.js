import { create } from 'zustand';
import { persist } from 'zustand/middleware';

/**
 * Store Zustand pour gérer l'état d'authentification
 * Utilise le middleware persist pour sauvegarder dans localStorage
 */
export const useAuthStore = create(
  persist(
    (set) => ({
      // État initial
      token: null,
      user: null,

      /**
       * Connecter un utilisateur
       * @param {string} token - Token JWT
       * @param {Object} user - Données utilisateur (idUtilisateur, nom, prenom, email, role, premiereConnexion)
       */
      login: (token, user) => {
        set({
          token,
          user,
        });
      },

      /**
       * Déconnecter l'utilisateur
       */
      logout: () => {
        set({
          token: null,
          user: null,
        });
      },

      /**
       * Mettre à jour les données utilisateur
       * @param {Object} user - Nouvelles données utilisateur
       */
      updateUser: (user) => {
        set((state) => ({
          user: { ...state.user, ...user },
        }));
      },
    }),
    {
      name: 'auth-storage', // Nom de la clé dans localStorage
      // Ne persister que le token et les données utilisateur essentielles
      partialize: (state) => ({
        token: state.token,
        user: state.user,
      }),
    }
  )
);

/**
 * Hook personnalisé pour vérifier si l'utilisateur est authentifié
 */
export const useIsAuthenticated = () => {
  const { token, user } = useAuthStore();
  return !!(token && user);
};

