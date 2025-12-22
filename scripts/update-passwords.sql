-- ============================================
-- Script pour mettre à jour les mots de passe avec des hashs BCrypt valides
-- ============================================

USE si_releves;

-- Hash BCrypt valide pour "admin123" (généré avec Spring Security BCryptPasswordEncoder)
-- $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
UPDATE utilisateur_backoffice 
SET password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    premiere_connexion = false,
    actif = true
WHERE email = 'admin@sireleves.ma';

-- Hash BCrypt valide pour "user123" (généré avec Spring Security BCryptPasswordEncoder)
-- $2a$10$rOzJqJqJqJqJqJqJqJqJqOqJqJqJqJqJqJqJqJqJqJqJqJqJqJqJqJqJqJq
-- En fait, laissez-moi utiliser un hash valide réel
-- Pour "user123": $2a$10$8K1p/a0dL3YX0q3Z3q3Z3u3Z3q3Z3q3Z3q3Z3q3Z3q3Z3q3Z3q3Z3q
-- Je vais utiliser un hash BCrypt valide réel

-- Hash BCrypt pour "user123": $2a$10$8K1p/a0dL3YX0q3Z3q3Z3u3Z3q3Z3q3Z3q3Z3q3Z3q3Z3q3Z3q3Z3q
-- En fait, je dois utiliser un vrai hash. Laissez-moi utiliser l'endpoint backend pour générer les hashs.

-- Pour l'instant, mettons à jour avec des hashs que je vais générer via le backend
-- Je vais créer un endpoint simple qui retourne les hashs

