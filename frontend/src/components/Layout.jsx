import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { 
  FiUsers, FiGrid, FiUser, FiClipboard, FiMapPin, FiLogOut, 
  FiShield, FiSettings, FiMenu, FiX, FiSearch, FiChevronDown,
  FiLayout
} from 'react-icons/fi';
import { useAuthStore } from '../store/authStore';
import Breadcrumbs from './ui/Breadcrumbs';
import { Badge } from './ui';

function Layout({ children }) {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout } = useAuthStore();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [profileDropdownOpen, setProfileDropdownOpen] = useState(false);

  // Tous les éléments de menu disponibles avec sections
  const menuSections = [
    {
      title: 'TABLEAU DE BORD',
      items: [
        { path: '/dashboard', label: 'Dashboard', icon: FiLayout, roles: ['Superadmin', 'Utilisateur'] },
      ]
    },
    {
      title: 'GESTION',
      items: [
        { path: '/releves', label: 'Relevés', icon: FiClipboard, roles: ['Superadmin', 'Utilisateur'] },
        { path: '/compteurs', label: 'Compteurs', icon: FiGrid, roles: ['Superadmin', 'Utilisateur'] },
        { path: '/agents', label: 'Agents', icon: FiUser, roles: ['Superadmin', 'Utilisateur'] },
        { path: '/quartiers', label: 'Quartiers', icon: FiMapPin, roles: ['Superadmin', 'Utilisateur'] },
      ]
    },
    {
      title: 'ADMINISTRATION',
      items: [
        { path: '/utilisateurs', label: 'Utilisateurs', icon: FiUsers, roles: ['Superadmin'] },
    { path: '/profil', label: 'Profil', icon: FiSettings, roles: ['Superadmin', 'Utilisateur'] },
      ]
    },
  ];

  // Filtrer les éléments de menu selon le rôle de l'utilisateur
  const filteredSections = menuSections.map(section => ({
    ...section,
    items: section.items.filter(item => {
      if (!user || !user.role) return false;
    const userRole = String(user.role).trim();
      if (item.path === '/utilisateurs' && userRole !== 'Superadmin') return false;
      return item.roles.some(role => String(role).trim() === userRole);
    })
  })).filter(section => section.items.length > 0);

  const isActive = (path) => location.pathname.startsWith(path);

  const handleLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  // Générer breadcrumbs basés sur la route
  const getBreadcrumbs = () => {
    const pathMap = {
      '/dashboard': 'Dashboard',
      '/releves': 'Relevés',
      '/compteurs': 'Compteurs',
      '/agents': 'Agents',
      '/quartiers': 'Quartiers',
      '/utilisateurs': 'Utilisateurs',
      '/profil': 'Profil',
    };
    
    const currentPath = location.pathname;
    const label = pathMap[currentPath] || 'Page';
    
    return [{ label, path: currentPath }];
  };

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar - Desktop */}
      <aside className="hidden lg:flex w-60 flex-col bg-gradient-to-b from-primary-blue to-primary-blue-dark text-white shadow-xl">
        {/* Logo */}
        <div className="px-6 py-6 border-b border-white/10">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-white/20 rounded-lg flex items-center justify-center">
              <FiShield className="text-white text-xl" />
            </div>
          <div>
              <h1 className="text-lg font-heading font-bold">SI Relevés</h1>
              <p className="text-xs text-white/80">Rabat Energie & Eau</p>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <nav className="flex-1 py-6 overflow-y-auto">
          {filteredSections.map((section, sectionIndex) => (
            <div key={sectionIndex} className="mb-6">
              <div className="px-6 mb-2">
                <h3 className="text-xs font-semibold text-white/60 uppercase tracking-wider">
                  {section.title}
                </h3>
              </div>
              {section.items.map((item) => {
                const Icon = item.icon;
                const active = isActive(item.path);
                return (
                  <Link
                    key={item.path}
                    to={item.path}
                    className={`flex items-center px-6 py-3 mx-2 rounded-lg transition-all duration-150 ${
                      active
                        ? 'bg-white/20 text-white border-l-[3px] border-white'
                        : 'text-white/80 hover:bg-white/10 hover:text-white'
                    }`}
                  >
                    <Icon className="mr-3 text-lg" />
                    <span className="font-medium text-sm">{item.label}</span>
                  </Link>
                );
              })}
            </div>
          ))}
        </nav>
      </aside>

      {/* Sidebar Mobile */}
      {sidebarOpen && (
        <>
          <div 
            className="fixed inset-0 bg-black/50 z-40 lg:hidden"
            onClick={() => setSidebarOpen(false)}
          />
          <aside className="fixed inset-y-0 left-0 w-60 z-50 lg:hidden flex flex-col bg-gradient-to-b from-primary-blue to-primary-blue-dark text-white shadow-2xl">
            <div className="px-6 py-4 flex items-center justify-between border-b border-white/10">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-white/20 rounded-lg flex items-center justify-center">
                  <FiShield className="text-white text-xl" />
                </div>
                <div>
                  <h1 className="text-lg font-heading font-bold">SI Relevés</h1>
                </div>
              </div>
              <button
                onClick={() => setSidebarOpen(false)}
                className="text-white/80 hover:text-white"
              >
                <FiX size={24} />
              </button>
            </div>
            <nav className="flex-1 py-6 overflow-y-auto">
              {filteredSections.map((section, sectionIndex) => (
                <div key={sectionIndex} className="mb-6">
                  <div className="px-6 mb-2">
                    <h3 className="text-xs font-semibold text-white/60 uppercase tracking-wider">
                      {section.title}
                    </h3>
        </div>
                  {section.items.map((item) => {
              const Icon = item.icon;
                    const active = isActive(item.path);
              return (
                <Link
                  key={item.path}
                  to={item.path}
                        onClick={() => setSidebarOpen(false)}
                        className={`flex items-center px-6 py-3 mx-2 rounded-lg transition-all duration-150 ${
                          active
                            ? 'bg-white/20 text-white border-l-[3px] border-white'
                            : 'text-white/80 hover:bg-white/10 hover:text-white'
                  }`}
                >
                        <Icon className="mr-3 text-lg" />
                        <span className="font-medium text-sm">{item.label}</span>
                </Link>
              );
            })}
                </div>
              ))}
          </nav>
        </aside>
        </>
      )}

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-w-0">
        {/* Top Bar */}
        <header className="sticky top-0 z-30 bg-white border-b border-gray-200 shadow-sm">
          <div className="px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
            {/* Left: Menu + Breadcrumbs */}
            <div className="flex items-center gap-4 flex-1 min-w-0">
              <button
                onClick={() => setSidebarOpen(true)}
                className="lg:hidden p-2 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg transition-colors"
              >
                <FiMenu size={20} />
              </button>
              <div className="hidden sm:block min-w-0">
                <Breadcrumbs items={getBreadcrumbs()} />
              </div>
            </div>

            {/* Right: Search + Profile */}
            <div className="flex items-center gap-3">
              {/* Search */}
              <div className="hidden md:flex items-center relative">
                <FiSearch className="absolute left-3 text-gray-400" size={18} />
                <input
                  type="text"
                  placeholder="Rechercher..."
                  className="pl-10 pr-4 py-2 w-64 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary-blue/20 focus:border-primary-blue transition-all"
                />
              </div>

              {/* Profile Dropdown */}
              {user && (
                <div className="relative">
                  <button
                    onClick={() => setProfileDropdownOpen(!profileDropdownOpen)}
                    className="flex items-center gap-3 px-3 py-2 hover:bg-gray-100 rounded-lg transition-colors"
                  >
                    <div className="w-8 h-8 bg-primary-blue rounded-full flex items-center justify-center text-white text-sm font-medium">
                      {user.prenom?.[0]}{user.nom?.[0]}
                    </div>
                    <div className="hidden md:block text-left">
                      <div className="text-sm font-medium text-gray-900">
                        {user.prenom} {user.nom}
                      </div>
                      <div className="text-xs text-gray-500">{user.role}</div>
                    </div>
                    <FiChevronDown className="hidden md:block text-gray-400" size={16} />
                  </button>

                  {/* Dropdown Menu */}
                  {profileDropdownOpen && (
                    <>
                      <div 
                        className="fixed inset-0 z-10"
                        onClick={() => setProfileDropdownOpen(false)}
                      />
                      <div className="absolute right-0 mt-2 w-56 bg-white rounded-lg shadow-xl border border-gray-200 py-2 z-20 animate-scaleIn">
                        <div className="px-4 py-3 border-b border-gray-200">
                          <div className="text-sm font-medium text-gray-900">
                            {user.prenom} {user.nom}
                          </div>
                          <div className="text-xs text-gray-500 truncate">{user.email}</div>
                          <div className="mt-1">
                            <Badge variant="info" size="sm">{user.role}</Badge>
                          </div>
                        </div>
                        <Link
                          to="/profil"
                          onClick={() => setProfileDropdownOpen(false)}
                          className="flex items-center gap-3 px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors"
                        >
                          <FiSettings size={18} />
                          <span>Paramètres</span>
                        </Link>
                        <button
                          onClick={handleLogout}
                          className="w-full flex items-center gap-3 px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors"
                        >
                          <FiLogOut size={18} />
                          <span>Déconnexion</span>
                        </button>
                      </div>
                    </>
                  )}
                </div>
              )}
            </div>
          </div>
        </header>

        {/* Page Content */}
        <main className="flex-1 p-4 sm:p-6 lg:p-8 overflow-y-auto">
          <div className="max-w-7xl mx-auto animate-fadeInUp">
          {children}
          </div>
        </main>
      </div>
    </div>
  );
}

export default Layout;