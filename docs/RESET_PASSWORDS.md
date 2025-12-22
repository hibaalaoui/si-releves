# 🔑 Réinitialisation des Mots de Passe

## Comptes Utilisateurs Existants

### 1. Superadmin
- **Email** : `admin@sireleves.ma`
- **Nom** : Admin
- **Prénom** : System
- **Rôle** : Superadmin

### 2. Utilisateur Normal
- **Email** : `user@sireleves.ma`
- **Nom** : User
- **Prénom** : Test
- **Rôle** : Utilisateur

## Méthode 1 : Via l'API Backend (Recommandé)

Une fois le backend complètement démarré (attendre ~1-2 minutes), utilisez l'endpoint suivant :

```bash
# Mettre à jour les mots de passe
curl -X POST http://localhost:8080/api/auth/reset-test-passwords
```

**Mots de passe après mise à jour** :
- Admin : `admin123`
- User : `user123`

## Méthode 2 : Via SQL Directement

Si l'endpoint ne fonctionne pas, vous pouvez mettre à jour directement dans la base de données.

**ATTENTION** : Les hashs BCrypt doivent être générés par Spring Security. Utilisez l'endpoint GET pour obtenir les hashs valides :

```bash
# Générer les hashs BCrypt valides
curl http://localhost:8080/api/auth/generate-hashes
```

Puis utilisez les hashs générés dans ce script SQL :

```sql
USE si_releves;

-- Remplacer HASH_ADMIN123 et HASH_USER123 par les hashs générés
UPDATE utilisateur_backoffice 
SET password_hash = 'HASH_ADMIN123',
    premiere_connexion = false,
    actif = true
WHERE email = 'admin@sireleves.ma';

UPDATE utilisateur_backoffice 
SET password_hash = 'HASH_USER123',
    premiere_connexion = false,
    actif = true
WHERE email = 'user@sireleves.ma';
```

## Test de Connexion

1. Allez sur : `http://localhost/login`
2. Connectez-vous avec :
   - **Superadmin** : `admin@sireleves.ma` / `admin123`
   - **Utilisateur** : `user@sireleves.ma` / `user123`

