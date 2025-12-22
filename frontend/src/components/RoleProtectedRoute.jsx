import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

/**
 * Composant pour protéger les routes selon le rôle de l'utilisateur
 * @param {Object} props
 * @param {React.ReactNode} props.children - Composants enfants à rendre
 * @param {string[]} props.allowedRoles - Liste des rôles autorisés (ex: ['Superadmin'])
 */
export function RoleProtectedRoute({ children, allowedRoles = [] }) {
  const { token, user } = useAuthStore();
  const isAuthenticated = !!(token && user);

  // Si non authentifié, rediriger vers login
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // Si aucun rôle spécifié, autoriser tous les utilisateurs authentifiés
  if (allowedRoles.length === 0) {
    return children;
  }

  // Vérifier si le rôle de l'utilisateur est autorisé
  if (!user || !user.role || !allowedRoles.includes(user.role)) {
    console.log('[RoleProtectedRoute] Accès refusé - Utilisateur:', user?.role, 'Rôles autorisés:', allowedRoles);
    // Rediriger vers une page non autorisée ou la page d'accueil
    return <Navigate to="/" replace />;
  }

  console.log('[RoleProtectedRoute] Accès autorisé - Utilisateur:', user?.role, 'Rôles autorisés:', allowedRoles);
  return children;
}

