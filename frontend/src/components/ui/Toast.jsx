import { useEffect } from 'react';
import { FiX, FiCheckCircle, FiAlertCircle, FiInfo, FiAlertTriangle } from 'react-icons/fi';

/**
 * Toast Component - Notifications toast professionnelles
 */
function Toast({ 
  id,
  type = 'info',
  title,
  message,
  duration = 5000,
  onClose 
}) {
  const icons = {
    success: FiCheckCircle,
    error: FiAlertCircle,
    warning: FiAlertTriangle,
    info: FiInfo,
  };

  const styles = {
    success: 'bg-green-50 border-green-500 text-green-800',
    error: 'bg-red-50 border-red-500 text-red-800',
    warning: 'bg-amber-50 border-amber-500 text-amber-800',
    info: 'bg-blue-50 border-blue-500 text-blue-800',
  };

  const Icon = icons[type];
  const styleClass = styles[type];

  useEffect(() => {
    if (duration > 0) {
      const timer = setTimeout(() => {
        onClose(id);
      }, duration);
      return () => clearTimeout(timer);
    }
  }, [duration, id, onClose]);

  return (
    <div
      className={`flex items-start gap-3 p-4 rounded-lg border-l-4 shadow-lg min-w-[320px] max-w-md animate-slideInRight ${styleClass}`}
      role="alert"
    >
      <Icon className="flex-shrink-0 text-xl mt-0.5" />
      <div className="flex-1 min-w-0">
        {title && (
          <div className="font-semibold text-sm mb-1">{title}</div>
        )}
        <div className="text-sm">{message}</div>
      </div>
      <button
        onClick={() => onClose(id)}
        className="flex-shrink-0 text-current opacity-60 hover:opacity-100 transition-opacity"
        aria-label="Fermer"
      >
        <FiX size={18} />
      </button>
    </div>
  );
}

/**
 * ToastContainer - Container pour afficher les toasts
 */
function ToastContainer({ toasts, onClose }) {
  if (toasts.length === 0) return null;

  return (
    <div className="fixed top-4 right-4 z-50 space-y-3 flex flex-col items-end">
      {toasts.map((toast) => (
        <Toast key={toast.id} {...toast} onClose={onClose} />
      ))}
    </div>
  );
}

export { Toast, ToastContainer };

