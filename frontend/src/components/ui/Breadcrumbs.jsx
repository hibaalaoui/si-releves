import { Link } from 'react-router-dom';
import { FiChevronRight, FiHome } from 'react-icons/fi';

/**
 * Breadcrumbs Component - Navigation hiérarchique
 */
function Breadcrumbs({ items = [] }) {
  // Toujours commencer par Home
  const allItems = [
    { label: 'Accueil', path: '/dashboard', icon: FiHome },
    ...items
  ];

  return (
    <nav className="flex items-center space-x-2 text-sm" aria-label="Breadcrumb">
      {allItems.map((item, index) => {
        const isLast = index === allItems.length - 1;
        const Icon = item.icon;

        return (
          <div key={index} className="flex items-center">
            {index > 0 && (
              <FiChevronRight className="mx-2 text-gray-400" size={16} />
            )}
            {isLast ? (
              <span className="flex items-center text-gray-700 font-medium">
                {Icon && <Icon className="mr-1.5" size={16} />}
                {item.label}
              </span>
            ) : (
              <Link
                to={item.path}
                className="flex items-center text-gray-500 hover:text-primary-blue transition-colors"
              >
                {Icon && <Icon className="mr-1.5" size={16} />}
                {item.label}
              </Link>
            )}
          </div>
        );
      })}
    </nav>
  );
}

export default Breadcrumbs;

