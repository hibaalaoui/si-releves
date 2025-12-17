package ma.ree.sireleves.compteur.repository;

import ma.ree.sireleves.common.entity.Adresse;
import ma.ree.sireleves.common.enums.TypeCompteur;
import ma.ree.sireleves.compteur.entity.Compteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteurRepository extends JpaRepository<Compteur, String> {
    
    List<Compteur> findByAdresse(Adresse adresse);
    
    List<Compteur> findByAdresseIdAdresse(Integer idAdresse);
    
    List<Compteur> findByType(TypeCompteur type);
    
    List<Compteur> findByActif(Boolean actif);
    
    List<Compteur> findByAdresseAndType(Adresse adresse, TypeCompteur type);
    
    List<Compteur> findByAdresseAndActif(Adresse adresse, Boolean actif);
    
    List<Compteur> findByTypeAndActif(TypeCompteur type, Boolean actif);
    
    Optional<Compteur> findByAdresseAndTypeAndPourEspacesCommuns(
        Adresse adresse, 
        TypeCompteur type, 
        Boolean pourEspacesCommuns
    );
    
    /**
     * Count standard compteurs (pour_espaces_communs = false) for an address
     */
    @Query("SELECT COUNT(c) FROM Compteur c WHERE c.adresse.idAdresse = :idAdresse AND c.pourEspacesCommuns = false")
    long countStandardCompteursByAdresse(@Param("idAdresse") Integer idAdresse);
    
    /**
     * Count espaces communs compteurs (pour_espaces_communs = true) for an address
     */
    @Query("SELECT COUNT(c) FROM Compteur c WHERE c.adresse.idAdresse = :idAdresse AND c.pourEspacesCommuns = true")
    long countEspacesCommunsCompteursByAdresse(@Param("idAdresse") Integer idAdresse);
    
    /**
     * Check if address can accept a new standard compteur (max 2 standard compteurs per address)
     */
    @Query("SELECT CASE WHEN COUNT(c) < 2 THEN true ELSE false END " +
           "FROM Compteur c WHERE c.adresse.idAdresse = :idAdresse AND c.pourEspacesCommuns = false")
    boolean canAddStandardCompteur(@Param("idAdresse") Integer idAdresse);
    
    /**
     * Check if address can accept a new espaces communs compteur (max 2 for immeubles only)
     */
    @Query("SELECT CASE WHEN COUNT(c) < 2 THEN true ELSE false END " +
           "FROM Compteur c WHERE c.adresse.idAdresse = :idAdresse AND c.pourEspacesCommuns = true")
    boolean canAddEspacesCommunsCompteur(@Param("idAdresse") Integer idAdresse);
    
    /**
     * Find all active compteurs by quartier
     */
    @Query("SELECT c FROM Compteur c WHERE c.adresse.quartier.idQuartier = :idQuartier AND c.actif = true")
    List<Compteur> findActiveCompteursByQuartier(@Param("idQuartier") Integer idQuartier);
    
    /**
     * Find compteurs without recent releve (> 40 days)
     */
    @Query(value = "SELECT * FROM compteur c WHERE c.actif = true AND " +
           "(c.date_derniere_releve IS NULL OR DATEDIFF(CURRENT_DATE, DATE(c.date_derniere_releve)) > 40)",
           nativeQuery = true)
    List<Compteur> findCompteursSansReleveRecent();
}

