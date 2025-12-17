const Dashboard = () => {
  return (
    <div className="p-6">
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Tableau de bord</h1>
        <p className="text-gray-600 mt-1">
          Vue d'ensemble des statistiques et indicateurs clés
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
        {/* KPI Cards - Placeholder */}
        <div className="card">
          <h3 className="text-sm font-medium text-gray-500 mb-2">
            Taux de couverture
          </h3>
          <p className="text-3xl font-bold text-primary-600">-</p>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-500 mb-2">
            Relevés aujourd'hui
          </h3>
          <p className="text-3xl font-bold text-primary-600">-</p>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-500 mb-2">
            Agents actifs
          </h3>
          <p className="text-3xl font-bold text-primary-600">-</p>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-500 mb-2">
            Compteurs actifs
          </h3>
          <p className="text-3xl font-bold text-primary-600">-</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Charts - Placeholder */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">
            Évolution des relevés
          </h3>
          <div className="h-64 flex items-center justify-center text-gray-400">
            Graphique à venir
          </div>
        </div>
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">
            Répartition par quartier
          </h3>
          <div className="h-64 flex items-center justify-center text-gray-400">
            Graphique à venir
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;

