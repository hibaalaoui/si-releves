import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, useLocation } from 'react-router-dom';
import { authService } from '../services/authService';
import { useAuthStore } from '../store/authStore';
import { FiMail, FiLock, FiLogIn, FiShield } from 'react-icons/fi';
import { Input, Button, Card } from '../components/ui';

function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { login, token, user } = useAuthStore();
  const { register, handleSubmit, formState: { errors } } = useForm();
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const isAuthenticated = !!(token && user);

  // Si déjà connecté, rediriger vers la page d'accueil
  useEffect(() => {
    if (isAuthenticated) {
      navigate('/', { replace: true });
    }
  }, [isAuthenticated, navigate]);

  const onSubmit = async (data) => {
    setIsLoading(true);
    setErrorMessage('');

    try {
      const response = await authService.login(data.email, data.password);
      
      // Sauvegarder le token et les données utilisateur
      // Normaliser le rôle en string pour éviter les problèmes de comparaison
      const normalizedRole = String(response.role || '').trim();
      login(response.token, {
        idUtilisateur: response.idUtilisateur,
        nom: response.nom,
        prenom: response.prenom,
        email: response.email,
        role: normalizedRole,
        premiereConnexion: response.premiereConnexion,
      });

      // Si première connexion, rediriger vers le changement de mot de passe
      if (response.premiereConnexion) {
        navigate('/change-password', { replace: true });
      } else {
        // Rediriger vers la page d'origine ou le dashboard
        const from = location.state?.from?.pathname || '/dashboard';
        navigate(from, { replace: true });
      }
    } catch (error) {
      // Gérer les erreurs
      if (error.response?.data?.message) {
        setErrorMessage(error.response.data.message);
      } else if (error.response?.status === 401) {
        setErrorMessage('Email ou mot de passe incorrect');
      } else if (error.response?.status === 403) {
        setErrorMessage('Ce compte est désactivé');
      } else {
        setErrorMessage('Une erreur est survenue. Veuillez réessayer.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-blue/10 via-white to-secondary-amber/10 flex items-center justify-center p-4">
      <Card className="w-full max-w-md animate-scaleIn">
        <Card.Body className="p-8">
          {/* Header */}
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-br from-primary-blue to-primary-blue-dark rounded-2xl mb-4 shadow-lg">
              <FiShield className="text-white text-2xl" />
            </div>
            <h1 className="text-3xl font-heading font-bold text-gray-900 mb-2">Connexion</h1>
            <p className="text-gray-600">Système d'Information Relevés</p>
            <p className="text-sm text-gray-500 mt-1">Rabat Energie & Eau</p>
          </div>

          {/* Formulaire */}
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
            {/* Email */}
            <Input
              label="Email"
              type="email"
              leftIcon={FiMail}
              placeholder="votre.email@example.com"
              error={errors.email?.message}
              {...register('email', {
                required: 'L\'email est obligatoire',
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: 'Format d\'email invalide',
                },
              })}
            />

            {/* Mot de passe */}
            <Input
              label="Mot de passe"
              type="password"
              leftIcon={FiLock}
              placeholder="••••••••"
              error={errors.password?.message}
              {...register('password', {
                required: 'Le mot de passe est obligatoire',
              })}
            />

            {/* Message d'erreur */}
            {errorMessage && (
              <div className="bg-red-50 border-l-4 border-red-500 rounded-lg p-4">
                <p className="text-sm font-medium text-red-800">{errorMessage}</p>
              </div>
            )}

            {/* Bouton de soumission */}
            <Button
              type="submit"
              variant="primary"
              size="lg"
              isLoading={isLoading}
              className="w-full"
            >
              {!isLoading && <FiLogIn className="mr-2" />}
              {isLoading ? 'Connexion en cours...' : 'Se connecter'}
            </Button>
          </form>
        </Card.Body>
      </Card>
    </div>
  );
}

export default LoginPage;

