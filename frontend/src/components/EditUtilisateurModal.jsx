import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { utilisateurService } from '../services/utilisateurService';
import Modal from './Modal';
import { Input, Button } from './ui';

function EditUtilisateurModal({ isOpen, onClose, utilisateur }) {
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState: { errors }, reset } = useForm();
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // Initialiser le formulaire avec les données de l'utilisateur
  useEffect(() => {
    if (utilisateur && isOpen) {
      reset({
        nom: utilisateur.nom || '',
        prenom: utilisateur.prenom || '',
        email: utilisateur.email || '',
        role: utilisateur.role || '',
      });
    }
  }, [utilisateur, isOpen, reset]);

  // Mutation pour mettre à jour un utilisateur
  const updateMutation = useMutation({
    mutationFn: (data) => utilisateurService.update(utilisateur.idUtilisateur, data),
    onSuccess: () => {
      setSuccessMessage('Utilisateur modifié avec succès !');
      setErrorMessage('');
      queryClient.invalidateQueries(['utilisateurs']);
      setTimeout(() => {
        onClose();
        setSuccessMessage('');
      }, 2000);
    },
    onError: (error) => {
      setErrorMessage(error.response?.data?.message || 'Erreur lors de la modification de l\'utilisateur');
      setSuccessMessage('');
    },
  });

  const onSubmit = (data) => {
    updateMutation.mutate({
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
      reset();
    }
  }, [isOpen, reset]);

  if (!utilisateur) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Modifier l'utilisateur" size="md">
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
        {/* Messages */}
        {successMessage && (
          <div className="bg-green-50 border-l-4 border-green-500 p-4 rounded">
            <p className="text-sm font-medium text-green-800">{successMessage}</p>
          </div>
        )}

        {errorMessage && (
          <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded">
            <p className="text-sm font-medium text-red-800">{errorMessage}</p>
          </div>
        )}

        {/* Nom */}
        <Input
          label="Nom"
          type="text"
          placeholder="Ex: ALAOUI"
          error={errors.nom?.message}
          {...register('nom', {
            required: 'Le nom est obligatoire',
            minLength: { value: 2, message: 'Le nom doit contenir au moins 2 caractères' }
          })}
        />

        {/* Prénom */}
        <Input
          label="Prénom"
          type="text"
          placeholder="Ex: Mohammed"
          error={errors.prenom?.message}
          {...register('prenom', {
            required: 'Le prénom est obligatoire',
            minLength: { value: 2, message: 'Le prénom doit contenir au moins 2 caractères' }
          })}
        />

        {/* Email */}
        <Input
          label="Email"
          type="email"
          placeholder="Ex: mohammed.alaoui@ree.ma"
          error={errors.email?.message}
          {...register('email', {
            required: 'L\'email est obligatoire',
            pattern: {
              value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
              message: 'Format d\'email invalide'
            }
          })}
        />

        {/* Rôle */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Rôle <span className="text-red-500">*</span>
          </label>
          <select
            {...register('role', { required: 'Le rôle est obligatoire' })}
            className="w-full h-11 px-4 border-[1.5px] border-gray-300 rounded-lg transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-1 focus:border-primary-blue focus:ring-primary-blue/20"
          >
            <option value="">Sélectionner un rôle</option>
            <option value="Utilisateur">Utilisateur</option>
            <option value="Superadmin">Superadmin</option>
          </select>
          {errors.role && (
            <p className="mt-1.5 text-sm text-error">{errors.role.message}</p>
          )}
        </div>

        {/* Boutons */}
        <div className="flex justify-end gap-3 pt-4">
          <Button
            type="button"
            variant="ghost"
            onClick={onClose}
            disabled={updateMutation.isPending}
          >
            Annuler
          </Button>
          <Button
            type="submit"
            variant="primary"
            isLoading={updateMutation.isPending}
          >
            Enregistrer
          </Button>
        </div>
      </form>
    </Modal>
  );
}

export default EditUtilisateurModal;

