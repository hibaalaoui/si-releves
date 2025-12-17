import { Link, useLocation } from 'react-router-dom';
import { FiUsers, FiGrid, FiUser, FiClipboard, FiMapPin } from 'react-icons/fi';

function Layout({ children }) {
  const location = useLocation();

  const menuItems = [
    { path: '/utilisateurs', label: 'Utilisateurs', icon: FiUsers },
    { path: '/quartiers', label: 'Quartiers', icon: FiMapPin },
    { path: '/agents', label: 'Agents', icon: FiUser },
    { path: '/compteurs', label: 'Compteurs', icon: FiGrid },
    { path: '/releves', label: 'Relevés', icon: FiClipboard },
  ];

  const isActive = (path) => location.pathname.startsWith(path);

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}
      <header className="bg-blue-600 text-white shadow-md">
        <div className="px-6 py-4">
          <h1 className="text-2xl font-bold">SI Relevés - REE</h1>
          <p className="text-sm text-blue-100">Gestion des relevés de compteurs</p>
        </div>
      </header>

      <div className="flex">
        {/* Sidebar */}
        <aside className="w-64 bg-white shadow-md min-h-screen">
          <nav className="py-6">
            {menuItems.map((item) => {
              const Icon = item.icon;
              return (
                <Link
                  key={item.path}
                  to={item.path}
                  className={`flex items-center px-6 py-3 text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition-colors ${
                    isActive(item.path) ? 'bg-blue-50 text-blue-600 border-r-4 border-blue-600' : ''
                  }`}
                >
                  <Icon className="mr-3 text-xl" />
                  <span className="font-medium">{item.label}</span>
                </Link>
              );
            })}
          </nav>
        </aside>

        {/* Main Content */}
        <main className="flex-1 p-6">
          {children}
        </main>
      </div>
    </div>
  );
}

export default Layout;