import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { jwtDecode } from 'jwt-decode';

export const authStore = create(
  persist(
    (set, get) => ({
      token: null,
      user: null,
      isAuthenticated: false,

      /**
       * Login with token and user info from LoginResponse
       * @param {string} token - JWT token
       * @param {Object} userInfo - User info from LoginResponse (id, email, nom, prenom, role, premiereConnexion)
       */
      login: (token, userInfo = null) => {
        try {
          const decoded = jwtDecode(token);
          
          // Use userInfo from response if provided, otherwise use decoded token claims
          const user = userInfo || {
            id: decoded.id,
            email: decoded.sub || decoded.email,
            nom: decoded.nom,
            prenom: decoded.prenom,
            role: decoded.role,
            premiereConnexion: decoded.premiereConnexion,
          };

          set({
            token,
            user,
            isAuthenticated: true,
          });
        } catch (error) {
          console.error('Error decoding token:', error);
          set({
            token: null,
            user: null,
            isAuthenticated: false,
          });
        }
      },

      logout: () => {
        set({
          token: null,
          user: null,
          isAuthenticated: false,
        });
      },

      checkTokenExpiry: () => {
        const { token } = get();
        if (!token) {
          return false;
        }

        try {
          const decoded = jwtDecode(token);
          const currentTime = Date.now() / 1000;
          
          if (decoded.exp < currentTime) {
            // Token expired
            get().logout();
            return false;
          }
          return true;
        } catch (error) {
          console.error('Error checking token expiry:', error);
          get().logout();
          return false;
        }
      },
    }),
    {
      name: 'auth-storage',
    }
  )
);

