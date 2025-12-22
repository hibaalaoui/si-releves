# 🔑 Comptes de Test - SI Relevés

## Comptes Disponibles

### 1. Superadmin
- **Email** : `admin@sireleves.ma`
- **Mot de passe** : `admin123`
- **Rôle** : Superadmin
- **Accès** : Toutes les fonctionnalités (gestion des utilisateurs, etc.)

### 2. Utilisateur Normal
- **Email** : `user@sireleves.ma`
- **Mot de passe** : `user123`
- **Rôle** : Utilisateur
- **Accès** : Fonctionnalités limitées (pas d'accès à la gestion des utilisateurs)

## ⚠️ Problème Actuel

Les mots de passe dans la base de données ont des hashs BCrypt **invalides**, ce qui cause l'erreur "Email ou mot de passe incorrect".

## 🔧 Solution : Réinitialiser les Mots de Passe

### Option 1 : Via l'API (Recommandé)

Une fois le backend complètement démarré, utilisez :

```bash
curl -X POST http://localhost:8080/api/auth/reset-test-passwords
```

### Option 2 : Via SQL Directement

Si l'endpoint ne fonctionne pas, vous pouvez mettre à jour directement dans la base de données après avoir généré les hashs BCrypt valides.

### Option 3 : Le DataInitializer

Le `DataInitializer` devrait automatiquement mettre à jour les mots de passe au démarrage du backend. Vérifiez les logs :

```bash
docker compose logs backend | grep -i "DataInitializer\|Admin\|Utilisateur"
```

## 📝 Test de Connexion

1. Allez sur : `http://localhost/login`
2. Connectez-vous avec l'un des comptes ci-dessus
3. Si l'erreur persiste, les mots de passe doivent être réinitialisés

