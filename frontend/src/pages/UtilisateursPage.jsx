import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { utilisateurService } from '../services/utilisateurService';
import { useAuthStore } from '../store/authStore';
import { FiPlus, FiEdit, FiRefreshCw, FiSearch } from 'react-icons/fi';
import CreateUtilisateurModal from '../components/CreateUtilisateurModal';

function UtilisateursPage() {
  const { user } = useAuthStore();
  const navigate = useNavigate();

  // Vérifier les permissions au montage du composant
  useEffect(() => {
    if (user && user.role !== 'Superadmin') {
      // Rediriger vers le profil si l'utilisateur n'est pas Superadmin
      navigate('/profil', { replace: true });
    }
  }, [user, navigate]);

  // Si l'utilisateur n'est pas Superadmin, ne rien afficher (la redirection se fera)
  if (!user || user.role !== 'Superadmin') {
    return null;
  }
  const [searchTerm, setSearchTerm] = useState('');
  const [roleFilter, setRoleFilter] = useState('');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  // Récupérer la liste des utilisateurs avec React Query
  const { data: utilisateurs = [], isLoading, error, refetch } = useQuery({
    queryKey: ['utilisateurs', roleFilter],
    queryFn: () => utilisateurService.getAll({ role: roleFilter || undefined }),
  });

  // Filtrer les utilisateurs par nom/prénom/email
  const filteredUtilisateurs = utilisateurs.filter((user) => {
    const search = searchTerm.toLowerCase();
    return (
      user.nom.toLowerCase().includes(search) ||
      user.prenom.toLowerCase().includes(search) ||
      user.email.toLowerCase().includes(search)
    );
  });

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-gray-600">Chargement...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        Erreur lors du chargement des utilisateurs
      </div>
    );
  }

  return (
    <div>
      {/* Header */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800">Gestion des Utilisateurs</h1>
        <p className="text-gray-600 mt-1">Liste des utilisateurs du système</p>
      </div>

      {/* Barre d'actions */}
      <div className="bg-white rounded-lg shadow-md p-4 mb-6">
        <div className="flex flex-wrap gap-4 items-center justify-between">
          {/* Recherche */}
          <div className="flex-1 min-w-[300px]">
            <div className="relative">
              <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
              <input
                type="text"
                placeholder="Rechercher par nom, prénom ou email..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
          </div>

          {/* Filtre par rôle */}
          <select
            value={roleFilter}
            onChange={(e) => setRoleFilter(e.target.value)}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">Tous les rôles</option>
            <option value="Superadmin">Superadmin</option>
            <option value="Utilisateur">Utilisateur</option>
          </select>

          {/* Boutons d'action */}
          <div className="flex gap-2">
            <button
              onClick={() => refetch()}
              className="flex items-center gap-2 px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors"
            >
              <FiRefreshCw />
              Actualiser
            </button>
            <button
              onClick={() => setIsCreateModalOpen(true)}
              className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
            >
              <FiPlus />
              Nouveau
            </button>
          </div>
        </div>
      </div>

      {/* Tableau */}
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Utilisateur
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Email
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Rôle
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Statut
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {filteredUtilisateurs.map((user) => (
              <tr key={user.idUtilisateur} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap">
                  <div>
                    <div className="text-sm font-medium text-gray-900">
                      {user.nom} {user.prenom}
                    </div>
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">{user.email}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      user.role === 'Superadmin'
                        ? 'bg-purple-100 text-purple-800'
                        : 'bg-blue-100 text-blue-800'
                    }`}
                  >
                    {user.role}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      user.actif
                        ? 'bg-green-100 text-green-800'
                        : 'bg-red-100 text-red-800'
                    }`}
                  >
                    {user.actif ? 'Actif' : 'Inactif'}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button className="text-blue-600 hover:text-blue-900 mr-3">
                    <FiEdit className="inline" /> Modifier
                  </button>
                  <button className="text-orange-600 hover:text-orange-900">
                    <FiRefreshCw className="inline" /> Reset MDP
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {filteredUtilisateurs.length === 0 && (
          <div className="text-center py-12 text-gray-500">
            Aucun utilisateur trouvé
          </div>
        )}
      </div>

      {/* Stats */}
      <div className="mt-4 text-sm text-gray-600">
        {filteredUtilisateurs.length} utilisateur(s) affiché(s) sur {utilisateurs.length} au total
      </div>

      {/* Modal de création */}
      <CreateUtilisateurModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
      />
    </div>
  );
}

export default UtilisateursPage;