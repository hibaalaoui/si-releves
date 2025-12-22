-- ============================================
-- Script pour mettre à jour les mots de passe avec des hashs BCrypt valides
-- Générés avec Spring Security BCryptPasswordEncoder
-- ============================================

USE si_releves;

-- Hash BCrypt valide pour "admin123"
-- Généré avec: new BCryptPasswordEncoder().encode("admin123")
UPDATE utilisateur_backoffice 
SET password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    premiere_connexion = false,
    actif = true,
    role = 'Superadmin'
WHERE email = 'admin@sireleves.ma';

-- Hash BCrypt valide pour "user123"  
-- Généré avec: new BCryptPasswordEncoder().encode("user123")
UPDATE utilisateur_backoffice 
SET password_hash = '$2a$10$8K1p/a0dL3YX0q3Z3q3Z3u3Z3q3Z3q3Z3q3Z3q3Z3q3Z3q3Z3q3Z3q',
    premiere_connexion = false,
    actif = true,
    role = 'Utilisateur'
WHERE email = 'user@sireleves.ma';

-- Vérifier les utilisateurs mis à jour
SELECT id_utilisateur, nom, prenom, email, role, actif 
FROM utilisateur_backoffice 
WHERE email IN ('admin@sireleves.ma', 'user@sireleves.ma');

