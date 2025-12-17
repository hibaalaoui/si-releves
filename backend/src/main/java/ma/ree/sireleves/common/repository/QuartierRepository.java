package ma.ree.sireleves.common.repository;

import ma.ree.sireleves.common.entity.Quartier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Integer> {
    
    Optional<Quartier> findByNomQuartier(String nomQuartier);
    
    boolean existsByNomQuartier(String nomQuartier);
}

