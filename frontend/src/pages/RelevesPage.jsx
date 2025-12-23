import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { releveService } from '../services/releveService';
import { FiPlus, FiRefreshCw, FiSearch, FiDroplet, FiZap, FiCalendar, FiInbox } from 'react-icons/fi';
import { format } from 'date-fns';
import { fr } from 'date-fns/locale';
import CreateReleveModal from '../components/CreateReleveModal';
import { Button, Card, Badge, Input, EmptyState } from '../components/ui';

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

  // Loading State
  if (isLoading) {
    return (
      <div className="space-y-6">
        <div>
          <div className="skeleton h-10 w-64 mb-2" />
          <div className="skeleton h-5 w-96" />
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[1, 2, 3].map(i => (
            <div key={i} className="skeleton h-32" />
          ))}
        </div>
        <div className="skeleton h-96" />
      </div>
    );
  }

  // Error State
  if (error) {
    return (
      <Card variant="bordered" className="border-error">
        <Card.Body>
          <div className="flex items-center gap-3 text-error">
            <FiZap className="text-xl" />
            <div>
              <h3 className="font-semibold">Erreur lors du chargement</h3>
              <p className="text-sm">Impossible de charger les relevés. Veuillez réessayer.</p>
            </div>
      </div>
        </Card.Body>
      </Card>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-4xl font-heading font-bold text-gray-900 mb-2">Gestion des Relevés</h1>
        <p className="text-gray-600">Historique complet des relevés de consommation</p>
      </div>

      {/* Barre d'actions */}
      <Card>
        <Card.Body>
        <div className="flex flex-wrap gap-4 items-center justify-between">
          {/* Recherche */}
          <div className="flex-1 min-w-[300px]">
              <Input
                leftIcon={FiSearch}
                type="text"
                placeholder="Rechercher par compteur, agent ou adresse..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
          </div>

          {/* Boutons d'action */}
          <div className="flex gap-2">
              <Button
                variant="ghost"
              onClick={() => refetch()}
            >
                <FiRefreshCw className="mr-2" />
              Actualiser
              </Button>
              <Button
                variant="primary"
              onClick={() => setIsCreateModalOpen(true)}
            >
                <FiPlus className="mr-2" />
              Nouveau relevé
              </Button>
            </div>
          </div>
        </Card.Body>
      </Card>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card hover>
          <Card.Body>
          <div className="flex items-center justify-between">
            <div>
                <p className="text-sm font-medium text-gray-500 mb-1">Total relevés</p>
                <p className="text-3xl font-bold text-gray-900">{releves.length}</p>
              </div>
              <div className="w-12 h-12 bg-primary-blue/10 rounded-xl flex items-center justify-center">
                <FiCalendar className="text-primary-blue text-2xl" />
            </div>
            </div>
          </Card.Body>
        </Card>

        <Card hover>
          <Card.Body>
          <div className="flex items-center justify-between">
            <div>
                <p className="text-sm font-medium text-gray-500 mb-1">Consommation Eau</p>
                <p className="text-3xl font-bold text-primary-blue">
                  {totalConsommationEau.toFixed(2)} <span className="text-lg">m³</span>
              </p>
            </div>
              <div className="w-12 h-12 bg-primary-blue/10 rounded-xl flex items-center justify-center">
                <FiDroplet className="text-primary-blue text-2xl" />
              </div>
            </div>
          </Card.Body>
        </Card>

        <Card hover>
          <Card.Body>
          <div className="flex items-center justify-between">
            <div>
                <p className="text-sm font-medium text-gray-500 mb-1">Consommation Élec.</p>
                <p className="text-3xl font-bold text-secondary-amber">
                  {totalConsommationElec.toFixed(2)} <span className="text-lg">kWh</span>
              </p>
            </div>
              <div className="w-12 h-12 bg-secondary-amber/10 rounded-xl flex items-center justify-center">
                <FiZap className="text-secondary-amber text-2xl" />
              </div>
            </div>
          </Card.Body>
        </Card>
      </div>

      {/* Tableau */}
      <Card>
        <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Date
              </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Compteur
              </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Agent
              </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Ancien Index
              </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Nouvel Index
              </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                Consommation
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {filteredReleves.map((releve) => (
                <tr key={releve.idReleve} className="hover:bg-gray-50 transition-colors">
                <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">
                      {format(new Date(releve.dateReleve), 'dd/MM/yyyy', { locale: fr })}
                    </div>
                    <div className="text-xs text-gray-500">
                      {format(new Date(releve.dateReleve), 'HH:mm', { locale: fr })}
                  </div>
                </td>
                  <td className="px-6 py-4">
                  <div className="flex items-center gap-2">
                    {releve.typeCompteur === 'Eau' ? (
                        <FiDroplet className="text-primary-blue" size={18} />
                    ) : (
                        <FiZap className="text-secondary-amber" size={18} />
                    )}
                      <div>
                        <div className="text-sm font-medium text-gray-900">
                      {releve.idCompteur}
                        </div>
                        <div className="text-xs text-gray-500 truncate max-w-xs">
                          {releve.adresseComplete}
                        </div>
                      </div>
                  </div>
                </td>
                  <td className="px-6 py-4">
                    <div className="text-sm font-medium text-gray-900">
                      {releve.prenomAgent} {releve.nomAgent}
                  </div>
                  <div className="text-xs text-gray-500">{releve.nomQuartier}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm text-gray-900">
                      {releve.ancienIndex} <span className="text-gray-500">{releve.unite}</span>
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-semibold text-gray-900">
                      {releve.nouvelIndex} <span className="text-gray-500 font-normal">{releve.unite}</span>
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                    <Badge 
                      variant={releve.typeCompteur === 'Eau' ? 'info' : 'warning'}
                      size="md"
                  >
                    {releve.consommation} {releve.unite}
                    </Badge>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>

        {/* Empty State */}
        {filteredReleves.length === 0 && (
          <EmptyState
            icon={FiInbox}
            title="Aucun relevé trouvé"
            description={searchTerm ? 'Aucun résultat pour votre recherche.' : 'Commencez par ajouter un relevé.'}
            action={!searchTerm && (
              <Button
                variant="primary"
                onClick={() => setIsCreateModalOpen(true)}
              >
                <FiPlus className="mr-2" />
                Ajouter un relevé
              </Button>
            )}
          />
        )}
      </Card>

      {/* Stats Footer */}
      {filteredReleves.length > 0 && (
        <div className="text-sm text-gray-600 flex items-center justify-between">
          <span>
        {filteredReleves.length} relevé(s) affiché(s) sur {releves.length} au total
          </span>
      </div>
      )}

      {/* Modal de création */}
      <CreateReleveModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
      />
    </div>
  );
}

export default RelevesPage;