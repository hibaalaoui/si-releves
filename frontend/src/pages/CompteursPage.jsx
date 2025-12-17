import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { compteurService } from '../services/compteurService';
import { FiPlus, FiRefreshCw, FiSearch, FiDroplet, FiZap } from 'react-icons/fi';
import CreateCompteurModal from '../components/CreateCompteurModal';

function CompteursPage() {
  const [searchTerm, setSearchTerm] = useState('');
  const [typeFilter, setTypeFilter] = useState('');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  // Récupérer la liste des compteurs
  const { data: compteurs = [], isLoading, error, refetch } = useQuery({
    queryKey: ['compteurs'],
    queryFn: () => compteurService.getAll(),
  });

  // Filtrer les compteurs
  const filteredCompteurs = compteurs.filter((compteur) => {
    const search = searchTerm.toLowerCase();
    const matchSearch =
      compteur.idCompteur.toLowerCase().includes(search) ||
      compteur.adresseComplete.toLowerCase().includes(search) ||
      compteur.nomQuartier.toLowerCase().includes(search);

    const matchType = !typeFilter || compteur.type === typeFilter;

    return matchSearch && matchType;
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
        Erreur lors du chargement des compteurs
      </div>
    );
  }

  return (
    <div>
      {/* Header */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800">Gestion des Compteurs</h1>
        <p className="text-gray-600 mt-1">Liste des compteurs d'eau et d'électricité</p>
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
                placeholder="Rechercher par ID, adresse ou quartier..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
          </div>

          {/* Filtre par type */}
          <select
            value={typeFilter}
            onChange={(e) => setTypeFilter(e.target.value)}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">Tous les types</option>
            <option value="Eau">Eau</option>
            <option value="Electricite">Électricité</option>
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
              Nouveau compteur
            </button>
          </div>
        </div>
      </div>

      {/* Statistiques rapides */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div className="bg-white rounded-lg shadow-md p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm">Total compteurs</p>
              <p className="text-2xl font-bold text-gray-800">{compteurs.length}</p>
            </div>
            <div className="bg-blue-100 p-3 rounded-full">
              <FiDroplet className="text-blue-600 text-2xl" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm">Compteurs Eau</p>
              <p className="text-2xl font-bold text-blue-600">
                {compteurs.filter(c => c.type === 'Eau').length}
              </p>
            </div>
            <div className="bg-blue-100 p-3 rounded-full">
              <FiDroplet className="text-blue-600 text-2xl" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm">Compteurs Électricité</p>
              <p className="text-2xl font-bold text-yellow-600">
                {compteurs.filter(c => c.type === 'Electricite').length}
              </p>
            </div>
            <div className="bg-yellow-100 p-3 rounded-full">
              <FiZap className="text-yellow-600 text-2xl" />
            </div>
          </div>
        </div>
      </div>

      {/* Tableau */}
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                ID Compteur
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Type
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Adresse
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Quartier
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Index Actuel
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Statut
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {filteredCompteurs.map((compteur) => (
              <tr key={compteur.idCompteur} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm font-medium text-gray-900">
                    {compteur.idCompteur}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`px-2 py-1 inline-flex items-center gap-1 text-xs leading-5 font-semibold rounded-full ${
                      compteur.type === 'Eau'
                        ? 'bg-blue-100 text-blue-800'
                        : 'bg-yellow-100 text-yellow-800'
                    }`}
                  >
                    {compteur.type === 'Eau' ? <FiDroplet /> : <FiZap />}
                    {compteur.type}
                  </span>
                </td>
                <td className="px-6 py-4">
                  <div className="text-sm text-gray-900">{compteur.adresseComplete}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">{compteur.nomQuartier}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm font-medium text-gray-900">
                    {compteur.indexActuel} {compteur.type === 'Eau' ? 'm³' : 'kWh'}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      compteur.actif
                        ? 'bg-green-100 text-green-800'
                        : 'bg-red-100 text-red-800'
                    }`}
                  >
                    {compteur.actif ? 'Actif' : 'Inactif'}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {filteredCompteurs.length === 0 && (
          <div className="text-center py-12 text-gray-500">
            Aucun compteur trouvé
          </div>
        )}
      </div>

      {/* Stats */}
      <div className="mt-4 text-sm text-gray-600">
        {filteredCompteurs.length} compteur(s) affiché(s) sur {compteurs.length} au total
      </div>

      {/* Modal de création */}
      <CreateCompteurModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
      />
    </div>
  );
}

export default CompteursPage;