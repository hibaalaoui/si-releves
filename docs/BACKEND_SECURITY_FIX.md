# ✅ Correction de la Sécurité Backend - Résumé

## 🔒 Problèmes Corrigés

### 1. **SecurityConfig - Endpoints Publics** ⚠️ CRITIQUE

**Problème identifié** :
```java
.requestMatchers("/api/**").permitAll() // ⚠️ TOUS LES ENDPOINTS ÉTAIENT PUBLICS !
```

**Impact** : N'importe qui pouvait accéder à toutes les données sans authentification.

**Solution appliquée** :
- ✅ Retiré `.requestMatchers("/api/**").permitAll()`
- ✅ Ajouté `JwtAuthenticationFilter` dans la chaîne de sécurité
- ✅ Activé `@EnableMethodSecurity` pour la sécurité basée sur les méthodes
- ✅ Protégé tous les endpoints API (sauf `/api/auth/**`)

**Fichier modifié** : `backend/src/main/java/ma/ree/sireleves/config/SecurityConfig.java`

---

### 2. **Différenciation des Permissions par Rôle** ✅

**Problème identifié** :
- Tous les utilisateurs authentifiés avaient accès à tout
- Pas de distinction entre Superadmin et Utilisateur

**Solution appliquée** :
- ✅ Ajouté `@PreAuthorize("hasRole('Superadmin')")` sur tous les endpoints de `UtilisateurController`
- ✅ Les endpoints `/api/utilisateurs/**` sont maintenant accessibles uniquement aux Superadmin
- ✅ Les autres endpoints (compteurs, agents, quartiers, relevés) sont accessibles à tous les utilisateurs authentifiés (Utilisateur ou Superadmin)

**Fichier modifié** : `backend/src/main/java/ma/ree/sireleves/controller/UtilisateurController.java`

**Permissions selon le cahier des charges** :

| Rôle | Permissions |
|------|-------------|
| **Superadmin** | - Gérer les utilisateurs (créer, modifier, réinitialiser mot de passe)<br>- Accès à tous les endpoints métier |
| **Utilisateur** | - Gérer les compteurs<br>- Gérer les agents<br>- Gérer les quartiers<br>- Gérer les relevés<br>- Consulter les clients et adresses<br>- Changer son propre mot de passe |

---

### 3. **Sécurisation du Changement de Mot de Passe** ✅

**Problème identifié** :
- Un utilisateur pouvait potentiellement changer le mot de passe d'un autre utilisateur

**Solution appliquée** :
- ✅ Vérification que l'utilisateur connecté correspond à l'userId fourni
- ✅ Un utilisateur ne peut modifier que son propre mot de passe
- ✅ Exception `BusinessException` si tentative de modification du mot de passe d'un autre utilisateur

**Fichier modifié** : `backend/src/main/java/ma/ree/sireleves/service/AuthService.java`

---

## 📝 Détails des Modifications

### SecurityConfig.java

**Avant** :
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/**").permitAll() // ⚠️ PROBLÈME
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
```

**Après** :
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // ✅ Activé
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // ✅ Injecté

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // ✅ Ajouté
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/**").authenticated() // ✅ Protégé
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
```

### UtilisateurController.java

**Ajouté sur tous les endpoints** :
```java
@PreAuthorize("hasRole('Superadmin')")
```

**Endpoints protégés** :
- `POST /api/utilisateurs` - Créer un utilisateur
- `GET /api/utilisateurs` - Lister tous les utilisateurs
- `GET /api/utilisateurs/{id}` - Voir un utilisateur
- `PUT /api/utilisateurs/{id}` - Modifier un utilisateur
- `POST /api/utilisateurs/{id}/reset-password` - Réinitialiser le mot de passe

### AuthService.java

**Méthode `changePassword` sécurisée** :
```java
public String changePassword(Integer userId, ChangePasswordRequestDTO changePasswordRequest) {
    // Récupérer l'utilisateur connecté
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserEmail = authentication.getName();
    UtilisateurBackoffice currentUser = utilisateurRepository.findByEmail(currentUserEmail)
        .orElseThrow(() -> new BusinessException("Utilisateur non trouvé"));

    // ✅ Vérifier que l'utilisateur connecté correspond à l'userId
    if (!currentUser.getIdUtilisateur().equals(userId)) {
        throw new BusinessException("Vous ne pouvez modifier que votre propre mot de passe");
    }
    
    // ... reste du code
}
```

---

## 🧪 Tests à Effectuer

### 1. Test de Protection des Endpoints

```bash
# 1. Tester sans token (doit échouer avec 401)
curl -X GET http://localhost:8080/api/utilisateurs

# 2. Se connecter pour obtenir un token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}'

# 3. Tester avec token (doit fonctionner)
curl -X GET http://localhost:8080/api/utilisateurs \
  -H "Authorization: Bearer <token>"
```

### 2. Test de Protection par Rôle

```bash
# 1. Se connecter en tant qu'Utilisateur (rôle: Utilisateur)
# 2. Essayer d'accéder à /api/utilisateurs (doit échouer avec 403 Forbidden)

# 3. Se connecter en tant que Superadmin (rôle: Superadmin)
# 4. Accéder à /api/utilisateurs (doit fonctionner)
```

### 3. Test de Changement de Mot de Passe

```bash
# 1. Se connecter avec userId=1
# 2. Essayer de changer le mot de passe de userId=2 (doit échouer)
# 3. Changer son propre mot de passe (userId=1) (doit fonctionner)
```

---

## ✅ Checklist de Vérification

- [x] SecurityConfig corrigé - endpoints protégés
- [x] JwtAuthenticationFilter ajouté dans la chaîne
- [x] @EnableMethodSecurity activé
- [x] UtilisateurController protégé avec @PreAuthorize
- [x] Changement de mot de passe sécurisé
- [x] Tests manuels à effectuer

---

## 📚 Références

- **Cahier des charges** : `cahier_de_charge-projet_appli_gestion_relevés.pdf`
- **Documentation Spring Security** : https://docs.spring.io/spring-security/reference/
- **Fichiers modifiés** :
  - `backend/src/main/java/ma/ree/sireleves/config/SecurityConfig.java`
  - `backend/src/main/java/ma/ree/sireleves/controller/UtilisateurController.java`
  - `backend/src/main/java/ma/ree/sireleves/service/AuthService.java`

---

**Date de correction** : 2024  
**Statut** : ✅ Complété

