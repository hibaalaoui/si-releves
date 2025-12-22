import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FiUsers, FiGrid, FiUser, FiClipboard, FiMapPin, FiLogOut, FiShield, FiSettings } from 'react-icons/fi';
import { useAuthStore } from '../store/authStore';

function Layout({ children }) {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout } = useAuthStore();

  // Tous les éléments de menu disponibles
  const allMenuItems = [
    { path: '/profil', label: 'Profil', icon: FiSettings, roles: ['Superadmin', 'Utilisateur'] },
    { path: '/utilisateurs', label: 'Utilisateurs', icon: FiUsers, roles: ['Superadmin'] }, // UNIQUEMENT Superadmin
    { path: '/quartiers', label: 'Quartiers', icon: FiMapPin, roles: ['Superadmin', 'Utilisateur'] },
    { path: '/agents', label: 'Agents', icon: FiUser, roles: ['Superadmin', 'Utilisateur'] },
    { path: '/compteurs', label: 'Compteurs', icon: FiGrid, roles: ['Superadmin', 'Utilisateur'] },
    { path: '/releves', label: 'Relevés', icon: FiClipboard, roles: ['Superadmin', 'Utilisateur'] },
  ];

  // Filtrer les éléments de menu selon le rôle de l'utilisateur
  const menuItems = allMenuItems.filter(item => {
    // Vérification stricte: utilisateur doit exister et avoir un rôle valide
    if (!user || !user.role) {
      return false;
    }
    
    // Normaliser le rôle de l'utilisateur (enlever espaces, convertir en string)
    const userRole = String(user.role).trim();
    
    // PROTECTION SPÉCIALE: Ne JAMAIS afficher "Utilisateurs" pour les utilisateurs normaux
    if (item.path === '/utilisateurs' && userRole !== 'Superadmin') {
      console.log('[Layout] BLOCAGE: Utilisateurs masqué pour rôle:', userRole);
      return false;
    }
    
    // Vérifier que le rôle de l'utilisateur est dans la liste des rôles autorisés
    const isAllowed = item.roles.some(role => String(role).trim() === userRole);
    
    return isAllowed;
  });

  const isActive = (path) => location.pathname.startsWith(path);

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}
      <header className="bg-blue-600 text-white shadow-md">
        <div className="px-6 py-4 flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold">SI Relevés - REE</h1>
            <p className="text-sm text-blue-100">Gestion des relevés de compteurs</p>
          </div>
          
          {/* Informations utilisateur et bouton de déconnexion */}
          {user && (
            <div className="flex items-center gap-4">
              <div className="text-right">
                <div className="flex items-center gap-2">
                  <span className="font-medium">
                    {user.prenom} {user.nom}
                  </span>
                  <span className="inline-flex items-center gap-1 px-2 py-1 bg-blue-700 rounded text-xs">
                    <FiShield className="text-xs" />
                    {user.role}
                  </span>
                </div>
                <p className="text-xs text-blue-200">{user.email}</p>
              </div>
              <button
                onClick={handleLogout}
                className="flex items-center gap-2 px-4 py-2 bg-blue-700 hover:bg-blue-800 rounded-md transition-colors text-sm font-medium"
                title="Déconnexion"
              >
                <FiLogOut />
                <span>Déconnexion</span>
              </button>
            </div>
          )}
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