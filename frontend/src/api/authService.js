import api from './axios';

/**
 * Authentication service for API calls
 */
export const authService = {
  /**
   * Login user with email and password
   * @param {string} email - User email
   * @param {string} password - User password
   * @returns {Promise<Object>} LoginResponse with token and user info
   */
  login: async (email, password) => {
    const response = await api.post('/api/auth/login', {
      email,
      password,
    });
    return response.data;
  },

  /**
   * Logout user (client-side only, server doesn't need to be called for JWT)
   */
  logout: () => {
    // JWT is stateless, so no server call needed
    // Just clear local storage via authStore
  },
};

