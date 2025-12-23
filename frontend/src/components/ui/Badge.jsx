/**
 * Badge Component - Design System Moderne
 * Variantes: success, warning, error, info, neutral
 */
function Badge({ 
  children, 
  variant = 'neutral',
  size = 'md',
  className = '',
  ...props 
}) {
  const baseStyles = 'inline-flex items-center font-medium rounded-full';
  
  const variants = {
    success: 'bg-green-100 text-green-800',
    warning: 'bg-amber-100 text-amber-800',
    error: 'bg-red-100 text-red-800',
    info: 'bg-blue-100 text-blue-800',
    neutral: 'bg-gray-100 text-gray-800',
  };
  
  const sizes = {
    sm: 'px-2 py-0.5 text-xs',
    md: 'px-2.5 py-0.5 text-xs',
    lg: 'px-3 py-1 text-sm',
  };
  
  const classes = `${baseStyles} ${variants[variant]} ${sizes[size]} ${className}`;
  
  return (
    <span className={classes} {...props}>
      {children}
    </span>
  );
}

export default Badge;

