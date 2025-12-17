import api from './api';

export const quartierService = {
  getAll: async () => {
    const response = await api.get('/quartiers');
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/quartiers/${id}`);
    return response.data;
  },

  create: async (data) => {
    const response = await api.post('/quartiers', data);
    return response.data;
  },

  update: async (id, data) => {
    const response = await api.put(`/quartiers/${id}`, data);
    return response.data;
  },

  delete: async (id) => {
    const response = await api.delete(`/quartiers/${id}`);
    return response.data;
  },
};