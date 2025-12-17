package ma.ree.sireleves.repository;

import ma.ree.sireleves.entity.Quartier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Integer> {

    // Vérifier si un quartier existe déjà par son nom
    boolean existsByNomQuartier(String nomQuartier);

    // Trouver un quartier par son nom
    Optional<Quartier> findByNomQuartier(String nomQuartier);
}