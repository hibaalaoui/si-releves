import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { quartierService } from '../services/quartierService';
import { FiPlus, FiRefreshCw, FiSearch, FiMapPin, FiEdit, FiTrash2 } from 'react-icons/fi';
import CreateQuartierModal from '../components/CreateQuartierModal';

function QuartiersPage() {
  const [searchTerm, setSearchTerm] = useState('');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  const { data: quartiers = [], isLoading, error, refetch } = useQuery({
    queryKey: ['quartiers'],
    queryFn: () => quartierService.getAll(),
  });

  const filteredQuartiers = quartiers.filter((quartier) => {
    const search = searchTerm.toLowerCase();
    return (
      quartier.nomQuartier.toLowerCase().includes(search) ||
      quartier.ville.toLowerCase().includes(search)
    );
  });

  if (isLoading) {
    return <div className="flex justify-center items-center h-64"><div className="text-gray-600">Chargement...</div></div>;
  }

  if (error) {
    return <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">Erreur lors du chargement des quartiers</div>;
  }

  return (
    <div>
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800">Gestion des Quartiers</h1>
        <p className="text-gray-600 mt-1">Liste des quartiers de la ville</p>
      </div>

      <div className="bg-white rounded-lg shadow-md p-4 mb-6">
        <div className="flex flex-wrap gap-4 items-center justify-between">
          <div className="flex-1 min-w-[300px]">
            <div className="relative">
              <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
              <input
                type="text"
                placeholder="Rechercher par nom ou ville..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
          </div>
          <div className="flex gap-2">
            <button onClick={() => refetch()} className="flex items-center gap-2 px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors">
              <FiRefreshCw />Actualiser
            </button>
            <button
              onClick={() => setIsCreateModalOpen(true)}
              className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
            >
              <FiPlus />Nouveau quartier
            </button>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md p-4 mb-6">
        <div className="flex items-center justify-between">
          <div>
            <p className="text-gray-500 text-sm">Total quartiers</p>
            <p className="text-2xl font-bold text-gray-800">{quartiers.length}</p>
          </div>
          <div className="bg-blue-100 p-3 rounded-full">
            <FiMapPin className="text-blue-600 text-2xl" />
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredQuartiers.map((quartier) => (
          <div key={quartier.idQuartier} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
            <div className="flex items-start justify-between mb-4">
              <div className="bg-blue-100 p-3 rounded-full">
                <FiMapPin className="text-blue-600 text-xl" />
              </div>
              <div className="flex gap-2">
                <button className="text-blue-600 hover:text-blue-800">
                  <FiEdit />
                </button>
                <button className="text-red-600 hover:text-red-800">
                  <FiTrash2 />
                </button>
              </div>
            </div>
            <h3 className="text-xl font-bold text-gray-800 mb-2">{quartier.nomQuartier}</h3>
            <p className="text-gray-600 text-sm">{quartier.ville}</p>
          </div>
        ))}
      </div>

      {filteredQuartiers.length === 0 && (
        <div className="bg-white rounded-lg shadow-md p-12 text-center text-gray-500">
          Aucun quartier trouvé
        </div>
      )}

      <div className="mt-4 text-sm text-gray-600">
        {filteredQuartiers.length} quartier(s) affiché(s) sur {quartiers.length} au total
      </div>

      {/* Modal de création */}
      <CreateQuartierModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
      />
    </div>
  );
}

export default QuartiersPage;