import { useState, useCallback } from 'react';

/**
 * Hook pour gérer les toasts
 */
export function useToast() {
  const [toasts, setToasts] = useState([]);

  const addToast = useCallback(({ type = 'info', title, message, duration = 5000 }) => {
    const id = Date.now() + Math.random();
    const newToast = { id, type, title, message, duration };
    setToasts((prev) => [...prev, newToast]);
    return id;
  }, []);

  const removeToast = useCallback((id) => {
    setToasts((prev) => prev.filter((toast) => toast.id !== id));
  }, []);

  const success = useCallback((message, title = 'Succès') => {
    return addToast({ type: 'success', title, message });
  }, [addToast]);

  const error = useCallback((message, title = 'Erreur') => {
    return addToast({ type: 'error', title, message });
  }, [addToast]);

  const warning = useCallback((message, title = 'Attention') => {
    return addToast({ type: 'warning', title, message });
  }, [addToast]);

  const info = useCallback((message, title = 'Information') => {
    return addToast({ type: 'info', title, message });
  }, [addToast]);

  return {
    toasts,
    addToast,
    removeToast,
    success,
    error,
    warning,
    info,
  };
}

