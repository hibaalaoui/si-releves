import api from './api';

/**
 * Service d'authentification
 * Gère les appels API pour l'authentification (login, changement de mot de passe)
 */
export const authService = {
  /**
   * Connexion d'un utilisateur
   * @param {string} email - Email de l'utilisateur
   * @param {string} password - Mot de passe
   * @returns {Promise<Object>} Réponse contenant le token et les données utilisateur
   */
  login: async (email, password) => {
    const response = await api.post('/auth/login', { email, password });
    return response.data;
  },

  /**
   * Changer le mot de passe d'un utilisateur
   * @param {number} userId - ID de l'utilisateur
   * @param {string} oldPassword - Ancien mot de passe
   * @param {string} newPassword - Nouveau mot de passe
   * @returns {Promise<string>} Message de succès
   */
  changePassword: async (userId, oldPassword, newPassword) => {
    const response = await api.post(
      `/auth/change-password/${userId}`,
      { oldPassword, newPassword }
    );
    return response.data;
  },
};

