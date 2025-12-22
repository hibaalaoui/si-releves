import { Navigate, useLocation } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { useEffect, useState } from 'react';

/**
 * Fonction ultra-robuste pour vérifier l'authentification directement dans localStorage
 * Source de vérité absolue pour éviter les problèmes de timing avec Zustand persist
 */
function isAuthenticatedInStorage() {
  try {
    const authStorage = localStorage.getItem('auth-storage');
    if (!authStorage) {
      console.log('[AuthCheck] localStorage vide');
      return false;
    }

    console.log('[AuthCheck] localStorage existe:', authStorage.substring(0, 100) + '...');

    const parsed = JSON.parse(authStorage);
    if (!parsed || typeof parsed !== 'object') {
      console.log('[AuthCheck] Parsing échoué - pas un objet');
      return false;
    }

    const state = parsed.state;
    if (!state || typeof state !== 'object') {
      console.log('[AuthCheck] Pas de propriété state ou state invalide');
      return false;
    }

    const { token, user } = state;

    // Vérifications strictes
    if (!token || typeof token !== 'string' || token.trim() === '') {
      console.log('[AuthCheck] Token invalide:', { token: token ? 'exists' : 'null', type: typeof token });
      return false;
    }

    if (!user || typeof user !== 'object' || !user.idUtilisateur) {
      console.log('[AuthCheck] User invalide:', { user: user ? 'exists' : 'null', hasId: user?.idUtilisateur ? 'yes' : 'no' });
      return false;
    }

    console.log('[AuthCheck] Authentification validée avec succès');
    return true;

  } catch (error) {
    console.error('[AuthCheck] Erreur lors de la vérification:', error);
    return false;
  }
}

/**
 * Composant amélioré pour protéger les routes nécessitant une authentification
 * Utilise une vérification synchrone et fiable pour éviter les problèmes de timing
 */
export function ProtectedRoute({ children }) {
  const location = useLocation();
  const { token, user } = useAuthStore();
  const [authChecked, setAuthChecked] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Vérification d'authentification au montage du composant
  useEffect(() => {
    const storageAuth = isAuthenticatedInStorage();
    const storeAuth = !!(token && user && typeof token === 'string' && token.trim() !== '' && user.idUtilisateur);

    const finalAuth = storageAuth || storeAuth;

    console.log('[ProtectedRoute] Vérification finale:', {
      storageAuth,
      storeAuth,
      finalAuth,
      storeToken: token ? 'exists' : 'null',
      storeUser: user ? 'exists' : 'null'
    });

    setIsAuthenticated(finalAuth);
    setAuthChecked(true);
  }, [token, user]); // Re-vérifier si le store change

  // Debug complet
  useEffect(() => {
    if (process.env.NODE_ENV === 'development') {
      console.log('[ProtectedRoute] État actuel:', {
        authChecked,
        isAuthenticated,
        pathname: location.pathname,
        localStorageSize: localStorage.getItem('auth-storage')?.length || 0
      });
    }
  }, [authChecked, isAuthenticated, location.pathname]);

  // Pendant la vérification initiale, ne rien afficher (évite le flash)
  if (!authChecked) {
    console.log('[ProtectedRoute] Vérification en cours...');
    return null;
  }

  // Si non authentifié, rediriger immédiatement vers login
  if (!isAuthenticated) {
    console.log('[ProtectedRoute] Redirection vers /login depuis:', location.pathname);
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Si authentifié, afficher le contenu protégé
  console.log('[ProtectedRoute] Accès autorisé à:', location.pathname);
  return children;
}

