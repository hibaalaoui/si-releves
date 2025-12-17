import { NavLink } from 'react-router-dom';
import {
  FiHome,
  FiZap,
  FiUsers,
  FiFileText,
  FiSettings,
} from 'react-icons/fi';
import { authStore } from '../../store/authStore';
import { ROLES } from '../../utils/constants';

const Sidebar = () => {
  const user = authStore((state) => state.user);

  const menuItems = [
    {
      path: '/dashboard',
      icon: FiHome,
      label: 'Tableau de bord',
    },
    {
      path: '/compteurs',
      icon: FiZap,
      label: 'Compteurs',
    },
    {
      path: '/agents',
      icon: FiUsers,
      label: 'Agents',
    },
    {
      path: '/releves',
      icon: FiFileText,
      label: 'Relevés',
    },
  ];

  // Add Users menu item only for Superadmin
  if (user?.role === ROLES.SUPERADMIN) {
    menuItems.push({
      path: '/users',
      icon: FiSettings,
      label: 'Utilisateurs',
    });
  }

  return (
    <aside className="w-64 bg-white shadow-lg border-r border-gray-200">
      <nav className="p-4">
        <ul className="space-y-2">
          {menuItems.map((item) => {
            const Icon = item.icon;
            return (
              <li key={item.path}>
                <NavLink
                  to={item.path}
                  className={({ isActive }) =>
                    `flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                      isActive
                        ? 'bg-primary-600 text-white'
                        : 'text-gray-700 hover:bg-gray-100'
                    }`
                  }
                >
                  <Icon className="w-5 h-5" />
                  <span className="font-medium">{item.label}</span>
                </NavLink>
              </li>
            );
          })}
        </ul>
      </nav>
    </aside>
  );
};

export default Sidebar;

