import { createBrowserRouter, Navigate } from 'react-router-dom';
import ProtectedRoute from './ProtectedRoute';
import Layout from '../components/layout/Layout';
import Login from '../pages/auth/Login';
import Dashboard from '../pages/dashboard/Dashboard';
import { ROLES } from '../utils/constants';

// Placeholder components for other pages
const CompteursList = () => <div className="p-6">Compteurs List - Coming Soon</div>;
const CompteurDetails = () => <div className="p-6">Compteur Details - Coming Soon</div>;
const AgentsList = () => <div className="p-6">Agents List - Coming Soon</div>;
const AgentDetails = () => <div className="p-6">Agent Details - Coming Soon</div>;
const RelevesList = () => <div className="p-6">Relevés List - Coming Soon</div>;
const ReleveDetails = () => <div className="p-6">Relevé Details - Coming Soon</div>;
const UsersList = () => <div className="p-6">Users List - Coming Soon</div>;

export const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login />,
  },
  {
    path: '/',
    element: (
      <ProtectedRoute>
        <Layout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <Navigate to="/dashboard" replace />,
      },
      {
        path: 'dashboard',
        element: <Dashboard />,
      },
      {
        path: 'compteurs',
        children: [
          {
            index: true,
            element: <CompteursList />,
          },
          {
            path: ':id',
            element: <CompteurDetails />,
          },
        ],
      },
      {
        path: 'agents',
        children: [
          {
            index: true,
            element: <AgentsList />,
          },
          {
            path: ':id',
            element: <AgentDetails />,
          },
        ],
      },
      {
        path: 'releves',
        children: [
          {
            index: true,
            element: <RelevesList />,
          },
          {
            path: ':id',
            element: <ReleveDetails />,
          },
        ],
      },
      {
        path: 'users',
        element: (
          <ProtectedRoute requiredRole={ROLES.SUPERADMIN}>
            <UsersList />
          </ProtectedRoute>
        ),
      },
    ],
  },
]);

