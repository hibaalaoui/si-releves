package ma.ree.sireleves.repository;

import ma.ree.sireleves.entity.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Integer> {

    // Trouver les adresses d'un client
    List<Adresse> findByClientIdClient(String idClient);

    // Trouver les adresses d'un quartier
    List<Adresse> findByQuartierIdQuartier(Integer idQuartier);

    // Trouver les adresses par type de bien
    List<Adresse> findByTypeBien(Adresse.TypeBien typeBien);
}