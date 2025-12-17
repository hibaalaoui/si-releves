import { Navigate, useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import { authStore } from '../store/authStore';
import useInactivityLogout from '../hooks/useInactivityLogout';

const ProtectedRoute = ({ children, requiredRole = null }) => {
  const location = useLocation();
  const isAuthenticated = authStore((state) => state.isAuthenticated);
  const user = authStore((state) => state.user);
  const checkTokenExpiry = authStore((state) => state.checkTokenExpiry);

  // Enable inactivity logout for protected routes
  useInactivityLogout();

  // Check token expiry on mount and route changes
  useEffect(() => {
    if (isAuthenticated && !checkTokenExpiry()) {
      authStore.getState().logout();
    }
  }, [location.pathname, isAuthenticated, checkTokenExpiry]);

  // Check if token is expired
  if (isAuthenticated && !checkTokenExpiry()) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  // Check if user is authenticated
  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  // Check if user has required role
  if (requiredRole && user?.role !== requiredRole) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
};

export default ProtectedRoute;

