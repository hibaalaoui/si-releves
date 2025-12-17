# SI Relevés - Frontend

Frontend application for the Water and Electricity Meter Readings Management System.

## Tech Stack

- **React 18** - UI library
- **Vite 5** - Build tool and dev server
- **Tailwind CSS 3** - Utility-first CSS framework
- **React Router DOM** - Client-side routing
- **Zustand** - State management
- **React Query** - Server state management
- **Axios** - HTTP client
- **React Hook Form + Yup** - Form handling and validation
- **Recharts** - Chart library
- **React Icons** - Icon library
- **date-fns** - Date utilities
- **jwt-decode** - JWT token decoding

## Getting Started

### Prerequisites

- Node.js 18+ and npm/yarn/pnpm

### Installation

```bash
npm install
```

### Environment Variables

Create a `.env` file in the root directory:

```env
VITE_API_URL=http://localhost:8080
```

### Development

```bash
npm run dev
```

The application will be available at `http://localhost:3000`

### Build

```bash
npm run build
```

### Preview Production Build

```bash
npm run preview
```

## Project Structure

```
frontend/
├── src/
│   ├── api/              # API configuration (Axios instance)
│   ├── components/       # Reusable components
│   │   ├── common/       # Common components (Button, Input, Modal, etc.)
│   │   ├── layout/       # Layout components (Header, Sidebar, Layout)
│   │   └── charts/       # Chart components
│   ├── pages/            # Page components
│   │   ├── auth/         # Authentication pages
│   │   ├── dashboard/    # Dashboard page
│   │   ├── compteurs/    # Compteur management pages
│   │   ├── agents/       # Agent management pages
│   │   └── releves/      # Relevé management pages
│   ├── hooks/            # Custom React hooks
│   ├── store/            # Zustand stores
│   ├── utils/            # Utility functions and constants
│   ├── routes/           # Route configuration
│   ├── App.jsx           # Main App component (not used with React Router v6)
│   └── main.jsx          # Application entry point
├── index.html
├── package.json
├── vite.config.js
├── tailwind.config.js
└── postcss.config.js
```

## Features

- ✅ JWT-based authentication
- ✅ Protected routes with role-based access control
- ✅ Responsive layout with sidebar navigation
- ✅ Axios interceptors for automatic token injection
- ✅ React Query for server state management
- ✅ Form validation with React Hook Form + Yup
- ✅ Tailwind CSS for styling

## Development Guidelines

- Use functional components with hooks
- Follow the folder structure for organization
- Use Zustand for global state (auth, UI state)
- Use React Query for server state and API calls
- Use Tailwind CSS utility classes (no custom CSS files)
- Follow naming conventions: PascalCase for components, camelCase for functions

