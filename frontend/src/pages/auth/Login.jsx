import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { authStore } from '../../store/authStore';
import { authService } from '../../api/authService';
import { FiMail, FiLock } from 'react-icons/fi';

const loginSchema = yup.object().shape({
  email: yup
    .string()
    .email('Email invalide')
    .required('Email requis'),
  password: yup
    .string()
    .min(1, 'Le mot de passe est requis')
    .required('Mot de passe requis'),
});

const Login = () => {
  const navigate = useNavigate();
  const login = authStore((state) => state.login);
  const isAuthenticated = authStore((state) => state.isAuthenticated);
  const checkTokenExpiry = authStore((state) => state.checkTokenExpiry);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Redirect if already authenticated
  useEffect(() => {
    if (isAuthenticated && checkTokenExpiry()) {
      navigate('/dashboard', { replace: true });
    }
  }, [isAuthenticated, checkTokenExpiry, navigate]);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(loginSchema),
  });

  const onSubmit = async (data) => {
    setError('');
    setLoading(true);

    try {
      const response = await authService.login(data.email, data.password);
      
      // Extract user info from response
      const userInfo = {
        id: response.id,
        email: response.email,
        nom: response.nom,
        prenom: response.prenom,
        role: response.role,
        premiereConnexion: response.premiereConnexion,
      };

      // Login with token and user info
      login(response.token, userInfo);
      
      // Redirect to dashboard
      navigate('/dashboard', { replace: true });
    } catch (err) {
      const errorMessage = err.response?.data?.message 
        || err.message 
        || 'Erreur lors de la connexion. Veuillez vérifier vos identifiants.';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary-500 to-primary-700">
      <div className="max-w-md w-full mx-4">
        <div className="bg-white rounded-lg shadow-xl p-8">
          <div className="text-center mb-8">
            <h1 className="text-3xl font-bold text-gray-800 mb-2">
              SI Relevés
            </h1>
            <p className="text-gray-600">RABAT ENERGIE & EAU</p>
          </div>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
                {error}
              </div>
            )}

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Email
              </label>
              <div className="relative">
                <FiMail className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                <input
                  type="email"
                  {...register('email')}
                  className="input pl-10"
                  placeholder="votre.email@example.com"
                />
              </div>
              {errors.email && (
                <p className="mt-1 text-sm text-red-600">{errors.email.message}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Mot de passe
              </label>
              <div className="relative">
                <FiLock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                <input
                  type="password"
                  {...register('password')}
                  className="input pl-10"
                  placeholder="••••••••"
                />
              </div>
              {errors.password && (
                <p className="mt-1 text-sm text-red-600">
                  {errors.password.message}
                </p>
              )}
            </div>

            <button
              type="submit"
              disabled={loading}
              className="btn btn-primary w-full py-3 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Connexion...' : 'Se connecter'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;

