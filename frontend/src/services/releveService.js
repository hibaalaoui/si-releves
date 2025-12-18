import api from './api';

export const releveService = {
  getAll: async (params = {}) => {
    const response = await api.get('/releves', { params });
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/releves/${id}`);
    return response.data;
  },

  create: async (data) => {
    const response = await api.post('/releves', data);
    return response.data;
  },
};