import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { FiCopy, FiCheck, FiEye, FiEyeOff } from 'react-icons/fi';
import { utilisateurService } from '../services/utilisateurService';
import Modal from './Modal';
import { Button, Card } from './ui';

function CreateUtilisateurModal({ isOpen, onClose }) {
  const queryClient = useQueryClient();
  const { register, handleSubmit, formState: { errors }, reset } = useForm();
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [generatedPassword, setGeneratedPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [copied, setCopied] = useState(false);

  // Mutation pour créer un utilisateur
  const createMutation = useMutation({
    mutationFn: (data) => utilisateurService.create(data),
    onSuccess: (response) => {
      setSuccessMessage('Utilisateur créé avec succès !');
      setErrorMessage('');
      // Le backend retourne maintenant generatedPassword
      setGeneratedPassword(response.generatedPassword || '');
      queryClient.invalidateQueries(['utilisateurs']);
      reset();
      // Ne pas fermer automatiquement pour que l'admin puisse voir le mot de passe
    },
    onError: (error) => {
      setErrorMessage(error.response?.data?.message || 'Erreur lors de la création de l\'utilisateur');
      setSuccessMessage('');
      setGeneratedPassword('');
    },
  });

  // Copier le mot de passe dans le presse-papiers
  const copyPassword = async () => {
    if (generatedPassword) {
      await navigator.clipboard.writeText(generatedPassword);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

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
      setShowPassword(false);
      setCopied(false);
      reset();
    }
  }, [isOpen, reset]);

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Créer un nouvel utilisateur" size="md">
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

        {/* Messages */}
        {successMessage && (
          <Card variant="bordered" className="border-success">
            <Card.Body>
              <div className="space-y-4">
                <p className="font-semibold text-success text-lg">{successMessage}</p>
                {generatedPassword && (
                  <div className="space-y-2">
                    <p className="text-sm font-medium text-gray-700">
                      Mot de passe généré pour cet utilisateur :
                    </p>
                    <div className="flex items-center gap-2">
                      <div className="flex-1 relative">
                        <input
                          type={showPassword ? 'text' : 'password'}
                          value={generatedPassword}
                          readOnly
                          className="w-full px-4 py-3 pr-20 font-mono text-sm bg-gray-50 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-primary-blue"
                        />
                        <button
                          type="button"
                          onClick={() => setShowPassword(!showPassword)}
                          className="absolute right-2 top-1/2 -translate-y-1/2 p-2 text-gray-500 hover:text-gray-700"
                          title={showPassword ? 'Masquer' : 'Afficher'}
                        >
                          {showPassword ? <FiEyeOff size={18} /> : <FiEye size={18} />}
                        </button>
                      </div>
                      <Button
                        type="button"
                        variant="secondary"
                        onClick={copyPassword}
                        title="Copier le mot de passe"
                      >
                        {copied ? <FiCheck className="mr-2 text-success" /> : <FiCopy className="mr-2" />}
                        {copied ? 'Copié !' : 'Copier'}
                      </Button>
                    </div>
                    <div className="bg-amber-50 border-l-4 border-amber-500 p-3 rounded">
                      <p className="text-sm text-amber-800">
                        ⚠️ <strong>Important :</strong> Notez ce mot de passe et communiquez-le à l'utilisateur de manière sécurisée. 
                        Il ne sera plus affiché après la fermeture de cette fenêtre.
                      </p>
                    </div>
                  </div>
                )}
              </div>
            </Card.Body>
          </Card>
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
          <Button
            type="button"
            variant="ghost"
            onClick={onClose}
            disabled={createMutation.isPending}
          >
            {generatedPassword ? 'Fermer' : 'Annuler'}
          </Button>
          {!generatedPassword && (
            <Button
              type="submit"
              variant="primary"
              isLoading={createMutation.isPending}
            >
              Créer l'utilisateur
            </Button>
          )}
        </div>
      </form>
    </Modal>
  );
}

export default CreateUtilisateurModal;