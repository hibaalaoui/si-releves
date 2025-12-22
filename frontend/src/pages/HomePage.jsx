import { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiLogIn, FiUserPlus, FiUsers, FiGrid, FiClipboard, FiMapPin, FiShield, FiBarChart2 } from 'react-icons/fi';
import { useAuthStore } from '../store/authStore';

function HomePage() {
  const navigate = useNavigate();
  const { token, user } = useAuthStore();
  const isAuthenticated = !!(token && user);

  // Si l'utilisateur est déjà connecté, rediriger vers le dashboard
  useEffect(() => {
    if (isAuthenticated) {
      navigate('/dashboard', { replace: true });
    }
  }, [isAuthenticated, navigate]);

  // Si l'utilisateur est connecté, ne rien afficher pendant la redirection
  if (isAuthenticated) {
    return null;
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-indigo-50">
      {/* Header */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-blue-600 rounded-lg flex items-center justify-center">
                <FiShield className="text-white text-xl" />
              </div>
              <div>
                <h1 className="text-xl font-bold text-gray-900">SI Relevés</h1>
                <p className="text-xs text-gray-500">Rabat Energie & Eau</p>
              </div>
            </div>
            <div className="flex items-center gap-3">
              <Link
                to="/login"
                className="px-4 py-2 text-blue-600 hover:text-blue-700 font-medium transition-colors"
              >
                Se connecter
              </Link>
              <Link
                to="/login"
                className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium flex items-center gap-2"
              >
                <FiLogIn />
                Connexion
              </Link>
            </div>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="text-center">
          <h2 className="text-5xl font-bold text-gray-900 mb-6">
            Système d'Information de Gestion des Relevés
          </h2>
          <p className="text-xl text-gray-600 mb-4 max-w-3xl mx-auto">
            Plateforme digitale complète pour la gestion des relevés de compteurs d'eau et d'électricité
            dans la ville de Rabat
          </p>
          <p className="text-lg text-gray-500 mb-12 max-w-2xl mx-auto">
            Simplifiez vos opérations de relevé, suivez les performances de vos agents,
            et optimisez la gestion de vos compteurs avec notre solution intégrée.
          </p>
          
          <div className="flex items-center justify-center gap-4">
            <Link
              to="/login"
              className="px-8 py-4 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-semibold text-lg flex items-center gap-2 shadow-lg hover:shadow-xl"
            >
              <FiLogIn />
              Se connecter
            </Link>
            <Link
              to="/contact"
              className="px-8 py-4 bg-white text-blue-600 border-2 border-blue-600 rounded-lg hover:bg-blue-50 transition-colors font-semibold text-lg flex items-center gap-2"
            >
              <FiUserPlus />
              Demander un compte
            </Link>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <h3 className="text-3xl font-bold text-center text-gray-900 mb-12">
          Fonctionnalités Principales
        </h3>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {/* Feature 1 */}
          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
            <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center mb-4">
              <FiUsers className="text-blue-600 text-2xl" />
            </div>
            <h4 className="text-xl font-semibold text-gray-900 mb-2">Gestion des Utilisateurs</h4>
            <p className="text-gray-600">
              Administration complète des utilisateurs avec gestion des rôles et permissions.
              Création et gestion des comptes Superadmin et Utilisateurs.
            </p>
          </div>

          {/* Feature 2 */}
          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
            <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center mb-4">
              <FiGrid className="text-green-600 text-2xl" />
            </div>
            <h4 className="text-xl font-semibold text-gray-900 mb-2">Gestion des Compteurs</h4>
            <p className="text-gray-600">
              Création et suivi des compteurs d'eau et d'électricité.
              Association avec les adresses et gestion des index.
            </p>
          </div>

          {/* Feature 3 */}
          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
            <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center mb-4">
              <FiClipboard className="text-purple-600 text-2xl" />
            </div>
            <h4 className="text-xl font-semibold text-gray-900 mb-2">Gestion des Relevés</h4>
            <p className="text-gray-600">
              Suivi complet des relevés effectués par les agents de terrain.
              Calcul automatique des consommations et historique détaillé.
            </p>
          </div>

          {/* Feature 4 */}
          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
            <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center mb-4">
              <FiMapPin className="text-orange-600 text-2xl" />
            </div>
            <h4 className="text-xl font-semibold text-gray-900 mb-2">Gestion des Quartiers</h4>
            <p className="text-gray-600">
              Organisation par quartiers avec affectation des agents de terrain.
              Suivi géographique des opérations de relevé.
            </p>
          </div>

          {/* Feature 5 */}
          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
            <div className="w-12 h-12 bg-indigo-100 rounded-lg flex items-center justify-center mb-4">
              <FiUsers className="text-indigo-600 text-2xl" />
            </div>
            <h4 className="text-xl font-semibold text-gray-900 mb-2">Gestion des Agents</h4>
            <p className="text-gray-600">
              Suivi des agents de terrain avec leurs performances.
              Affectation aux quartiers et suivi des relevés quotidiens.
            </p>
          </div>

          {/* Feature 6 */}
          <div className="bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow">
            <div className="w-12 h-12 bg-teal-100 rounded-lg flex items-center justify-center mb-4">
              <FiBarChart2 className="text-teal-600 text-2xl" />
            </div>
            <h4 className="text-xl font-semibold text-gray-900 mb-2">Tableaux de Bord</h4>
            <p className="text-gray-600">
              Visualisation des KPIs et statistiques en temps réel.
              Rapports exportables et analyses de performance.
            </p>
          </div>
        </div>
      </section>

      {/* About Section */}
      <section className="bg-white py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h3 className="text-3xl font-bold text-gray-900 mb-6">À Propos de la Plateforme</h3>
            <div className="max-w-4xl mx-auto text-gray-600 space-y-4">
              <p className="text-lg">
                Le <strong>Système d'Information Relevés (SI Relevés)</strong> est une solution complète
                développée pour <strong>Rabat Energie & Eau (REE)</strong> dans le cadre de leur transformation digitale.
              </p>
              <p>
                Cette plateforme permet de digitaliser et automatiser le processus de relevé des compteurs
                d'eau et d'électricité, facilitant ainsi le travail des agents de terrain et améliorant
                le suivi et le contrôle des opérations.
              </p>
              <p>
                Le système intègre la gestion des compteurs, l'affectation des agents, le suivi des relevés,
                et la préparation des données pour la facturation.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="bg-blue-600 py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h3 className="text-3xl font-bold text-white mb-4">
            Prêt à commencer ?
          </h3>
          <p className="text-xl text-blue-100 mb-8">
            Connectez-vous pour accéder à votre tableau de bord ou contactez un administrateur pour obtenir un compte.
          </p>
          <div className="flex items-center justify-center gap-4">
            <Link
              to="/login"
              className="px-8 py-4 bg-white text-blue-600 rounded-lg hover:bg-gray-100 transition-colors font-semibold text-lg flex items-center gap-2"
            >
              <FiLogIn />
              Se connecter
            </Link>
            <Link
              to="/contact"
              className="px-8 py-4 bg-blue-700 text-white border-2 border-white rounded-lg hover:bg-blue-800 transition-colors font-semibold text-lg flex items-center gap-2"
            >
              <FiUserPlus />
              Demander un compte
            </Link>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-900 text-gray-400 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="mb-2">© 2024 Rabat Energie & Eau (REE). Tous droits réservés.</p>
          <p className="text-sm">Système d'Information Relevés - Version 1.0</p>
        </div>
      </footer>
    </div>
  );
}

export default HomePage;

