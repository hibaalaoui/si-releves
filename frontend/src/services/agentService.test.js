import { describe, it, expect, vi, beforeEach } from 'vitest';
import { agentService } from './agentService';
import api from './api';

vi.mock('./api');

describe('agentService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getAll', () => {
    it('devrait récupérer tous les agents', async () => {
      const mockAgents = [{ idAgent: 'AGT001', nom: 'Dupont' }];
      api.get.mockResolvedValue({ data: mockAgents });

      const result = await agentService.getAll();

      expect(result).toEqual(mockAgents);
      expect(api.get).toHaveBeenCalledWith('/agents', { params: {} });
    });

    it('devrait récupérer les agents avec des paramètres', async () => {
      const params = { idQuartier: 1 };
      const mockAgents = [{ idAgent: 'AGT001' }];
      api.get.mockResolvedValue({ data: mockAgents });

      const result = await agentService.getAll(params);

      expect(result).toEqual(mockAgents);
      expect(api.get).toHaveBeenCalledWith('/agents', { params });
    });
  });

  describe('getById', () => {
    it('devrait récupérer un agent par son ID', async () => {
      const mockAgent = { idAgent: 'AGT001', nom: 'Dupont' };
      api.get.mockResolvedValue({ data: mockAgent });

      const result = await agentService.getById('AGT001');

      expect(result).toEqual(mockAgent);
      expect(api.get).toHaveBeenCalledWith('/agents/AGT001');
    });
  });

  describe('create', () => {
    it('devrait créer un agent', async () => {
      const agentData = { idAgent: 'AGT001', nom: 'Dupont' };
      const mockResponse = { idAgent: 'AGT001', nom: 'Dupont' };
      api.post.mockResolvedValue({ data: mockResponse });

      const result = await agentService.create(agentData);

      expect(result).toEqual(mockResponse);
      expect(api.post).toHaveBeenCalledWith('/agents', agentData);
    });
  });

  describe('update', () => {
    it('devrait mettre à jour un agent', async () => {
      const agentData = { nom: 'Martin' };
      const mockResponse = { idAgent: 'AGT001', nom: 'Martin' };
      api.put.mockResolvedValue({ data: mockResponse });

      const result = await agentService.update('AGT001', agentData);

      expect(result).toEqual(mockResponse);
      expect(api.put).toHaveBeenCalledWith('/agents/AGT001', agentData);
    });
  });

  describe('toggleStatus', () => {
    it('devrait basculer le statut d\'un agent', async () => {
      const mockResponse = { idAgent: 'AGT001', actif: false };
      api.patch.mockResolvedValue({ data: mockResponse });

      const result = await agentService.toggleStatus('AGT001');

      expect(result).toEqual(mockResponse);
      expect(api.patch).toHaveBeenCalledWith('/agents/AGT001/toggle-status');
    });
  });
});
