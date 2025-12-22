import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { FiUser, FiMail, FiShield, FiKey, FiEdit } from 'react-icons/fi';

function ProfilPage() {
  const { user } = useAuthStore();
  const [activeTab, setActiveTab] = useState('profil');

  if (!user) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">Accès non autorisé</h2>
          <p className="text-gray-600 mb-4">Vous devez être connecté pour accéder à cette page.</p>
          <Link
            to="/login"
            className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors"
          >
            Se connecter
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto p-6">
      <div className="bg-white rounded-lg shadow-md">
        {/* Header */}
        <div className="bg-blue-600 text-white p-6 rounded-t-lg">
          <div className="flex items-center gap-4">
            <div className="w-16 h-16 bg-blue-700 rounded-full flex items-center justify-center">
              <FiUser className="text-2xl" />
            </div>
            <div>
              <h1 className="text-2xl font-bold">
                {user.prenom} {user.nom}
              </h1>
              <p className="text-blue-100">{user.email}</p>
            </div>
          </div>
        </div>

        {/* Navigation Tabs */}
        <div className="border-b border-gray-200">
          <nav className="flex">
            <button
              onClick={() => setActiveTab('profil')}
              className={`px-6 py-3 font-medium text-sm border-b-2 transition-colors ${
                activeTab === 'profil'
                  ? 'border-blue-600 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <FiUser className="inline mr-2" />
              Mon Profil
            </button>
            <button
              onClick={() => setActiveTab('securite')}
              className={`px-6 py-3 font-medium text-sm border-b-2 transition-colors ${
                activeTab === 'securite'
                  ? 'border-blue-600 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <FiKey className="inline mr-2" />
              Sécurité
            </button>
          </nav>
        </div>

        {/* Content */}
        <div className="p-6">
          {activeTab === 'profil' && (
            <div className="space-y-6">
              <h2 className="text-xl font-semibold text-gray-800 mb-4">Informations du Profil</h2>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Nom
                    </label>
                    <div className="flex items-center p-3 bg-gray-50 rounded-lg">
                      <FiUser className="text-gray-400 mr-3" />
                      <span className="text-gray-900">{user.nom}</span>
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Prénom
                    </label>
                    <div className="flex items-center p-3 bg-gray-50 rounded-lg">
                      <FiUser className="text-gray-400 mr-3" />
                      <span className="text-gray-900">{user.prenom}</span>
                    </div>
                  </div>
                </div>

                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Email
                    </label>
                    <div className="flex items-center p-3 bg-gray-50 rounded-lg">
                      <FiMail className="text-gray-400 mr-3" />
                      <span className="text-gray-900">{user.email}</span>
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Rôle
                    </label>
                    <div className="flex items-center p-3 bg-gray-50 rounded-lg">
                      <FiShield className="text-gray-400 mr-3" />
                      <span className="text-gray-900 capitalize">{user.role}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                <div className="flex items-start">
                  <FiShield className="text-blue-600 mt-1 mr-3 flex-shrink-0" />
                  <div>
                    <h3 className="text-sm font-medium text-blue-800 mb-1">
                      Informations sur votre compte
                    </h3>
                    <p className="text-sm text-blue-700">
                      {user.role === 'Superadmin'
                        ? 'Vous avez accès à toutes les fonctionnalités du système, y compris la gestion des utilisateurs.'
                        : 'Vous avez accès aux fonctionnalités de gestion des compteurs, relevés et agents dans les quartiers assignés.'
                      }
                    </p>
                  </div>
                </div>
              </div>
            </div>
          )}

          {activeTab === 'securite' && (
            <div className="space-y-6">
              <h2 className="text-xl font-semibold text-gray-800 mb-4">Sécurité du Compte</h2>

              <div className="bg-white border border-gray-200 rounded-lg p-6">
                <div className="flex items-center justify-between">
                  <div>
                    <h3 className="text-lg font-medium text-gray-900 mb-2">
                      Changer le mot de passe
                    </h3>
                    <p className="text-gray-600">
                      Modifiez votre mot de passe pour renforcer la sécurité de votre compte.
                    </p>
                  </div>
                  <Link
                    to="/change-password"
                    className="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                  >
                    <FiKey className="mr-2" />
                    Changer le mot de passe
                  </Link>
                </div>
              </div>

              <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                <div className="flex items-start">
                  <FiShield className="text-yellow-600 mt-1 mr-3 flex-shrink-0" />
                  <div>
                    <h3 className="text-sm font-medium text-yellow-800 mb-1">
                      Conseils de sécurité
                    </h3>
                    <ul className="text-sm text-yellow-700 space-y-1">
                      <li>• Utilisez un mot de passe fort avec au moins 8 caractères</li>
                      <li>• Ne partagez jamais vos identifiants de connexion</li>
                      <li>• Déconnectez-vous après utilisation du système</li>
                      <li>• Changez régulièrement votre mot de passe</li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default ProfilPage;
