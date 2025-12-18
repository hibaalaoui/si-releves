# ============================================
# Build Stage - Compilation avec Node.js
# ============================================
FROM node:20-alpine AS builder
WORKDIR /app

# Configure npm for better reliability
RUN npm config set registry https://registry.npmjs.org/ && \
    npm config set fetch-retry-mintimeout 20000 && \
    npm config set fetch-retry-maxtimeout 120000 && \
    npm config set fetch-retries 5

# Copier les fichiers de dépendances
COPY package*.json ./

# Installer les dépendances avec retry logic
RUN npm install --legacy-peer-deps --no-audit --no-fund --loglevel verbose || \
    npm install --legacy-peer-deps --no-audit --no-fund --loglevel verbose || \
    npm install --legacy-peer-deps --no-audit --no-fund --loglevel verbose

# Copier le code source
COPY . .

# Build l'application pour production
RUN npm run build

# ============================================
# Runtime Stage - Servir avec Nginx
# ============================================
FROM nginx:alpine

# Installer wget pour health checks
RUN apk add --no-cache wget

# Copier les fichiers buildés depuis le stage de build
COPY --from=builder /app/dist /usr/share/nginx/html

# Copier la configuration Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Exposer le port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Nginx démarre automatiquement en foreground
CMD ["nginx", "-g", "daemon off;"]