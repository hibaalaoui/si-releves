package ma.ree.sireleves.repository;

import ma.ree.sireleves.entity.Compteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteurRepository extends JpaRepository<Compteur, String> {

    // Trouver tous les compteurs actifs
    List<Compteur> findByActifTrue();

    // Trouver les compteurs d'une adresse
    List<Compteur> findByAdresseIdAdresse(Integer idAdresse);

    // Trouver les compteurs par type
    List<Compteur> findByType(Compteur.TypeCompteur type);

    // Trouver les compteurs par quartier
    @Query("SELECT c FROM Compteur c WHERE c.adresse.quartier.idQuartier = :idQuartier")
    List<Compteur> findByQuartier(@Param("idQuartier") Integer idQuartier);

    // Trouver les compteurs sans relevé depuis X jours
    @Query("SELECT c FROM Compteur c WHERE c.actif = true AND " +
            "(c.dateDerniereReleve IS NULL OR " +
            "DATEDIFF(CURRENT_DATE, c.dateDerniereReleve) > :nbJours)")
    List<Compteur> findCompteursNonRelevesDepuis(@Param("nbJours") int nbJours);

    // Compter le nombre de compteurs d'une adresse (pour validation max 4)
    @Query("SELECT COUNT(c) FROM Compteur c WHERE c.adresse.idAdresse = :idAdresse")
    long countByAdresse(@Param("idAdresse") Integer idAdresse);

    // Compter les compteurs standards d'une adresse (max 2)
    @Query("SELECT COUNT(c) FROM Compteur c WHERE c.adresse.idAdresse = :idAdresse AND c.pourEspacesCommuns = false")
    long countCompteurStandardByAdresse(@Param("idAdresse") Integer idAdresse);

    // Compter les compteurs espaces communs d'une adresse (max 2)
    @Query("SELECT COUNT(c) FROM Compteur c WHERE c.adresse.idAdresse = :idAdresse AND c.pourEspacesCommuns = true")
    long countCompteurEspaceCommunByAdresse(@Param("idAdresse") Integer idAdresse);

    // Vérifier si un compteur de ce type existe déjà pour cette adresse
    boolean existsByAdresseIdAdresseAndTypeAndPourEspacesCommuns(
            Integer idAdresse,
            Compteur.TypeCompteur type,
            Boolean pourEspacesCommuns
    );
}