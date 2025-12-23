import { useState, useEffect } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { FiCopy, FiCheck, FiEye, FiEyeOff, FiAlertTriangle } from 'react-icons/fi';
import { utilisateurService } from '../services/utilisateurService';
import Modal from './Modal';
import { Button, Card } from './ui';

function ResetPasswordModal({ isOpen, onClose, utilisateur }) {
  const queryClient = useQueryClient();
  const [generatedPassword, setGeneratedPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [copied, setCopied] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  // Mutation pour réinitialiser le mot de passe
  const resetMutation = useMutation({
    mutationFn: () => utilisateurService.resetPassword(utilisateur.idUtilisateur),
    onSuccess: (response) => {
      setGeneratedPassword(response.generatedPassword || '');
      setErrorMessage('');
      queryClient.invalidateQueries(['utilisateurs']);
    },
    onError: (error) => {
      setErrorMessage(error.response?.data?.message || 'Erreur lors de la réinitialisation du mot de passe');
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

  useEffect(() => {
    if (!isOpen) {
      setGeneratedPassword('');
      setShowPassword(false);
      setCopied(false);
      setErrorMessage('');
    }
  }, [isOpen]);

  if (!utilisateur) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Réinitialiser le mot de passe" size="md">
      <div className="space-y-5">
        {/* Message d'information */}
        <div className="bg-blue-50 border-l-4 border-blue-500 p-4 rounded">
          <p className="text-sm text-blue-800">
            Un nouveau mot de passe sécurisé sera généré pour <strong>{utilisateur.prenom} {utilisateur.nom}</strong> ({utilisateur.email}).
          </p>
        </div>

        {/* Message d'erreur */}
        {errorMessage && (
          <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded">
            <p className="text-sm font-medium text-red-800">{errorMessage}</p>
          </div>
        )}

        {/* Mot de passe généré */}
        {generatedPassword && (
          <Card variant="bordered" className="border-success">
            <Card.Body>
              <div className="space-y-4">
                <p className="font-semibold text-success text-lg">Mot de passe réinitialisé avec succès !</p>
                <div className="space-y-2">
                  <p className="text-sm font-medium text-gray-700">
                    Nouveau mot de passe généré :
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
                      <FiAlertTriangle className="inline mr-2" />
                      <strong>Important :</strong> Notez ce mot de passe et communiquez-le à l'utilisateur de manière sécurisée. 
                      Il ne sera plus affiché après la fermeture de cette fenêtre.
                    </p>
                  </div>
                </div>
              </div>
            </Card.Body>
          </Card>
        )}

        {/* Boutons */}
        <div className="flex justify-end gap-3 pt-4">
          <Button
            type="button"
            variant="ghost"
            onClick={onClose}
            disabled={resetMutation.isPending}
          >
            {generatedPassword ? 'Fermer' : 'Annuler'}
          </Button>
          {!generatedPassword && (
            <Button
              type="button"
              variant="primary"
              onClick={() => resetMutation.mutate()}
              isLoading={resetMutation.isPending}
            >
              Réinitialiser le mot de passe
            </Button>
          )}
        </div>
      </div>
    </Modal>
  );
}

export default ResetPasswordModal;

