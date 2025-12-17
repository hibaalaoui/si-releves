import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { quartierService } from '../services/quartierService';
import Modal from './Modal';

function CreateQuartierModal({ isOpen, onClose }) {
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState: { errors }, reset } = useForm({
    defaultValues: {
      ville: 'Rabat'
    }
  });
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // Mutation pour créer un quartier
  const createMutation = useMutation({
    mutationFn: (data) => quartierService.create(data),
    onSuccess: () => {
      setSuccessMessage('Quartier créé avec succès !');
      setErrorMessage('');
      queryClient.invalidateQueries(['quartiers']);
      reset({ ville: 'Rabat' });
      setTimeout(() => {
        onClose();
        setSuccessMessage('');
      }, 2000);
    },
    onError: (error) => {
      setErrorMessage(error.response?.data?.message || 'Erreur lors de la création du quartier');
      setSuccessMessage('');
    },
  });

  const onSubmit = (data) => {
    createMutation.mutate({
      nomQuartier: data.nomQuartier,
      ville: data.ville,
    });
  };

  useEffect(() => {
    if (!isOpen) {
      setSuccessMessage('');
      setErrorMessage('');
      reset({ ville: 'Rabat' });
    }
  }, [isOpen, reset]);

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Créer un nouveau quartier" size="sm">
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

        {/* Nom du quartier */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Nom du quartier <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            {...register('nomQuartier', {
              required: 'Le nom du quartier est obligatoire',
              minLength: { value: 2, message: 'Le nom doit contenir au moins 2 caractères' }
            })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: Hay Riad"
          />
          {errors.nomQuartier && (
            <p className="text-red-500 text-sm mt-1">{errors.nomQuartier.message}</p>
          )}
        </div>

        {/* Ville */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Ville
          </label>
          <input
            type="text"
            {...register('ville')}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-gray-50"
            placeholder="Rabat"
          />
          <p className="text-gray-500 text-xs mt-1">Par défaut : Rabat</p>
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
            {createMutation.isPending ? 'Création...' : 'Créer le quartier'}
          </button>
        </div>
      </form>
    </Modal>
  );
}

export default CreateQuartierModal;