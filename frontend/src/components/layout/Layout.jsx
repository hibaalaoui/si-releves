import { Outlet } from 'react-router-dom';
import Header from './Header';
import Sidebar from './Sidebar';
import useInactivityLogout from '../../hooks/useInactivityLogout';

const Layout = () => {
  // Enable inactivity logout in layout
  useInactivityLogout();

  return (
    <div className="flex h-screen bg-gray-100">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-y-auto bg-gray-50">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Layout;

