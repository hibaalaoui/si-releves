import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastProvider } from './contexts/ToastContext';
import Layout from './components/Layout';
import { ProtectedRoute } from './components/ProtectedRoute';
import { RoleProtectedRoute } from './components/RoleProtectedRoute';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import ContactPage from './pages/ContactPage';
import ChangePasswordPage from './pages/ChangePasswordPage';
import ProfilPage from './pages/ProfilPage';
import UtilisateursPage from './pages/UtilisateursPage';
import CompteursPage from './pages/CompteursPage';
import RelevesPage from './pages/RelevesPage';
import AgentsPage from './pages/AgentsPage';
import QuartiersPage from './pages/QuartiersPage';
import DashboardPage from './pages/DashboardPage';

function App() {
  return (
    <ToastProvider>
    <Router>
      <Routes>
        {/* Routes publiques */}
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/contact" element={<ContactPage />} />
        <Route path="/change-password" element={<ChangePasswordPage />} />
        <Route
          path="/profil"
          element={
            <ProtectedRoute>
              <Layout>
                <ProfilPage />
              </Layout>
            </ProtectedRoute>
          }
        />

        {/* Routes protégées */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Layout>
                <DashboardPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/utilisateurs"
          element={
            <ProtectedRoute>
              <RoleProtectedRoute allowedRoles={['Superadmin']}>
              <Layout>
                <UtilisateursPage />
              </Layout>
              </RoleProtectedRoute>
            </ProtectedRoute>
          }
        />
        <Route
          path="/compteurs"
          element={
            <ProtectedRoute>
              <Layout>
                <CompteursPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/releves"
          element={
            <ProtectedRoute>
              <Layout>
                <RelevesPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/agents"
          element={
            <ProtectedRoute>
              <Layout>
                <AgentsPage />
              </Layout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/quartiers"
          element={
            <ProtectedRoute>
              <Layout>
                <QuartiersPage />
              </Layout>
            </ProtectedRoute>
          }
        />

        {/* Redirection par défaut pour les routes protégées */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
    </ToastProvider>
  );
}

export default App;