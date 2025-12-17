import api from './api';

// ========== UTILISATEURS ==========

export const utilisateurService = {
  // Récupérer tous les utilisateurs
  getAll: async (params = {}) => {
    const response = await api.get('/utilisateurs', { params });
    return response.data;
  },

  // Récupérer un utilisateur par ID
  getById: async (id) => {
    const response = await api.get(`/utilisateurs/${id}`);
    return response.data;
  },

  // Créer un utilisateur
  create: async (data) => {
    const response = await api.post('/utilisateurs', data);
    return response.data;
  },

  // Modifier un utilisateur
  update: async (id, data) => {
    const response = await api.put(`/utilisateurs/${id}`, data);
    return response.data;
  },

  // Réinitialiser le mot de passe
  resetPassword: async (id) => {
    const response = await api.post(`/utilisateurs/${id}/reset-password`);
    return response.data;
  },
};