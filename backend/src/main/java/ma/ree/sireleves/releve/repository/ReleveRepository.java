package ma.ree.sireleves.releve.repository;

import ma.ree.sireleves.agent.entity.Agent;
import ma.ree.sireleves.compteur.entity.Compteur;
import ma.ree.sireleves.releve.entity.Releve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReleveRepository extends JpaRepository<Releve, Integer> {
    
    List<Releve> findByCompteur(Compteur compteur);
    
    List<Releve> findByCompteurIdCompteur(String idCompteur);
    
    List<Releve> findByAgent(Agent agent);
    
    List<Releve> findByAgentIdAgent(String idAgent);
    
    List<Releve> findByDateReleveBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Releve> findByCompteurAndDateReleveBetween(Compteur compteur, LocalDateTime startDate, LocalDateTime endDate);
    
    List<Releve> findByAgentAndDateReleveBetween(Agent agent, LocalDateTime startDate, LocalDateTime endDate);
    
    List<Releve> findByEnvoyeFacturation(Boolean envoyeFacturation);
    
    /**
     * Count distinct compteurs with releves in date range
     */
    @Query("SELECT COUNT(DISTINCT r.compteur.idCompteur) FROM Releve r " +
           "WHERE r.dateReleve BETWEEN :startDate AND :endDate")
    long countDistinctCompteursReleves(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    /**
     * Count distinct compteurs with releves in date range filtered by quartier
     */
    @Query("SELECT COUNT(DISTINCT r.compteur.idCompteur) FROM Releve r " +
           "WHERE r.dateReleve BETWEEN :startDate AND :endDate " +
           "AND r.compteur.adresse.quartier.idQuartier = :idQuartier")
    long countDistinctCompteursRelevesByQuartier(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate,
                                                   @Param("idQuartier") Integer idQuartier);
    
    /**
     * Count total releves by agent in date range
     */
    @Query(value = "SELECT COUNT(*) FROM releve r " +
           "WHERE r.id_agent = :idAgent " +
           "AND DATE(r.date_releve) BETWEEN DATE(:startDate) AND DATE(:endDate)",
           nativeQuery = true)
    long countRelevesByAgentAndDateRange(@Param("idAgent") String idAgent,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
    
    /**
     * Calculate average releves per day for an agent in date range
     */
    @Query(value = "SELECT CAST(COUNT(*) AS DOUBLE) / CAST(DATEDIFF(:endDate, :startDate) + 1 AS DOUBLE) " +
           "FROM releve r " +
           "WHERE r.id_agent = :idAgent " +
           "AND DATE(r.date_releve) BETWEEN DATE(:startDate) AND DATE(:endDate)",
           nativeQuery = true)
    Double calculateAverageRelevesPerDay(@Param("idAgent") String idAgent,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find releves by quartier and date range
     */
    @Query("SELECT r FROM Releve r " +
           "WHERE r.compteur.adresse.quartier.idQuartier = :idQuartier " +
           "AND r.dateReleve BETWEEN :startDate AND :endDate " +
           "ORDER BY r.dateReleve DESC")
    List<Releve> findByQuartierAndDateRange(@Param("idQuartier") Integer idQuartier,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get latest releve for a compteur
     */
    @Query("SELECT r FROM Releve r WHERE r.compteur.idCompteur = :idCompteur " +
           "ORDER BY r.dateReleve DESC")
    List<Releve> findLatestReleveByCompteur(@Param("idCompteur") String idCompteur);
    
    /**
     * Count total releves in date range
     */
    @Query("SELECT COUNT(r) FROM Releve r " +
           "WHERE r.dateReleve BETWEEN :startDate AND :endDate")
    long countRelevesByDateRange(@Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);
    
    /**
     * Count releves by quartier in date range
     */
    @Query("SELECT COUNT(r) FROM Releve r " +
           "WHERE r.compteur.adresse.quartier.idQuartier = :idQuartier " +
           "AND r.dateReleve BETWEEN :startDate AND :endDate")
    long countRelevesByQuartierAndDateRange(@Param("idQuartier") Integer idQuartier,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get releves statistics grouped by agent
     */
    @Query("SELECT r.agent.idAgent, COUNT(r) as totalReleves, " +
           "MIN(r.dateReleve) as firstReleve, MAX(r.dateReleve) as lastReleve " +
           "FROM Releve r " +
           "WHERE r.dateReleve BETWEEN :startDate AND :endDate " +
           "GROUP BY r.agent.idAgent")
    List<Object[]> getRelevesStatisticsByAgent(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}

