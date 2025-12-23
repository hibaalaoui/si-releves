/**
 * Card Component - Design System Moderne
 * Variantes: default, elevated, bordered
 */
function Card({ 
  children, 
  variant = 'default',
  className = '',
  hover = false,
  ...props 
}) {
  const baseStyles = 'bg-white rounded-xl transition-all duration-200';
  
  const variants = {
    default: 'shadow-sm border border-gray-200',
    elevated: 'shadow-md border border-gray-200',
    bordered: 'border-2 border-gray-200',
  };
  
  const hoverStyles = hover ? 'hover:shadow-lg hover:-translate-y-0.5 cursor-pointer' : '';
  
  const classes = `${baseStyles} ${variants[variant]} ${hoverStyles} ${className}`;
  
  return (
    <div className={classes} {...props}>
      {children}
    </div>
  );
}

function CardHeader({ children, className = '', ...props }) {
  return (
    <div className={`px-6 py-4 border-b border-gray-200 ${className}`} {...props}>
      {children}
    </div>
  );
}

function CardBody({ children, className = '', ...props }) {
  return (
    <div className={`px-6 py-4 ${className}`} {...props}>
      {children}
    </div>
  );
}

function CardFooter({ children, className = '', ...props }) {
  return (
    <div className={`px-6 py-4 border-t border-gray-200 bg-gray-50 rounded-b-xl ${className}`} {...props}>
      {children}
    </div>
  );
}

Card.Header = CardHeader;
Card.Body = CardBody;
Card.Footer = CardFooter;

export default Card;


