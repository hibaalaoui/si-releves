/**
 * EmptyState Component - État vide professionnel
 */
function EmptyState({ 
  icon: Icon,
  title,
  description,
  action,
  className = ''
}) {
  return (
    <div className={`text-center py-16 ${className}`}>
      <div className="flex flex-col items-center justify-center">
        {Icon && (
          <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mb-4">
            <Icon className="text-gray-400 text-2xl" />
          </div>
        )}
        <h3 className="text-lg font-semibold text-gray-900 mb-1">{title}</h3>
        {description && (
          <p className="text-sm text-gray-500 mb-4 max-w-md">{description}</p>
        )}
        {action && <div className="mt-2">{action}</div>}
      </div>
    </div>
  );
}

export default EmptyState;

