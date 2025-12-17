import { format, parseISO } from 'date-fns';
import { DATE_FORMATS } from './constants';

/**
 * Format date for display
 */
export const formatDate = (date, formatStr = DATE_FORMATS.DISPLAY) => {
  if (!date) return '-';
  try {
    const dateObj = typeof date === 'string' ? parseISO(date) : date;
    return format(dateObj, formatStr);
  } catch (error) {
    console.error('Error formatting date:', error);
    return '-';
  }
};

/**
 * Format number with thousand separators
 */
export const formatNumber = (number, decimals = 2) => {
  if (number === null || number === undefined) return '-';
  return new Intl.NumberFormat('fr-FR', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  }).format(number);
};

/**
 * Capitalize first letter of string
 */
export const capitalize = (str) => {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
};

/**
 * Uppercase string
 */
export const toUpperCase = (str) => {
  if (!str) return '';
  return str.toUpperCase();
};

/**
 * Check if user has required role
 */
export const hasRole = (user, requiredRole) => {
  if (!user || !user.role) return false;
  return user.role === requiredRole;
};

/**
 * Check if user is Superadmin
 */
export const isSuperadmin = (user) => {
  return hasRole(user, 'Superadmin');
};

