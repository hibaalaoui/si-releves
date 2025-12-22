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

    // Trouver tous les compteurs actifs avec relations chargées
    @Query("SELECT DISTINCT c FROM Compteur c " +
           "LEFT JOIN FETCH c.adresse a " +
           "LEFT JOIN FETCH a.quartier q " +
           "LEFT JOIN FETCH a.client " +
           "WHERE c.actif = true")
    List<Compteur> findByActifTrueWithRelations();
    
    // Méthode originale conservée pour compatibilité
    List<Compteur> findByActifTrue();

    // Trouver les compteurs d'une adresse avec relations chargées
    @Query("SELECT DISTINCT c FROM Compteur c " +
           "LEFT JOIN FETCH c.adresse a " +
           "LEFT JOIN FETCH a.quartier q " +
           "LEFT JOIN FETCH a.client " +
           "WHERE a.idAdresse = :idAdresse")
    List<Compteur> findByAdresseIdAdresseWithRelations(@Param("idAdresse") Integer idAdresse);
    
    // Méthode originale conservée pour compatibilité
    List<Compteur> findByAdresseIdAdresse(Integer idAdresse);

    // Trouver les compteurs par type
    List<Compteur> findByType(Compteur.TypeCompteur type);

    // Trouver les compteurs par quartier avec relations chargées
    @Query("SELECT DISTINCT c FROM Compteur c " +
           "LEFT JOIN FETCH c.adresse a " +
           "LEFT JOIN FETCH a.quartier q " +
           "LEFT JOIN FETCH a.client " +
           "WHERE q.idQuartier = :idQuartier")
    List<Compteur> findByQuartierWithRelations(@Param("idQuartier") Integer idQuartier);
    
    // Méthode originale conservée pour compatibilité
    @Query("SELECT c FROM Compteur c JOIN c.adresse a WHERE a.quartier.idQuartier = :idQuartier")
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

    // Trouver tous les compteurs avec leurs relations chargées (adresse et quartier)
    @Query("SELECT DISTINCT c FROM Compteur c " +
           "LEFT JOIN FETCH c.adresse a " +
           "LEFT JOIN FETCH a.quartier q " +
           "LEFT JOIN FETCH a.client")
    List<Compteur> findAllWithRelations();
    
    // Trouver un compteur par ID avec relations chargées
    @Query("SELECT DISTINCT c FROM Compteur c " +
           "LEFT JOIN FETCH c.adresse a " +
           "LEFT JOIN FETCH a.quartier q " +
           "LEFT JOIN FETCH a.client " +
           "WHERE c.idCompteur = :idCompteur")
    Optional<Compteur> findByIdWithRelations(@Param("idCompteur") String idCompteur);
}