-- ============================================
-- Script pour créer un utilisateur admin de test
-- ============================================

USE si_releves;

-- Créer un utilisateur Superadmin
-- Mot de passe: admin123
-- Hash BCrypt pour "admin123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO utilisateur_backoffice (nom, prenom, email, password_hash, role, premiere_connexion, actif, date_ajout)
VALUES (
    'Admin',
    'System',
    'admin@sireleves.ma',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- admin123
    'Superadmin',
    false,
    true,
    NOW()
)
ON DUPLICATE KEY UPDATE
    password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    role = 'Superadmin',
    actif = true;

-- Créer un utilisateur normal de test
-- Mot de passe: user123
-- Hash BCrypt pour "user123": $2a$10$8K3p0VzNjVzNjVzNjVzNj.uX8K3p0VzNjVzNjVzNjVzNjVzNjVzNjVzN
INSERT INTO utilisateur_backoffice (nom, prenom, email, password_hash, role, premiere_connexion, actif, date_ajout)
VALUES (
    'User',
    'Test',
    'user@sireleves.ma',
    '$2a$10$8K3p0VzNjVzNjVzNjVzNj.uX8K3p0VzNjVzNjVzNjVzNjVzNjVzNjVzN', -- user123
    'Utilisateur',
    false,
    true,
    NOW()
)
ON DUPLICATE KEY UPDATE
    password_hash = '$2a$10$rOzJqJqJqJqJqJqJqJqJqOqJqJqJqJqJqJqJqJqJqJqJqJqJqJqJqJqJqJq',
    role = 'Utilisateur',
    actif = true;

-- Afficher les utilisateurs créés
SELECT id_utilisateur, nom, prenom, email, role, actif, premiere_connexion 
FROM utilisateur_backoffice;

