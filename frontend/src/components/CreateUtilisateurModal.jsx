import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { utilisateurService } from '../services/utilisateurService';
import Modal from './Modal';

function CreateUtilisateurModal({ isOpen, onClose }) {
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState: { errors }, reset } = useForm();
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [generatedPassword, setGeneratedPassword] = useState('');

  // Mutation pour créer un utilisateur
  const createMutation = useMutation({
    mutationFn: (data) => utilisateurService.create(data),
    onSuccess: (response) => {
      setSuccessMessage('Utilisateur créé avec succès !');
      setErrorMessage('');
      setGeneratedPassword(`Mot de passe généré : ${response.passwordGenere || 'Voir console backend'}`);
      queryClient.invalidateQueries(['utilisateurs']);
      reset();
      setTimeout(() => {
        onClose();
        setSuccessMessage('');
        setGeneratedPassword('');
      }, 4000);
    },
    onError: (error) => {
      setErrorMessage(error.response?.data?.message || 'Erreur lors de la création de l\'utilisateur');
      setSuccessMessage('');
    },
  });

  const onSubmit = (data) => {
    createMutation.mutate({
      nom: data.nom,
      prenom: data.prenom,
      email: data.email,
      role: data.role,
    });
  };

  useEffect(() => {
    if (!isOpen) {
      setSuccessMessage('');
      setErrorMessage('');
      setGeneratedPassword('');
      reset();
    }
  }, [isOpen, reset]);

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Créer un nouvel utilisateur" size="md">
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

        {/* Messages */}
        {successMessage && (
          <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded">
            <p className="font-semibold">{successMessage}</p>
            {generatedPassword && (
              <p className="text-sm mt-2 font-mono bg-green-50 p-2 rounded">{generatedPassword}</p>
            )}
          </div>
        )}

        {errorMessage && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {errorMessage}
          </div>
        )}

        {/* Nom */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Nom <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            {...register('nom', {
              required: 'Le nom est obligatoire',
              minLength: { value: 2, message: 'Le nom doit contenir au moins 2 caractères' }
            })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: ALAOUI"
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
            {...register('prenom', {
              required: 'Le prénom est obligatoire',
              minLength: { value: 2, message: 'Le prénom doit contenir au moins 2 caractères' }
            })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: Mohammed"
          />
          {errors.prenom && (
            <p className="text-red-500 text-sm mt-1">{errors.prenom.message}</p>
          )}
        </div>

        {/* Email */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Email <span className="text-red-500">*</span>
          </label>
          <input
            type="email"
            {...register('email', {
              required: 'L\'email est obligatoire',
              pattern: {
                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                message: 'Format d\'email invalide'
              }
            })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Ex: mohammed.alaoui@ree.ma"
          />
          {errors.email && (
            <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>
          )}
        </div>

        {/* Rôle */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Rôle <span className="text-red-500">*</span>
          </label>
          <select
            {...register('role', { required: 'Le rôle est obligatoire' })}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">Sélectionner un rôle</option>
            <option value="Utilisateur">Utilisateur</option>
            <option value="Superadmin">Superadmin</option>
          </select>
          {errors.role && (
            <p className="text-red-500 text-sm mt-1">{errors.role.message}</p>
          )}
        </div>

        {/* Info mot de passe */}
        <div className="bg-blue-50 border border-blue-200 rounded-lg p-3">
          <p className="text-sm text-blue-800">
            ℹ️ Un mot de passe sécurisé sera généré automatiquement et affiché après la création.
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
            {createMutation.isPending ? 'Création...' : 'Créer l\'utilisateur'}
          </button>
        </div>
      </form>
    </Modal>
  );
}

export default CreateUtilisateurModal;