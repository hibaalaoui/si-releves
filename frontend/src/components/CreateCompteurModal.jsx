import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { compteurService } from '../services/compteurService';
import Modal from './Modal';

// On va créer les services adresse et client si besoin
import api from '../services/api';

function CreateCompteurModal({ isOpen, onClose }) {
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState: { errors }, reset } = useForm();
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // Récupérer les adresses disponibles
  const { data: adresses = [] } = useQuery({
    queryKey: ['adresses'],
    queryFn: async () => {
      const response = await api.get('/adresses');
      return response.data;
    },
    enabled: isOpen, // Charger uniquement quand le modal est ouvert
  });

  // Mutation pour créer un compteur
  const createMutation = useMutation({
    mutationFn: (data) => compteurService.create(data),
    onSuccess: () => {
      setSuccessMessage('Compteur créé avec succès !');
      setErrorMessage('');
      queryClient.invalidateQueries(['compteurs']); // Rafraîchir la liste
      reset();
      setTimeout(() => {
        onClose();
        setSuccessMessage('');
      }, 2000);
    },
    onError: (error) => {
      setErrorMessage(error.response?.data?.message || 'Erreur lors de la création du compteur');
      setSuccessMessage('');
    },
  });

  const onSubmit = (data) => {
    createMutation.mutate({
      idAdresse: parseInt(data.idAdresse),
      type: data.type,
      pourEspacesCommuns: data.pourEspacesCommuns === 'true',
      dateInstallation: data.dateInstallation || undefined,
    });
  };

  // Réinitialiser les messages quand on ferme le modal
  useEffect(() => {
    if (!isOpen) {
      setSuccessMessage('');
      setErrorMessage('');
      reset();
    }
  }, [isOpen, reset]);

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Créer un nouveau compteur" size="md">
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

        {/* Adresse */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Adresse <span className="text-red-500">*</span>
          </label>
          <select
            {...register('idAdresse', { required: 'L\'adresse est obligatoire' })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">Sélectionner une adresse</option>
            {adresses.map((adresse) => (
              <option key={adresse.idAdresse} value={adresse.idAdresse}>
                {adresse.adresseComplete} - {adresse.nomQuartier}
              </option>
            ))}
          </select>
          {errors.idAdresse && (
            <p className="text-red-500 text-sm mt-1">{errors.idAdresse.message}</p>
          )}
        </div>

        {/* Type */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Type de compteur <span className="text-red-500">*</span>
          </label>
          <select
            {...register('type', { required: 'Le type est obligatoire' })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">Sélectionner un type</option>
            <option value="Eau">Eau</option>
            <option value="Electricite">Électricité</option>
          </select>
          {errors.type && (
            <p className="text-red-500 text-sm mt-1">{errors.type.message}</p>
          )}
        </div>

        {/* Pour espaces communs */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Usage
          </label>
          <select
            {...register('pourEspacesCommuns')}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="false">Standard</option>
            <option value="true">Espaces communs (Immeuble)</option>
          </select>
        </div>

        {/* Date d'installation */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Date d'installation (optionnelle)
          </label>
          <input
            type="date"
            {...register('dateInstallation')}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
          <p className="text-gray-500 text-xs mt-1">Si vide, la date du jour sera utilisée</p>
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
            {createMutation.isPending ? 'Création...' : 'Créer le compteur'}
          </button>
        </div>
      </form>
    </Modal>
  );
}

export default CreateCompteurModal;