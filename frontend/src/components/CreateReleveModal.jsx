import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { releveService } from '../services/releveService';
import Modal from './Modal';
import api from '../services/api';

function CreateReleveModal({ isOpen, onClose }) {
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState: { errors }, reset, watch } = useForm();
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // Récupérer les compteurs actifs
  const { data: compteurs = [] } = useQuery({
    queryKey: ['compteurs-actifs'],
    queryFn: async () => {
      const response = await api.get('/compteurs?actifOnly=true');
      return response.data;
    },
    enabled: isOpen,
  });

  // Récupérer les agents actifs
  const { data: agents = [] } = useQuery({
    queryKey: ['agents-actifs'],
    queryFn: async () => {
      const response = await api.get('/agents?actifOnly=true');
      return response.data;
    },
    enabled: isOpen,
  });

  // Surveiller le compteur sélectionné pour afficher son index actuel
  const selectedCompteurId = watch('idCompteur');
  const selectedCompteur = compteurs.find(c => c.idCompteur === selectedCompteurId);

  // Mutation pour créer un relevé
  const createMutation = useMutation({
    mutationFn: (data) => releveService.create(data),
    onSuccess: (response) => {
      setSuccessMessage(
        `Relevé enregistré avec succès ! Consommation : ${response.consommation} ${response.unite}`
      );
      setErrorMessage('');
      queryClient.invalidateQueries(['releves']);
      queryClient.invalidateQueries(['compteurs']);
      reset();
      setTimeout(() => {
        onClose();
        setSuccessMessage('');
      }, 3000);
    },
    onError: (error) => {
      setErrorMessage(error.response?.data?.message || 'Erreur lors de l\'enregistrement du relevé');
      setSuccessMessage('');
    },
  });

  const onSubmit = (data) => {
    createMutation.mutate({
      idCompteur: data.idCompteur,
      idAgent: data.idAgent,
      nouvelIndex: parseFloat(data.nouvelIndex),
      dateReleve: data.dateReleve ? new Date(data.dateReleve).toISOString() : undefined,
    });
  };

  useEffect(() => {
    if (!isOpen) {
      setSuccessMessage('');
      setErrorMessage('');
      reset();
    }
  }, [isOpen, reset]);

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Enregistrer un nouveau relevé" size="md">
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

        {/* Messages */}
        {successMessage && (
          <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded">
            {successMessage}
          </div>
        )}

        {errorMessage && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {errorMessage}
          </div>
        )}

        {/* Compteur */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Compteur <span className="text-red-500">*</span>
          </label>
          <select
            {...register('idCompteur', { required: 'Le compteur est obligatoire' })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">Sélectionner un compteur</option>
            {compteurs.map((compteur) => (
              <option key={compteur.idCompteur} value={compteur.idCompteur}>
                {compteur.idCompteur} - {compteur.type} - {compteur.adresseComplete}
              </option>
            ))}
          </select>
          {errors.idCompteur && (
            <p className="text-red-500 text-sm mt-1">{errors.idCompteur.message}</p>
          )}

          {/* Afficher l'index actuel */}
          {selectedCompteur && (
            <div className="mt-2 p-2 bg-blue-50 border border-blue-200 rounded">
              <p className="text-sm text-blue-800">
                <strong>Index actuel :</strong> {selectedCompteur.indexActuel} {selectedCompteur.type === 'Eau' ? 'm³' : 'kWh'}
              </p>
            </div>
          )}
        </div>

        {/* Agent */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Agent <span className="text-red-500">*</span>
          </label>
          <select
            {...register('idAgent', { required: 'L\'agent est obligatoire' })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">Sélectionner un agent</option>
            {agents.map((agent) => (
              <option key={agent.idAgent} value={agent.idAgent}>
                {agent.nom} {agent.prenom} - {agent.nomQuartier}
              </option>
            ))}
          </select>
          {errors.idAgent && (
            <p className="text-red-500 text-sm mt-1">{errors.idAgent.message}</p>
          )}
        </div>

        {/* Nouvel index */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Nouvel index <span className="text-red-500">*</span>
          </label>
          <input
            type="number"
            step="0.01"
            {...register('nouvelIndex', {
              required: 'Le nouvel index est obligatoire',
              min: { value: 0, message: 'L\'index doit être positif' }
            })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: 250.50"
          />
          {errors.nouvelIndex && (
            <p className="text-red-500 text-sm mt-1">{errors.nouvelIndex.message}</p>
          )}
        </div>

        {/* Date du relevé */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Date et heure du relevé (optionnelle)
          </label>
          <input
            type="datetime-local"
            {...register('dateReleve')}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
          <p className="text-gray-500 text-xs mt-1">Si vide, la date/heure actuelle sera utilisée</p>
        </div>

        {/* Info calcul automatique */}
        <div className="bg-blue-50 border border-blue-200 rounded-lg p-3">
          <p className="text-sm text-blue-800">
            ℹ️ La consommation sera calculée automatiquement : <strong>Nouvel index - Ancien index</strong>
          </p>
        </div>

        {/* Boutons */}
        <div className="flex justify-end gap-3 pt-4">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
          >
            Annuler
          </button>
          <button
            type="submit"
            disabled={createMutation.isPending}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:bg-gray-400"
          >
            {createMutation.isPending ? 'Enregistrement...' : 'Enregistrer le relevé'}
          </button>
        </div>
      </form>
    </Modal>
  );
}

export default CreateReleveModal;