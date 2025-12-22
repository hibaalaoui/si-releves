-- ============================================
-- Script d'initialisation MySQL pour SI Relevés
-- ============================================
-- Note: MySQL Docker crée automatiquement :
-- 1. La base de données définie dans MYSQL_DATABASE
-- 2. L'utilisateur défini dans MYSQL_USER avec le mot de passe MYSQL_PASSWORD
-- 3. Et lui accorde déjà les permissions GRANT ALL
--
-- Ce script se concentre sur les optimisations de performance
-- et les configurations spécifiques à l'application

-- ============================================
-- Optimisations MySQL pour production
-- ============================================

-- Configuration des paramètres InnoDB
SET GLOBAL innodb_buffer_pool_size = 512*1024*1024;  -- 512MB
-- innodb_log_file_size est en lecture seule dans MySQL 8.0
-- SET GLOBAL innodb_log_file_size = 128*1024*1024;     -- 128MB
SET GLOBAL innodb_flush_log_at_trx_commit = 2;
-- innodb_flush_method peut ne pas être disponible sur toutes les plateformes
-- SET GLOBAL innodb_flush_method = O_DIRECT;

-- Configuration des connexions
SET GLOBAL max_connections = 200;
SET GLOBAL max_connect_errors = 10000;

-- Configuration du query cache (désactivé en MySQL 8.0, mais gardé pour compatibilité)
-- SET GLOBAL query_cache_size = 0;
-- SET GLOBAL query_cache_type = 0;

-- Configuration des timeouts
SET GLOBAL wait_timeout = 28800;
SET GLOBAL interactive_timeout = 28800;

-- Configuration du charset
SET GLOBAL character_set_server = 'utf8mb4';
SET GLOBAL collation_server = 'utf8mb4_unicode_ci';

-- ============================================
-- Création de l'utilisateur de l'application
-- ============================================
CREATE USER IF NOT EXISTS 'si_user'@'%' IDENTIFIED BY 'si_password';
GRANT ALL PRIVILEGES ON si_releves.* TO 'si_user'@'%';
FLUSH PRIVILEGES;

-- ============================================
-- Logs et monitoring
-- ============================================

-- Activer le slow query log
SET GLOBAL slow_query_log = 1;
SET GLOBAL long_query_time = 2;
SET GLOBAL log_queries_not_using_indexes = 1;

-- ============================================
-- Message de confirmation
-- ============================================
SELECT 'Initialisation MySQL terminée avec succès' AS status;
