import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { agentService } from '../services/agentService';
import { quartierService } from '../services/quartierService';
import Modal from './Modal';

function CreateAgentModal({ isOpen, onClose }) {
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState: { errors }, reset } = useForm();
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // Récupérer les quartiers
  const { data: quartiers = [] } = useQuery({
    queryKey: ['quartiers'],
    queryFn: () => quartierService.getAll(),
    enabled: isOpen,
  });

  // Mutation pour créer un agent
  const createMutation = useMutation({
    mutationFn: (data) => agentService.create(data),
    onSuccess: () => {
      setSuccessMessage('Agent créé avec succès !');
      setErrorMessage('');
      queryClient.invalidateQueries(['agents']);
      reset();
      setTimeout(() => {
        onClose();
        setSuccessMessage('');
      }, 2000);
    },
    onError: (error) => {
      setErrorMessage(error.response?.data?.message || 'Erreur lors de la création de l\'agent');
      setSuccessMessage('');
    },
  });

  const onSubmit = (data) => {
    createMutation.mutate({
      idAgent: data.idAgent,
      idQuartier: parseInt(data.idQuartier),
      nom: data.nom,
      prenom: data.prenom,
      telPersonnel: data.telPersonnel || null,
      telProfessionnel: data.telProfessionnel,
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
    <Modal isOpen={isOpen} onClose={onClose} title="Créer un nouvel agent" size="md">
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

        {/* ID Agent */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            ID Agent <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            {...register('idAgent', {
              required: 'L\'ID de l\'agent est obligatoire',
              pattern: { value: /^AGT\d{3}$/, message: 'Format: AGT001, AGT002, etc.' }
            })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: AGT005"
          />
          {errors.idAgent && (
            <p className="text-red-500 text-sm mt-1">{errors.idAgent.message}</p>
          )}
        </div>

        {/* Nom */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Nom <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            {...register('nom', { required: 'Le nom est obligatoire' })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: BENCHEKROUN"
          />
          {errors.nom && (
            <p className="text-red-500 text-sm mt-1">{errors.nom.message}</p>
          )}
        </div>

        {/* Prénom */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Prénom <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            {...register('prenom', { required: 'Le prénom est obligatoire' })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: Rachid"
          />
          {errors.prenom && (
            <p className="text-red-500 text-sm mt-1">{errors.prenom.message}</p>
          )}
        </div>

        {/* Quartier */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Quartier affecté <span className="text-red-500">*</span>
          </label>
          <select
            {...register('idQuartier', { required: 'Le quartier est obligatoire' })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">Sélectionner un quartier</option>
            {quartiers.map((quartier) => (
              <option key={quartier.idQuartier} value={quartier.idQuartier}>
                {quartier.nomQuartier} - {quartier.ville}
              </option>
            ))}
          </select>
          {errors.idQuartier && (
            <p className="text-red-500 text-sm mt-1">{errors.idQuartier.message}</p>
          )}
        </div>

        {/* Téléphone professionnel */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Téléphone professionnel <span className="text-red-500">*</span>
          </label>
          <input
            type="tel"
            {...register('telProfessionnel', {
              required: 'Le téléphone professionnel est obligatoire',
              pattern: { value: /^0[5-7]\d{8}$/, message: 'Format: 06xxxxxxxx ou 05xxxxxxxx' }
            })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: 0537123456"
          />
          {errors.telProfessionnel && (
            <p className="text-red-500 text-sm mt-1">{errors.telProfessionnel.message}</p>
          )}
        </div>

        {/* Téléphone personnel */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Téléphone personnel (optionnel)
          </label>
          <input
            type="tel"
            {...register('telPersonnel', {
              pattern: { value: /^0[5-7]\d{8}$/, message: 'Format: 06xxxxxxxx ou 05xxxxxxxx' }
            })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: 0612345678"
          />
          {errors.telPersonnel && (
            <p className="text-red-500 text-sm mt-1">{errors.telPersonnel.message}</p>
          )}
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
            {createMutation.isPending ? 'Création...' : 'Créer l\'agent'}
          </button>
        </div>
      </form>
    </Modal>
  );
}

export default CreateAgentModal;