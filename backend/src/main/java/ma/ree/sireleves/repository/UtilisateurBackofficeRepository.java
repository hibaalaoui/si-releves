package ma.ree.sireleves.repository;

import ma.ree.sireleves.entity.UtilisateurBackoffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurBackofficeRepository extends JpaRepository<UtilisateurBackoffice, Integer> {

    // Recherche par email (pour l'authentification)
    Optional<UtilisateurBackoffice> findByEmail(String email);

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);

    // Trouver tous les utilisateurs actifs
    List<UtilisateurBackoffice> findByActifTrue();

    // Filtrer par rôle
    List<UtilisateurBackoffice> findByRole(UtilisateurBackoffice.Role role);

    // Filtrer par rôle et statut actif
    List<UtilisateurBackoffice> findByRoleAndActifTrue(UtilisateurBackoffice.Role role);
}