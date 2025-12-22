# 🔐 Comment Accéder à la Page de Login

## 📍 URL de la Page de Login

**URL directe** : `http://localhost/login`

ou

**URL complète** : `http://localhost:80/login`

---

## 🚪 Comportement de l'Application

### Si vous n'êtes PAS connecté :

1. **Accès direct à une route protégée** (ex: `http://localhost/utilisateurs`)
   - ✅ **Redirection automatique** vers `/login`
   - Vous verrez la page de connexion

2. **Accès direct à `/login`**
   - ✅ Vous verrez directement la page de connexion

### Si vous ÊTES connecté :

1. **Accès à `/login`**
   - ✅ **Redirection automatique** vers la page d'accueil (`/utilisateurs`)

2. **Accès à une route protégée**
   - ✅ Accès autorisé, vous verrez le contenu

---

## 📝 Important : Pas de Page d'Inscription Publique

**Selon le cahier des charges** :
- ❌ Il n'y a **PAS** de page d'inscription publique
- ✅ Les utilisateurs sont créés **uniquement par les Superadmin** via le backoffice
- ✅ Seuls les Superadmin peuvent créer de nouveaux utilisateurs dans la page "Gestion des Utilisateurs"

### Processus de Création d'Utilisateur :

1. Un **Superadmin** se connecte
2. Va dans la page **"Utilisateurs"** (menu de gauche)
3. Clique sur **"+ Nouveau"** pour créer un utilisateur
4. Le système génère automatiquement un mot de passe
5. L'utilisateur reçoit le mot de passe (par email ou autre moyen)
6. L'utilisateur se connecte avec son email et le mot de passe généré
7. À la première connexion, l'utilisateur **doit changer son mot de passe**

---

## 🔧 Si vous voyez le Dashboard sans être connecté

Cela peut arriver si :
1. Il y a un **token invalide** dans le localStorage
2. Le token est **expiré** mais pas encore nettoyé

### Solution :

1. **Ouvrir la Console du Navigateur** (F12)
2. **Aller dans l'onglet Application** (ou Storage)
3. **Local Storage** → `http://localhost`
4. **Supprimer la clé** `auth-storage`
5. **Rafraîchir la page** (F5)

Ou simplement :

1. **Aller directement à** : `http://localhost/login`
2. Vous serez redirigé automatiquement si vous n'êtes pas connecté

---

## 🧪 Test de la Redirection

Pour tester que la protection fonctionne :

1. **Ouvrir une fenêtre de navigation privée** (Ctrl+Shift+N)
2. **Aller à** : `http://localhost/utilisateurs`
3. **Résultat attendu** : Redirection automatique vers `/login`

---

## 📋 Routes Disponibles

### Routes Publiques (sans authentification) :
- ✅ `/login` - Page de connexion
- ✅ `/change-password` - Changement de mot de passe (nécessite d'être connecté)

### Routes Protégées (nécessitent authentification) :
- 🔒 `/` - Redirige vers `/utilisateurs`
- 🔒 `/utilisateurs` - Gestion des utilisateurs (Superadmin uniquement)
- 🔒 `/compteurs` - Gestion des compteurs
- 🔒 `/agents` - Gestion des agents
- 🔒 `/quartiers` - Gestion des quartiers
- 🔒 `/releves` - Gestion des relevés

---

## 🎯 Résumé

1. **Pour se connecter** : Aller à `http://localhost/login`
2. **Pas d'inscription publique** : Les utilisateurs sont créés par les Superadmin
3. **Redirection automatique** : Si non connecté, redirection vers `/login`
4. **Nettoyer le cache** : Si problème, supprimer `auth-storage` du localStorage

---

**Date de création** : 2024

