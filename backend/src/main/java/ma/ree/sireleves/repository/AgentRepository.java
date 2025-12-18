package ma.ree.sireleves.repository;

import ma.ree.sireleves.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {

    // Trouver les agents actifs
    List<Agent> findByActifTrue();

    // Trouver les agents d'un quartier
    List<Agent> findByQuartierIdQuartier(Integer idQuartier);

    // Trouver les agents actifs d'un quartier
    List<Agent> findByQuartierIdQuartierAndActifTrue(Integer idQuartier);
}