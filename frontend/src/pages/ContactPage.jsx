import { Link } from 'react-router-dom';
import { FiArrowLeft, FiMail, FiPhone, FiInfo } from 'react-icons/fi';

function ContactPage() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-indigo-50">
      {/* Header */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-blue-600 rounded-lg flex items-center justify-center">
                <FiInfo className="text-white text-xl" />
              </div>
              <div>
                <h1 className="text-xl font-bold text-gray-900">SI Relevés</h1>
                <p className="text-xs text-gray-500">Rabat Energie & Eau</p>
              </div>
            </div>
            <Link
              to="/"
              className="px-4 py-2 text-blue-600 hover:text-blue-700 font-medium transition-colors flex items-center gap-2"
            >
              <FiArrowLeft />
              Retour à l'accueil
            </Link>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="bg-white rounded-xl shadow-lg p-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-6">
            Demander un Compte
          </h2>

          <div className="bg-blue-50 border-l-4 border-blue-600 p-6 mb-8 rounded-r-lg">
            <h3 className="text-lg font-semibold text-blue-900 mb-3 flex items-center gap-2">
              <FiInfo className="text-blue-600" />
              Comment obtenir un compte ?
            </h3>
            <p className="text-blue-800 mb-4">
              Les comptes utilisateurs sont créés uniquement par les <strong>Superadmin</strong> de la plateforme.
              Pour obtenir un compte, vous devez contacter un administrateur système.
            </p>
            <div className="bg-white rounded-lg p-4 mt-4">
              <h4 className="font-semibold text-gray-900 mb-3">Processus de création de compte :</h4>
              <ol className="list-decimal list-inside space-y-2 text-gray-700">
                <li>Contactez un Superadmin de votre organisation</li>
                <li>Le Superadmin créera votre compte avec vos informations (nom, prénom, email, rôle)</li>
                <li>Un mot de passe sécurisé sera généré automatiquement</li>
                <li>Vous recevrez vos identifiants de connexion</li>
                <li>À votre première connexion, vous devrez changer votre mot de passe</li>
              </ol>
            </div>
          </div>

          <div className="space-y-6">
            <div>
              <h3 className="text-xl font-semibold text-gray-900 mb-4">Rôles Disponibles</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="border border-gray-200 rounded-lg p-4">
                  <h4 className="font-semibold text-purple-600 mb-2">Superadmin</h4>
                  <p className="text-sm text-gray-600">
                    Accès complet à toutes les fonctionnalités, y compris la gestion des utilisateurs.
                  </p>
                </div>
                <div className="border border-gray-200 rounded-lg p-4">
                  <h4 className="font-semibold text-blue-600 mb-2">Utilisateur</h4>
                  <p className="text-sm text-gray-600">
                    Accès aux fonctionnalités métier : gestion des compteurs, agents, quartiers et relevés.
                  </p>
                </div>
              </div>
            </div>

            <div className="border-t pt-6">
              <h3 className="text-xl font-semibold text-gray-900 mb-4">Contact</h3>
              <div className="space-y-4">
                <div className="flex items-start gap-3">
                  <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center flex-shrink-0">
                    <FiMail className="text-blue-600" />
                  </div>
                  <div>
                    <h4 className="font-semibold text-gray-900">Email</h4>
                    <p className="text-gray-600">Contactez votre administrateur système</p>
                    <p className="text-blue-600">admin@ree.ma</p>
                  </div>
                </div>
                <div className="flex items-start gap-3">
                  <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center flex-shrink-0">
                    <FiPhone className="text-green-600" />
                  </div>
                  <div>
                    <h4 className="font-semibold text-gray-900">Téléphone</h4>
                    <p className="text-gray-600">Service informatique</p>
                    <p className="text-green-600">+212 XXX XXX XXX</p>
                  </div>
                </div>
              </div>
            </div>

            <div className="bg-gray-50 rounded-lg p-6 mt-8">
              <h4 className="font-semibold text-gray-900 mb-2">Vous avez déjà un compte ?</h4>
              <p className="text-gray-600 mb-4">
                Si vous avez déjà reçu vos identifiants, vous pouvez vous connecter directement.
              </p>
              <Link
                to="/login"
                className="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
              >
                Se connecter
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ContactPage;

