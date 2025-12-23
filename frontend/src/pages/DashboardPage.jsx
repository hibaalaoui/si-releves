import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { 
  FiClipboard, FiGrid, FiUser, FiMapPin, FiDroplet, FiZap, 
  FiTrendingUp, FiCalendar, FiRefreshCw, FiArrowRight 
} from 'react-icons/fi';
import { format, subDays, startOfDay } from 'date-fns';
import { fr } from 'date-fns/locale';
import { releveService } from '../services/releveService';
import { compteurService } from '../services/compteurService';
import { agentService } from '../services/agentService';
import { quartierService } from '../services/quartierService';
import { Card, Badge, Button } from '../components/ui';

function DashboardPage() {
  // Récupérer toutes les données
  const { data: releves = [], isLoading: relevesLoading } = useQuery({
    queryKey: ['releves'],
    queryFn: () => releveService.getAll(),
  });

  const { data: compteurs = [], isLoading: compteursLoading } = useQuery({
    queryKey: ['compteurs'],
    queryFn: () => compteurService.getAll(),
  });

  const { data: agents = [], isLoading: agentsLoading } = useQuery({
    queryKey: ['agents'],
    queryFn: () => agentService.getAll(),
  });

  const { data: quartiers = [], isLoading: quartiersLoading } = useQuery({
    queryKey: ['quartiers'],
    queryFn: () => quartierService.getAll(),
  });

  const isLoading = relevesLoading || compteursLoading || agentsLoading || quartiersLoading;

  // Calculer les statistiques
  const totalReleves = releves.length;
  const totalCompteurs = compteurs.length;
  const totalAgents = agents.length;
  const totalQuartiers = quartiers.length;

  // Relevés des 7 derniers jours
  const sevenDaysAgo = startOfDay(subDays(new Date(), 7));
  const recentReleves = releves.filter(r => new Date(r.dateReleve) >= sevenDaysAgo);
  const relevesLast7Days = recentReleves.length;

  // Consommations
  const totalConsommationEau = releves
    .filter(r => r.typeCompteur === 'Eau')
    .reduce((sum, r) => sum + r.consommation, 0);

  const totalConsommationElec = releves
    .filter(r => r.typeCompteur === 'Electricite')
    .reduce((sum, r) => sum + r.consommation, 0);

  // Répartition par type de compteur
  const compteursEau = compteurs.filter(c => c.typeCompteur === 'Eau').length;
  const compteursElec = compteurs.filter(c => c.typeCompteur === 'Electricite').length;

  // Agents actifs
  const agentsActifs = agents.filter(a => a.actif !== false).length;

  // Derniers relevés (5)
  const derniersReleves = [...releves]
    .sort((a, b) => new Date(b.dateReleve) - new Date(a.dateReleve))
    .slice(0, 5);

  // Loading State
  if (isLoading) {
    return (
      <div className="space-y-6">
        <div>
          <div className="skeleton h-10 w-64 mb-2" />
          <div className="skeleton h-5 w-96" />
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {[1, 2, 3, 4].map(i => (
            <div key={i} className="skeleton h-32" />
          ))}
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="skeleton h-96" />
          <div className="skeleton h-96" />
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-4xl font-heading font-bold text-gray-900 mb-2">Tableau de Bord</h1>
        <p className="text-gray-600">Vue d'ensemble du système de gestion des relevés</p>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {/* Total Relevés */}
        <Card hover>
          <Card.Body>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-500 mb-1">Total Relevés</p>
                <p className="text-3xl font-bold text-gray-900">{totalReleves}</p>
                <p className="text-xs text-gray-500 mt-1">
                  {relevesLast7Days} ces 7 derniers jours
                </p>
              </div>
              <div className="w-12 h-12 bg-primary-blue/10 rounded-xl flex items-center justify-center">
                <FiClipboard className="text-primary-blue text-2xl" />
              </div>
            </div>
          </Card.Body>
        </Card>

        {/* Total Compteurs */}
        <Card hover>
          <Card.Body>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-500 mb-1">Total Compteurs</p>
                <p className="text-3xl font-bold text-gray-900">{totalCompteurs}</p>
                <div className="flex gap-2 mt-1">
                  <Badge variant="info" size="sm">{compteursEau} Eau</Badge>
                  <Badge variant="warning" size="sm">{compteursElec} Élec.</Badge>
                </div>
              </div>
              <div className="w-12 h-12 bg-secondary-amber/10 rounded-xl flex items-center justify-center">
                <FiGrid className="text-secondary-amber text-2xl" />
              </div>
            </div>
          </Card.Body>
        </Card>

        {/* Total Agents */}
        <Card hover>
          <Card.Body>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-500 mb-1">Agents</p>
                <p className="text-3xl font-bold text-gray-900">{totalAgents}</p>
                <p className="text-xs text-success mt-1">
                  {agentsActifs} actifs
                </p>
              </div>
              <div className="w-12 h-12 bg-success/10 rounded-xl flex items-center justify-center">
                <FiUser className="text-success text-2xl" />
              </div>
            </div>
          </Card.Body>
        </Card>

        {/* Total Quartiers */}
        <Card hover>
          <Card.Body>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-500 mb-1">Quartiers</p>
                <p className="text-3xl font-bold text-gray-900">{totalQuartiers}</p>
              </div>
              <div className="w-12 h-12 bg-info/10 rounded-xl flex items-center justify-center">
                <FiMapPin className="text-info text-2xl" />
              </div>
            </div>
          </Card.Body>
        </Card>
      </div>

      {/* Consommations */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card>
          <Card.Header>
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-primary-blue/10 rounded-lg flex items-center justify-center">
                  <FiDroplet className="text-primary-blue text-xl" />
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-gray-900">Consommation Eau</h3>
                  <p className="text-sm text-gray-500">Total cumulé</p>
                </div>
              </div>
            </div>
          </Card.Header>
          <Card.Body>
            <div className="flex items-end gap-2">
              <p className="text-4xl font-bold text-primary-blue">
                {totalConsommationEau.toFixed(2)}
              </p>
              <p className="text-lg text-gray-500 mb-1">m³</p>
            </div>
            <div className="mt-4 flex items-center gap-2 text-sm text-gray-600">
              <FiTrendingUp className="text-success" />
              <span>Basé sur {releves.filter(r => r.typeCompteur === 'Eau').length} relevés</span>
            </div>
          </Card.Body>
        </Card>

        <Card>
          <Card.Header>
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-secondary-amber/10 rounded-lg flex items-center justify-center">
                  <FiZap className="text-secondary-amber text-xl" />
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-gray-900">Consommation Électricité</h3>
                  <p className="text-sm text-gray-500">Total cumulé</p>
                </div>
              </div>
            </div>
          </Card.Header>
          <Card.Body>
            <div className="flex items-end gap-2">
              <p className="text-4xl font-bold text-secondary-amber">
                {totalConsommationElec.toFixed(2)}
              </p>
              <p className="text-lg text-gray-500 mb-1">kWh</p>
            </div>
            <div className="mt-4 flex items-center gap-2 text-sm text-gray-600">
              <FiTrendingUp className="text-success" />
              <span>Basé sur {releves.filter(r => r.typeCompteur === 'Electricite').length} relevés</span>
            </div>
          </Card.Body>
        </Card>
      </div>

      {/* Derniers Relevés */}
      <Card>
        <Card.Header>
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-gray-100 rounded-lg flex items-center justify-center">
                <FiCalendar className="text-gray-700 text-xl" />
              </div>
              <div>
                <h3 className="text-lg font-semibold text-gray-900">Derniers Relevés</h3>
                <p className="text-sm text-gray-500">5 relevés les plus récents</p>
              </div>
            </div>
            <Link to="/releves">
              <Button variant="ghost" size="sm">
                Voir tout
                <FiArrowRight className="ml-2" />
              </Button>
            </Link>
          </div>
        </Card.Header>
        <Card.Body>
          {derniersReleves.length > 0 ? (
            <div className="space-y-3">
              {derniersReleves.map((releve) => (
                <div
                  key={releve.idReleve}
                  className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors"
                >
                  <div className="flex items-center gap-3 flex-1">
                    {releve.typeCompteur === 'Eau' ? (
                      <FiDroplet className="text-primary-blue text-xl" />
                    ) : (
                      <FiZap className="text-secondary-amber text-xl" />
                    )}
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2">
                        <p className="text-sm font-medium text-gray-900">
                          {releve.idCompteur}
                        </p>
                        <Badge
                          variant={releve.typeCompteur === 'Eau' ? 'info' : 'warning'}
                          size="sm"
                        >
                          {releve.consommation} {releve.unite}
                        </Badge>
                      </div>
                      <p className="text-xs text-gray-500 truncate">
                        {releve.adresseComplete}
                      </p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-medium text-gray-900">
                      {format(new Date(releve.dateReleve), 'dd/MM/yyyy', { locale: fr })}
                    </p>
                    <p className="text-xs text-gray-500">
                      {releve.prenomAgent} {releve.nomAgent}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              <FiCalendar className="mx-auto text-4xl mb-2 text-gray-300" />
              <p>Aucun relevé enregistré</p>
            </div>
          )}
        </Card.Body>
      </Card>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Link to="/releves">
          <Card hover className="h-full">
            <Card.Body>
              <div className="flex items-center gap-4">
                <div className="w-12 h-12 bg-primary-blue/10 rounded-xl flex items-center justify-center">
                  <FiClipboard className="text-primary-blue text-2xl" />
                </div>
                <div className="flex-1">
                  <h4 className="font-semibold text-gray-900">Gérer les Relevés</h4>
                  <p className="text-sm text-gray-500">Consulter et créer des relevés</p>
                </div>
                <FiArrowRight className="text-gray-400" />
              </div>
            </Card.Body>
          </Card>
        </Link>

        <Link to="/compteurs">
          <Card hover className="h-full">
            <Card.Body>
              <div className="flex items-center gap-4">
                <div className="w-12 h-12 bg-secondary-amber/10 rounded-xl flex items-center justify-center">
                  <FiGrid className="text-secondary-amber text-2xl" />
                </div>
                <div className="flex-1">
                  <h4 className="font-semibold text-gray-900">Gérer les Compteurs</h4>
                  <p className="text-sm text-gray-500">Voir et ajouter des compteurs</p>
                </div>
                <FiArrowRight className="text-gray-400" />
              </div>
            </Card.Body>
          </Card>
        </Link>

        <Link to="/agents">
          <Card hover className="h-full">
            <Card.Body>
              <div className="flex items-center gap-4">
                <div className="w-12 h-12 bg-success/10 rounded-xl flex items-center justify-center">
                  <FiUser className="text-success text-2xl" />
                </div>
                <div className="flex-1">
                  <h4 className="font-semibold text-gray-900">Gérer les Agents</h4>
                  <p className="text-sm text-gray-500">Voir et gérer les agents</p>
                </div>
                <FiArrowRight className="text-gray-400" />
              </div>
            </Card.Body>
          </Card>
        </Link>
      </div>
    </div>
  );
}

export default DashboardPage;


