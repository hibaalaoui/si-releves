import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { agentService } from '../services/agentService';
import { FiPlus, FiRefreshCw, FiSearch, FiUser } from 'react-icons/fi';
import CreateAgentModal from '../components/CreateAgentModal';

function AgentsPage() {
  const [searchTerm, setSearchTerm] = useState('');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  const { data: agents = [], isLoading, error, refetch } = useQuery({
    queryKey: ['agents'],
    queryFn: () => agentService.getAll(),
  });

  const filteredAgents = agents.filter((agent) => {
    const search = searchTerm.toLowerCase();
    return (
      agent.nom.toLowerCase().includes(search) ||
      agent.prenom.toLowerCase().includes(search) ||
      agent.idAgent.toLowerCase().includes(search)
    );
  });

  if (isLoading) {
    return <div className="flex justify-center items-center h-64"><div className="text-gray-600">Chargement...</div></div>;
  }

  if (error) {
    return <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">Erreur lors du chargement des agents</div>;
  }

  return (
    <div>
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-800">Gestion des Agents</h1>
        <p className="text-gray-600 mt-1">Liste des agents de relevé</p>
      </div>

      <div className="bg-white rounded-lg shadow-md p-4 mb-6">
        <div className="flex flex-wrap gap-4 items-center justify-between">
          <div className="flex-1 min-w-[300px]">
            <div className="relative">
              <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
              <input
                type="text"
                placeholder="Rechercher par nom, prénom ou ID..."
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
              <FiPlus />Nouvel agent
            </button>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
        <div className="bg-white rounded-lg shadow-md p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm">Total agents</p>
              <p className="text-2xl font-bold text-gray-800">{agents.length}</p>
            </div>
            <div className="bg-blue-100 p-3 rounded-full">
              <FiUser className="text-blue-600 text-2xl" />
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg shadow-md p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm">Agents actifs</p>
              <p className="text-2xl font-bold text-green-600">{agents.filter(a => a.actif).length}</p>
            </div>
            <div className="bg-green-100 p-3 rounded-full">
              <FiUser className="text-green-600 text-2xl" />
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID Agent</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Agent</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Quartier</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Téléphones</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Statut</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {filteredAgents.map((agent) => (
              <tr key={agent.idAgent} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className="text-sm font-medium text-gray-900">{agent.idAgent}</span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm font-medium text-gray-900">{agent.nom} {agent.prenom}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className="px-2 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">
                    {agent.nomQuartier}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">Pro: {agent.telProfessionnel}</div>
                  {agent.telPersonnel && <div className="text-xs text-gray-500">Perso: {agent.telPersonnel}</div>}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${agent.actif ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                    {agent.actif ? 'Actif' : 'Inactif'}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {filteredAgents.length === 0 && <div className="text-center py-12 text-gray-500">Aucun agent trouvé</div>}
      </div>

      <div className="mt-4 text-sm text-gray-600">
        {filteredAgents.length} agent(s) affiché(s) sur {agents.length} au total
      </div>

      {/* Modal de création */}
      <CreateAgentModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
      />
    </div>
  );
}

export default AgentsPage;