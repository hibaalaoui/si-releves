import { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { authStore } from '../store/authStore';

/**
 * Hook to handle auto-logout after 10 minutes of inactivity
 */
const useInactivityLogout = () => {
  const navigate = useNavigate();
  const timeoutRef = useRef(null);
  const INACTIVITY_TIMEOUT = 10 * 60 * 1000; // 10 minutes in milliseconds

  useEffect(() => {
    const resetTimer = () => {
      // Clear existing timeout
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }

      // Set new timeout
      timeoutRef.current = setTimeout(() => {
        const { isAuthenticated, logout } = authStore.getState();
        if (isAuthenticated) {
          logout();
          navigate('/login', { replace: true });
        }
      }, INACTIVITY_TIMEOUT);
    };

    // Events that indicate user activity
    const events = ['mousedown', 'mousemove', 'keypress', 'scroll', 'touchstart', 'click'];
    
    // Initial timer setup
    resetTimer();

    // Add event listeners
    events.forEach((event) => {
      window.addEventListener(event, resetTimer);
    });

    // Cleanup
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
      events.forEach((event) => {
        window.removeEventListener(event, resetTimer);
      });
    };
  }, [navigate]);
};

export default useInactivityLogout;

