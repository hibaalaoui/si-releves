import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import UtilisateursPage from './pages/UtilisateursPage';
import CompteursPage from './pages/CompteursPage';
import RelevesPage from './pages/RelevesPage';
import AgentsPage from './pages/AgentsPage';
import QuartiersPage from './pages/QuartiersPage';

function App() {
  return (
    <Router>
      <Routes>
        {/* Routes avec Layout (sidebar) */}
        <Route path="/" element={<Layout><Navigate to="/utilisateurs" replace /></Layout>} />
        <Route path="/utilisateurs" element={<Layout><UtilisateursPage /></Layout>} />
        <Route path="/compteurs" element={<Layout><CompteursPage /></Layout>} />
        <Route path="/releves" element={<Layout><RelevesPage /></Layout>} />
        <Route path="/agents" element={<Layout><AgentsPage /></Layout>} />
        <Route path="/quartiers" element={<Layout><QuartiersPage /></Layout>} />
        {/* Redirection par défaut */}
        <Route path="*" element={<Navigate to="/utilisateurs" replace />} />
      </Routes>
    </Router>
  );
}

export default App;