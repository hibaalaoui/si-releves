import { forwardRef } from 'react';

/**
 * Input Component - Design System Moderne
 * Supporte: text, email, password, number, etc.
 * Features: icônes, erreurs, helper text
 */
const Input = forwardRef(({
  label,
  error,
  helperText,
  leftIcon: LeftIcon,
  rightIcon: RightIcon,
  type = 'text',
  className = '',
  ...props
}, ref) => {
  const baseStyles = 'w-full h-11 px-4 border-[1.5px] rounded-lg transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-1 disabled:opacity-50 disabled:cursor-not-allowed';
  
  const normalStyles = 'border-gray-300 focus:border-primary-blue focus:ring-primary-blue/20';
  const errorStyles = 'border-error focus:border-error focus:ring-error/20';
  
  const inputStyles = error ? errorStyles : normalStyles;
  
  const paddingLeft = LeftIcon ? 'pl-10' : 'pl-4';
  const paddingRight = RightIcon ? 'pr-10' : 'pr-4';
  
  const classes = `${baseStyles} ${inputStyles} ${paddingLeft} ${paddingRight} ${className}`;
  
  return (
    <div className="w-full">
      {label && (
        <label className="block text-sm font-medium text-gray-700 mb-2">
          {label}
        </label>
      )}
      <div className="relative">
        {LeftIcon && (
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <LeftIcon className="text-gray-400" size={20} />
          </div>
        )}
        <input
          ref={ref}
          type={type}
          className={classes}
          {...props}
        />
        {RightIcon && (
          <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
            <RightIcon className="text-gray-400" size={20} />
          </div>
        )}
      </div>
      {error && (
        <p className="mt-1.5 text-sm text-error">{error}</p>
      )}
      {helperText && !error && (
        <p className="mt-1.5 text-sm text-gray-500">{helperText}</p>
      )}
    </div>
  );
});

Input.displayName = 'Input';

export default Input;

