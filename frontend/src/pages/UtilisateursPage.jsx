import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { utilisateurService } from '../services/utilisateurService';
import { useAuthStore } from '../store/authStore';
import { FiPlus, FiEdit, FiRefreshCw, FiSearch, FiUsers, FiInbox } from 'react-icons/fi';
import { Button, Card, Badge, Input, EmptyState } from '../components/ui';
import CreateUtilisateurModal from '../components/CreateUtilisateurModal';
import EditUtilisateurModal from '../components/EditUtilisateurModal';
import ResetPasswordModal from '../components/ResetPasswordModal';

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
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isResetPasswordModalOpen, setIsResetPasswordModalOpen] = useState(false);
  const [selectedUtilisateur, setSelectedUtilisateur] = useState(null);

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

  // Loading State
  if (isLoading) {
    return (
      <div className="space-y-6">
        <div>
          <div className="skeleton h-10 w-64 mb-2" />
          <div className="skeleton h-5 w-96" />
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
            <FiUsers className="text-xl" />
            <div>
              <h3 className="font-semibold">Erreur lors du chargement</h3>
              <p className="text-sm">Impossible de charger les utilisateurs. Veuillez réessayer.</p>
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
        <h1 className="text-4xl font-heading font-bold text-gray-900 mb-2">Gestion des Utilisateurs</h1>
        <p className="text-gray-600">Liste des utilisateurs du système</p>
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
                placeholder="Rechercher par nom, prénom ou email..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>

            {/* Filtre par rôle */}
            <select
              value={roleFilter}
              onChange={(e) => setRoleFilter(e.target.value)}
              className="h-11 px-4 border-[1.5px] border-gray-300 rounded-lg transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-1 focus:border-primary-blue focus:ring-primary-blue/20"
            >
              <option value="">Tous les rôles</option>
              <option value="Superadmin">Superadmin</option>
              <option value="Utilisateur">Utilisateur</option>
            </select>

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
                Nouveau
              </Button>
            </div>
          </div>
        </Card.Body>
      </Card>

      {/* Tableau */}
      <Card>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Utilisateur
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Email
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Rôle
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Statut
                </th>
                <th className="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredUtilisateurs.map((user) => (
                <tr key={user.idUtilisateur} className="hover:bg-gray-50 transition-colors">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">
                      {user.prenom} {user.nom}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-900">{user.email}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <Badge
                      variant={user.role === 'Superadmin' ? 'warning' : 'info'}
                      size="md"
                    >
                      {user.role}
                    </Badge>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <Badge
                      variant={user.actif ? 'success' : 'error'}
                      size="md"
                    >
                      {user.actif ? 'Actif' : 'Inactif'}
                    </Badge>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center gap-3">
                      <button
                        onClick={() => {
                          setSelectedUtilisateur(user);
                          setIsEditModalOpen(true);
                        }}
                        className="flex items-center gap-1 text-primary-blue hover:text-primary-blue-dark transition-colors text-sm font-medium"
                      >
                        <FiEdit size={16} />
                        Modifier
                      </button>
                      <button
                        onClick={() => {
                          setSelectedUtilisateur(user);
                          setIsResetPasswordModalOpen(true);
                        }}
                        className="flex items-center gap-1 text-secondary-amber hover:text-secondary-amber-dark transition-colors text-sm font-medium"
                      >
                        <FiRefreshCw size={16} />
                        Reset MDP
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Empty State */}
        {filteredUtilisateurs.length === 0 && (
          <EmptyState
            icon={FiInbox}
            title="Aucun utilisateur trouvé"
            description={searchTerm ? 'Aucun résultat pour votre recherche.' : 'Commencez par ajouter un utilisateur.'}
            action={!searchTerm && (
              <Button
                variant="primary"
                onClick={() => setIsCreateModalOpen(true)}
              >
                <FiPlus className="mr-2" />
                Ajouter un utilisateur
              </Button>
            )}
          />
        )}
      </Card>

      {/* Stats Footer */}
      {filteredUtilisateurs.length > 0 && (
        <div className="text-sm text-gray-600 flex items-center justify-between">
          <span>
            {filteredUtilisateurs.length} utilisateur(s) affiché(s) sur {utilisateurs.length} au total
          </span>
        </div>
      )}

      {/* Modal de création */}
      <CreateUtilisateurModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
      />

      {/* Modal de modification */}
      <EditUtilisateurModal
        isOpen={isEditModalOpen}
        onClose={() => {
          setIsEditModalOpen(false);
          setSelectedUtilisateur(null);
        }}
        utilisateur={selectedUtilisateur}
      />

      {/* Modal de réinitialisation de mot de passe */}
      <ResetPasswordModal
        isOpen={isResetPasswordModalOpen}
        onClose={() => {
          setIsResetPasswordModalOpen(false);
          setSelectedUtilisateur(null);
        }}
        utilisateur={selectedUtilisateur}
      />
    </div>
  );
}

export default UtilisateursPage;