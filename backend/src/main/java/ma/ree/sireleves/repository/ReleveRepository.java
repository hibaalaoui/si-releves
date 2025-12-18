package ma.ree.sireleves.repository;

import ma.ree.sireleves.entity.Releve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReleveRepository extends JpaRepository<Releve, Integer> {

    // Trouver les relevés d'un compteur
    List<Releve> findByCompteurIdCompteur(String idCompteur);

    // Trouver les relevés d'un agent
    List<Releve> findByAgentIdAgent(String idAgent);

    // Trouver les relevés dans une période
    List<Releve> findByDateReleveBetween(LocalDateTime debut, LocalDateTime fin);

    // Trouver les relevés non envoyés à la facturation
    List<Releve> findByEnvoyeFacturationFalse();

    // Trouver les relevés par quartier
    @Query("SELECT r FROM Releve r WHERE r.compteur.adresse.quartier.idQuartier = :idQuartier")
    List<Releve> findByQuartier(@Param("idQuartier") Integer idQuartier);

    // Trouver le dernier relevé d'un compteur
    @Query("SELECT r FROM Releve r WHERE r.compteur.idCompteur = :idCompteur ORDER BY r.dateReleve DESC LIMIT 1")
    Releve findLastReleveByCompteur(@Param("idCompteur") String idCompteur);

    // Compter les relevés d'un agent sur une période
    @Query("SELECT COUNT(r) FROM Releve r WHERE r.agent.idAgent = :idAgent " +
            "AND r.dateReleve BETWEEN :debut AND :fin")
    long countRelevesByAgentAndPeriode(
            @Param("idAgent") String idAgent,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin
    );
}