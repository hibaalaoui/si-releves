import { describe, it, expect, vi, beforeEach } from 'vitest';
import { compteurService } from './compteurService';
import api from './api';

vi.mock('./api');

describe('compteurService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getAll', () => {
    it('devrait récupérer tous les compteurs', async () => {
      const mockCompteurs = [{ idCompteur: 'CMP001', type: 'Eau' }];
      api.get.mockResolvedValue({ data: mockCompteurs });

      const result = await compteurService.getAll();

      expect(result).toEqual(mockCompteurs);
      expect(api.get).toHaveBeenCalledWith('/compteurs', { params: {} });
    });

    it('devrait récupérer les compteurs avec des paramètres', async () => {
      const params = { idQuartier: 1 };
      const mockCompteurs = [{ idCompteur: 'CMP001' }];
      api.get.mockResolvedValue({ data: mockCompteurs });

      const result = await compteurService.getAll(params);

      expect(result).toEqual(mockCompteurs);
      expect(api.get).toHaveBeenCalledWith('/compteurs', { params });
    });
  });

  describe('getById', () => {
    it('devrait récupérer un compteur par son ID', async () => {
      const mockCompteur = { idCompteur: 'CMP001', type: 'Eau' };
      api.get.mockResolvedValue({ data: mockCompteur });

      const result = await compteurService.getById('CMP001');

      expect(result).toEqual(mockCompteur);
      expect(api.get).toHaveBeenCalledWith('/compteurs/CMP001');
    });
  });

  describe('create', () => {
    it('devrait créer un compteur', async () => {
      const compteurData = { idAdresse: 1, type: 'Eau' };
      const mockResponse = { idCompteur: 'CMP001', type: 'Eau' };
      api.post.mockResolvedValue({ data: mockResponse });

      const result = await compteurService.create(compteurData);

      expect(result).toEqual(mockResponse);
      expect(api.post).toHaveBeenCalledWith('/compteurs', compteurData);
    });
  });
});
