import api from './api';

export const compteurService = {
  getAll: async (params = {}) => {
    const response = await api.get('/compteurs', { params });
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/compteurs/${id}`);
    return response.data;
  },

  create: async (data) => {
    const response = await api.post('/compteurs', data);
    return response.data;
  },
};