import { authStore } from '../../store/authStore';
import { useNavigate } from 'react-router-dom';
import { FiLogOut, FiUser } from 'react-icons/fi';

const Header = () => {
  const user = authStore((state) => state.user);
  const logout = authStore((state) => state.logout);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-white shadow-sm border-b border-gray-200">
      <div className="flex items-center justify-between px-6 py-4">
        <div className="flex items-center">
          <h1 className="text-xl font-semibold text-gray-800">
            SI Relevés - RABAT ENERGIE & EAU
          </h1>
        </div>
        <div className="flex items-center gap-4">
          <div className="flex items-center gap-2 text-gray-700">
            <FiUser className="w-5 h-5" />
            <span className="text-sm font-medium">
              {user?.prenom} {user?.nom}
            </span>
            <span className="text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded">
              {user?.role}
            </span>
          </div>
          <button
            onClick={handleLogout}
            className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-gray-700 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
          >
            <FiLogOut className="w-4 h-4" />
            Déconnexion
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;

