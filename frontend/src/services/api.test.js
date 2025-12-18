import { describe, it, expect } from 'vitest';
import api from './api';

describe('api', () => {
  it('devrait avoir la bonne URL de base', () => {
    expect(api.defaults.baseURL).toBe('/api');
  });

  it('devrait avoir les bons headers par défaut', () => {
    expect(api.defaults.headers['Content-Type']).toBe('application/json');
  });

  it('devrait avoir un timeout configuré', () => {
    expect(api.defaults.timeout).toBe(10000);
  });

  it('devrait être une instance axios', () => {
    expect(api).toBeDefined();
    expect(typeof api.get).toBe('function');
    expect(typeof api.post).toBe('function');
    expect(typeof api.put).toBe('function');
    expect(typeof api.patch).toBe('function');
    expect(typeof api.delete).toBe('function');
  });
});
