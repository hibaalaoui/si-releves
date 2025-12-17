package ma.ree.sireleves.user.repository;

import ma.ree.sireleves.common.enums.Role;
import ma.ree.sireleves.user.entity.UtilisateurBackoffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UtilisateurBackoffice, Integer> {
    
    Optional<UtilisateurBackoffice> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<UtilisateurBackoffice> findByRole(Role role);
    
    List<UtilisateurBackoffice> findByActif(Boolean actif);
    
    List<UtilisateurBackoffice> findByRoleAndActif(Role role, Boolean actif);
}

