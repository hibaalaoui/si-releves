// API Endpoints
export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/api/auth/login',
    LOGOUT: '/api/auth/logout',
    CHANGE_PASSWORD: '/api/auth/change-password',
  },
  USERS: {
    BASE: '/api/users',
    BY_ID: (id) => `/api/users/${id}`,
  },
  COMPTEURS: {
    BASE: '/api/compteurs',
    BY_ID: (id) => `/api/compteurs/${id}`,
  },
  AGENTS: {
    BASE: '/api/agents',
    BY_ID: (id) => `/api/agents/${id}`,
  },
  RELEVES: {
    BASE: '/api/releves',
    BY_ID: (id) => `/api/releves/${id}`,
  },
  DASHBOARD: {
    KPIS: '/api/dashboard/kpis',
    STATS: '/api/dashboard/stats',
  },
};

// User Roles
export const ROLES = {
  SUPERADMIN: 'Superadmin',
  UTILISATEUR: 'Utilisateur',
};

// Compteur Types
export const COMPTEUR_TYPES = {
  EAU: 'Eau',
  ELECTRICITE: 'Electricite',
};

// Date Formats
export const DATE_FORMATS = {
  DISPLAY: 'dd/MM/yyyy',
  API: 'yyyy-MM-dd',
  DATETIME: 'dd/MM/yyyy HH:mm',
};

