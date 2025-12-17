import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { releveService } from '../services/releveService';
import { FiPlus, FiRefreshCw, FiSearch, FiDroplet, FiZap, FiCalendar } from 'react-icons/fi';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';
import CreateReleveModal from '../components/CreateReleveModal';

function RelevesPage() {
  const [searchTerm, setSearchTerm] = useState('');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  // Récupérer la liste des relevés
  const { data: releves = [], isLoading, error, refetch } = useQuery({
    queryKey: ['releves'],
    queryFn: () => releveService.getAll(),
  });

  // Filtrer les relevés
  const filteredReleves = releves.filter((releve) => {
    const search = searchTerm.toLowerCase();
    return (
      releve.idCompteur.toLowerCase().includes(search) ||
      releve.nomAgent.toLowerCase().includes(search) ||
      releve.prenomAgent.toLowerCase().includes(search) ||
      releve.adresseComplete.toLowerCase().includes(search)
    );
  });

  // Calculer les statistiques
  const totalConsommationEau = releves
    .filter(r => r.typeCompteur === 'Eau')
    .reduce((sum, r) => sum + r.consommation, 0);

  const totalConsommationElec = releves
    .filter(r => r.typeCompteur === 'Electricite')
    .reduce((sum, r) => sum + r.consommation, 0);

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
        Erreur lors du chargement des relevés
      </div>
    );
  }

  return (
    <div>
      {/* Header */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800">Gestion des Relevés</h1>
        <p className="text-gray-600 mt-1">Historique des relevés de consommation</p>
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
                placeholder="Rechercher par compteur, agent ou adresse..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
          </div>

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
              Nouveau relevé
            </button>
          </div>
        </div>
      </div>

      {/* Statistiques rapides */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div className="bg-white rounded-lg shadow-md p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm">Total relevés</p>
              <p className="text-2xl font-bold text-gray-800">{releves.length}</p>
            </div>
            <div className="bg-blue-100 p-3 rounded-full">
              <FiCalendar className="text-blue-600 text-2xl" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm">Consommation Eau</p>
              <p className="text-2xl font-bold text-blue-600">
                {totalConsommationEau.toFixed(2)} m³
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
              <p className="text-gray-500 text-sm">Consommation Élec.</p>
              <p className="text-2xl font-bold text-yellow-600">
                {totalConsommationElec.toFixed(2)} kWh
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
                Date
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Compteur
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Agent
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Ancien Index
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nouvel Index
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Consommation
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {filteredReleves.map((releve) => (
              <tr key={releve.idReleve} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">
                    {format(new Date(releve.dateReleve), 'dd/MM/yyyy HH:mm', { locale: fr })}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="flex items-center gap-2">
                    {releve.typeCompteur === 'Eau' ? (
                      <FiDroplet className="text-blue-600" />
                    ) : (
                      <FiZap className="text-yellow-600" />
                    )}
                    <span className="text-sm font-medium text-gray-900">
                      {releve.idCompteur}
                    </span>
                  </div>
                  <div className="text-xs text-gray-500">{releve.adresseComplete}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">
                    {releve.nomAgent} {releve.prenomAgent}
                  </div>
                  <div className="text-xs text-gray-500">{releve.nomQuartier}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">
                    {releve.ancienIndex} {releve.unite}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm font-medium text-gray-900">
                    {releve.nouvelIndex} {releve.unite}
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`px-3 py-1 inline-flex text-sm font-semibold rounded-full ${
                      releve.typeCompteur === 'Eau'
                        ? 'bg-blue-100 text-blue-800'
                        : 'bg-yellow-100 text-yellow-800'
                    }`}
                  >
                    {releve.consommation} {releve.unite}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {filteredReleves.length === 0 && (
          <div className="text-center py-12 text-gray-500">
            Aucun relevé trouvé
          </div>
        )}
      </div>

      {/* Stats */}
      <div className="mt-4 text-sm text-gray-600">
        {filteredReleves.length} relevé(s) affiché(s) sur {releves.length} au total
      </div>

      {/* Modal de création */}
      <CreateReleveModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
      />
    </div>
  );
}

export default RelevesPage;