package ma.ree.sireleves.agent.repository;

import ma.ree.sireleves.agent.entity.Agent;
import ma.ree.sireleves.common.entity.Quartier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
    
    List<Agent> findByQuartier(Quartier quartier);
    
    List<Agent> findByQuartierIdQuartier(Integer idQuartier);
    
    List<Agent> findByActif(Boolean actif);
    
    List<Agent> findByQuartierAndActif(Quartier quartier, Boolean actif);
    
    List<Agent> findByQuartierIdQuartierAndActif(Integer idQuartier, Boolean actif);
    
    boolean existsByIdAgent(String idAgent);
}

